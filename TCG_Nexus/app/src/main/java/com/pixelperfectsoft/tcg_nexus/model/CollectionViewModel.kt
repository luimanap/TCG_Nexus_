package com.pixelperfectsoft.tcg_nexus.model

import android.telephony.TelephonyCallback.DataActivationStateListener
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class CollectionViewModel : ViewModel() {
    var state: MutableState<DataState> = mutableStateOf(DataState.Empty)
    val collection = mutableStateOf(Collect())
    val cards = mutableListOf<Card>()


    /*fun searchCardsByName(searchinput: String) {
        /*
          * Buscando cartas por nombre:
          * 1. Nos creamos un array de cartas vacio
          * 2. Ponemos el estado de la respuesta en cargando
          * 3. Recorremos el array de cartas templist que es el que obtiene todas las cartas de Firebase
          * 4. Si el nombre de la carta en minusculas contiene lo que introducimos se va a añadir al nuevo array
          * 5. Ponemos el estado de la respuesta en completado y le pasamos el nuevo array
        */
        var searchList = mutableListOf<Card>()
        state.value = DataState.Loading
        for (i in tempList) {
            if (i.name.toString().lowercase().contains(searchinput.lowercase())) {
                searchList.add(i)
            }
        }
        state.value = DataState.Success(searchList)

    }*/

    /*fun resetSearch() {
        response.value = DataState.Success(tempList)
    }*/
    fun updateCollection(cards: List<String>) {
        /*
        * Actualizando la coleccion
        * 1. Obtenemos el id del usuario actualmente logueado
        * 2. Obtenemos los documentos que contengan el atributo user_id coincidente con el id del usuario
        * 3. En cada documento que encuentre, actualizamos el atributo "cards" con el array de
        *    String que le pasaremos como parametro
        * 4. Mostramos logs tanto de finalizacion correcta como de error en cada caso
         */
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        FirebaseFirestore.getInstance().collection("collections").whereEqualTo("user_id", userId)
            .get().addOnSuccessListener {
                for (document in it.documents) {
                    // Actualiza cada documento encontrado con los nuevos datos
                    FirebaseFirestore.getInstance().collection("collections").document(document.id)
                        .update("cards", cards)
                        .addOnSuccessListener {
                            Log.d("collection_update", "Cards updated successfully")
                        }
                        .addOnFailureListener { e ->
                            Log.w("collection_update", "Error updating cards -> $e")
                        }
                }
            }
    }

    fun createCollection() {
        /*
        * Creando la coleccion:
        * 1. Obtenemos el id del usuario actualmente logueado
        * 2. Nos creamos un objeto de la clase coleccion con el id del usuario y un array de cartas
        *    vacio. En este array se van a almacenar los id de las cartas en la otra base de datos
        * 3. Obtenemos la coleccion de colecciones y añadimos un documento con el objeto
        *    que nos acabamos de crear
        * 4. Si se crea correctamente, actualizamos el atributo id de la coleccion con el id del
        *    usuario y mostramos el log por consola
        * 5. Si hay algun error al crearlo mostramos el log correspondiente al error por consola
         */
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        val collection = userId?.let {
            Collect(
                collection_id = "",
                user_id = it,
                cards = mutableListOf()
            ).toMap()
        }
        if (collection != null) {
            FirebaseFirestore.getInstance().collection("collections").add(collection)
                .addOnSuccessListener {
                    it.update("collection_id", it.id)
                    Log.d("collection", "createCol: Collection ${it.id} created successfully")
                }.addOnFailureListener {
                    Log.w("collection", "createCol: Unspected error creating collection $it")
                }
        }
    }

    init {
        /*
            Este metodo se va a ejecutar cuando se instancie la clase CollectionViewModel
         */
        get_collection()
    }

    fun get_collection() {
        viewModelScope.launch {
            collection.value = retrieveCollection()
        }
    }

    suspend fun retrieveCollection(): Collect {
        /*
         * Obteniendo la coleccion del usuario:
         * 1. Nos creamos una coleccion vacia
         * 2. Obtenemos el uid del usuario actualmente logueado
         * 3. Obtenemos la coleccion de colecciones y le decimos que obtenga los documentos que
         *      tengan el atributo "user_id" que coincida con el id del usuario actualmente logueado
         * 4. Parseamos el resultado de la query a un objeto de la clase Collect
         * 5. Si el userId no es nulo, devolvemos el resultado con la variable current_collection
        */
        var current_collection = mutableStateOf(Collect())
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        try {
            FirebaseFirestore.getInstance()
                .collection("collections")
                .whereEqualTo("user_id", userId)
                .get().await().map {
                    val result = it.toObject(Collect::class.java)
                    if (userId != null) {
                        Log.d("collection-retrieve", "Collection found -> $collection")
                        current_collection.value = result
                    }
                }
        } catch (e: FirebaseFirestoreException) {
            Log.w("collection-retrieve", "Error retrieving collection data: $e")
        }
        return current_collection.value
    }

    fun show_cards_from_collection() {
        /*
        * Mostrando las cartas de la coleccion:
        * 1. Obtenemos la referencia "cards" de la base de datos para trabajar sobre ella
        * 2. Ponemos el estado de la operacion en Loading
        * 3. Limpiamos el array temporal de cartas por si hubiera restos de alguna query anterior
        * 4. Obtenemos cartas de la Realtime Database de Firestore y las añadimos al array temporal
        *       si su id coincide con el almacenado en el array de cartas de la coleccion. Para ello,
        *       por cada carta en la realtime recorremos el array de la coleccion hasta que encuentra
        *       un id que coincida, paramos de buscar y pasamos a la siguiente carta
        * 5. Ponemos el estado de la operacion en completado y le pasamos el array temporal
        * 6. Si da error por algun motivo, ponemos el estado de la operacion en fallido y le pasamos
        *    el mensaje de error
         */
        val collection = collection.value
        val db = FirebaseDatabase.getInstance().getReference("cards")
        db.keepSynced(true)
        state.value = DataState.Loading
        cards.removeAll(cards)
        db.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (i in snapshot.children) {
                    val card = i.getValue(Card::class.java)
                    if (card != null) {
                        collectioniterator@ for (i in collection.cards) {
                            if (card.id == i) {
                                cards.add(card)
                                break@collectioniterator
                            }
                        }
                    }
                }
                state.value = DataState.Success(cards)
            }

            override fun onCancelled(error: DatabaseError) {
                state.value = DataState.Failure(error.message)
            }
        })
    }
}