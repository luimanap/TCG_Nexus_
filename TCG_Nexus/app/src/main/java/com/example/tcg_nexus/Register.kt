package com.example.tcg_nexus

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyRegisterScreen(navController: NavController) {
    var nameinput by rememberSaveable { mutableStateOf("") }
    var lastnameinput by rememberSaveable { mutableStateOf("") }
    var emailinput by rememberSaveable { mutableStateOf("") }
    var passinput by rememberSaveable { mutableStateOf("") }
    var confpassinput by rememberSaveable { mutableStateOf("") }
    BackgroundImage()
    Column(
        Modifier
            .fillMaxSize()
            .background(color = Color(204, 204, 204, 90))
            .clip(RoundedCornerShape(40.dp))
    ) {

        //Logo Image
        Spacer(Modifier.size(5.dp))
        MyLogo(150)

        //Name Input
        //MyTextField("Nombre", nameinput){nameinput = it}
        TextField(value = nameinput, onValueChange = { nameinput = it }, label = {
            Text(text = "Nombre")
        }, modifier = Modifier
            .fillMaxWidth()
            .padding(start = 30.dp, end = 30.dp)
            .background(Color.White)
        )
        Spacer(Modifier.size(16.dp))

        //Last Name Input
        TextField(value = lastnameinput, onValueChange = { lastnameinput = it }, label = {
            Text(text = "Apellidos")
        }, modifier = Modifier
            .fillMaxWidth()
            .padding(start = 30.dp, end = 30.dp)
            .align(Alignment.CenterHorizontally)
            .background(Color.White)
        )
        Spacer(Modifier.size(16.dp))

        //Email Input
        TextField(value = emailinput, onValueChange = { emailinput = it }, label = {
            Text(text = "Correo Electrónico")
        }, modifier = Modifier
            .fillMaxWidth()
            .padding(start = 30.dp, end = 30.dp)
            .align(Alignment.CenterHorizontally)
            .background(Color.White)
        )
        Spacer(Modifier.size(16.dp))

        //Password Input
        TextField(value = passinput, onValueChange = { passinput = it }, label = {
            Text(text = "Contraseña")
        }, modifier = Modifier
            .fillMaxWidth()
            .padding(start = 30.dp, end = 30.dp)
            .align(Alignment.CenterHorizontally)
            .background(Color.White)
        )
        Spacer(Modifier.size(16.dp))

        //Confirm Password Input
        TextField(value = confpassinput, onValueChange = { confpassinput = it }, label = {
            Text(text = "Confirmar Contraseña")
        }, modifier = Modifier
            .fillMaxWidth()
            .padding(start = 30.dp, end = 30.dp)
            .align(Alignment.CenterHorizontally)
            .background(Color.White)
        )
        Spacer(Modifier.size(15.dp))

        //Checkboxes
        MyTextCheckBox("Acepto los términos y condiciones")
        MyTextCheckBox("Acepto la política de privacidad")
        MyTextCheckBox("Me gustaría recibir ofertas y promociones exclusivas")

        //Register button
        MyButton(text = "Crear Cuenta", onclick = {navController.navigate("home-screen")},containercolor = Color(92, 115, 255))

        //Separator
        Spacer(Modifier.size(8.dp))
        MyCanvasSeparator()
        Spacer(Modifier.size(8.dp))
        
        //Login Button
        MyButton(text = "Iniciar Sesion", onclick = { navController.popBackStack() }, containercolor = Color(255, 178, 92))
    }
}

/*@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTextField(text: String, value: String, valuechange: () -> Unit) {
    TextField(value = value, onValueChange = valuechange, label = {
        Text(text = text)
    }, modifier = Modifier
        .fillMaxWidth()
        .padding(start = 30.dp, end = 30.dp)
        .background(Color.White)
    )
}*/

@Composable
fun MyTextCheckBox(text: String) {
    var state by rememberSaveable { mutableStateOf(false) }
    Row(Modifier.padding(start = 30.dp, end = 30.dp)) {
        Checkbox(
            checked = state, onCheckedChange = { state = !state }, colors = CheckboxDefaults.colors(
                uncheckedColor = Color.Black,
                checkedColor = Color.Blue,
                checkmarkColor = Color.White
            )
        )
        Spacer(Modifier.width(8.dp))
        Text(
            text = text,
            style = TextStyle(color = Color.Black),
            modifier = Modifier.align(Alignment.CenterVertically)
        )
    }
}
