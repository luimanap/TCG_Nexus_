package com.pixelperfectsoft.tcg_nexus.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.pixelperfectsoft.tcg_nexus.BackgroundImage
import com.pixelperfectsoft.tcg_nexus.MyButton
import com.pixelperfectsoft.tcg_nexus.MyCanvasSeparator
import com.pixelperfectsoft.tcg_nexus.MyLogo
import com.pixelperfectsoft.tcg_nexus.MyPasswordField
import com.pixelperfectsoft.tcg_nexus.MyTextField
import com.pixelperfectsoft.tcg_nexus.navigation.MyAppRoute

@Composable
fun RegisterScreen(
    navController: NavController,
    viewModel: LoginScreenViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    var userinput by rememberSaveable { mutableStateOf("") }
    var emailinput by rememberSaveable { mutableStateOf("") }
    var passinput by rememberSaveable { mutableStateOf("") }
    var confpassinput by rememberSaveable { mutableStateOf("") }
    BackgroundImage()
    Column(
        Modifier
            .fillMaxSize()
            .background(color = Color(204, 204, 204, 90))
            .verticalScroll(rememberScrollState())
    ) {

        //Logo Image
        Spacer(Modifier.size(5.dp))
        MyLogo(90)

        //Name Input
        MyTextField(data = userinput, label = "Usuario", onvaluechange = { userinput = it })
        Spacer(Modifier.size(16.dp))


        //Email Input
        MyTextField(
            data = emailinput,
            label = "Correo Electrónico",
            onvaluechange = { emailinput = it })
        Spacer(Modifier.size(16.dp))

        //Password Input
        MyPasswordField(data = passinput, label = "Contraseña", onvaluechange = { passinput = it })
        Spacer(Modifier.size(16.dp))

        //Confirm Password Input
        MyPasswordField(
            data = confpassinput,
            label = "Confirmar Contraseña",
            onvaluechange = { confpassinput = it })
        Spacer(Modifier.size(15.dp))

        //Checkboxes
        val termschecked = MyTextCheckBox("Acepto los términos y condiciones")
        val policychecked = MyTextCheckBox("Acepto la política de privacidad")
        MyTextCheckBox("Me gustaría recibir ofertas y promociones exclusivas")

        //Register button
        MyButton(
            text = "Crear Cuenta",
            onclick = {
                if (passinput == confpassinput && termschecked && policychecked) {
                    viewModel.createUserAccount(userinput, emailinput, passinput) {
                        navController.navigate(MyAppRoute.PROFILE)
                    }
                }
            },
            containercolor = Color(92, 115, 255),
            bordercolor = Color(92, 115, 255),
            textcolor = Color.White
        )

        //Separator
        Spacer(Modifier.size(8.dp))
        MyCanvasSeparator()
        Spacer(Modifier.size(8.dp))

        //Login Button
        MyButton(
            text = "Iniciar Sesion",
            onclick = { navController.popBackStack() },
            containercolor = Color(255, 178, 92),
            bordercolor = Color(255, 178, 92),
            textcolor = Color.White
        )
    }
}

@Composable
fun MyTextCheckBox(text: String): Boolean {
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
    return state
}
