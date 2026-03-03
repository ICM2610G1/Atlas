package com.example.atlas.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.atlas.R
import com.example.atlas.elements.DefaulButton
import com.example.atlas.navegation.AppScreens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogIn(controller: NavController){
    var input by remember { mutableStateOf("") }
    var inputpass by remember { mutableStateOf("") }
    var rememberPassword by remember { mutableStateOf(false) }


    Box(modifier = Modifier.fillMaxSize()){
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Iniciar Sesión",color = colorResource(R.color.white), fontWeight = FontWeight.Black)},
                    colors = TopAppBarDefaults.topAppBarColors(colorResource(R.color.rojoGranada)),
                    modifier = Modifier.padding(vertical = 40.dp).padding(horizontal = 10.dp))},
            containerColor = colorResource(R.color.rojoGranada)
        ) { paddingValues ->
            Column(modifier = Modifier.fillMaxSize().padding(paddingValues).fillMaxHeight(0.989f)
                .clip(RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp))
                .background(colorResource(R.color.pink)).padding(vertical = 40.dp).padding(horizontal = 25.dp),
                verticalArrangement = Arrangement.Top,
            ) {
                Text("Usuario",
                    fontWeight = FontWeight.Medium,
                    fontSize = 19.sp)

                TextField(
                    value = input, onValueChange = {input = it}, placeholder = {Text("Nombre de usuario")},
                    modifier = Modifier.fillMaxWidth().padding(vertical = 5.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = colorResource(R.color.white),
                        focusedContainerColor = colorResource(R.color.white),
                        unfocusedPlaceholderColor = Color.Gray,
                        focusedPlaceholderColor = Color.Gray
                    )
                )
                Text("Contraseña",
                    fontWeight = FontWeight.Medium,
                    fontSize = 19.sp,
                    modifier = Modifier.padding(vertical = 10.dp).fillMaxWidth())
                TextField(
                    trailingIcon = {Icon(painter = painterResource(R.drawable.icon_ojo), contentDescription = "Ojo",tint = colorResource(R.color.rojoGranada))},
                    value = inputpass, onValueChange = {inputpass = it}, placeholder = {Text("Al menos 8 caracteres")},
                    modifier = Modifier.padding(vertical = 6.dp).fillMaxWidth(), shape = RoundedCornerShape(14.dp),
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = colorResource(R.color.white),
                        focusedContainerColor = colorResource(R.color.white),
                        unfocusedPlaceholderColor = Color.Gray,
                        focusedPlaceholderColor = Color.Gray
                    )
                )
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp, horizontal = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically
                ) {

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(
                                if (rememberPassword)
                                    R.drawable.ic_check_box
                                else
                                    R.drawable.ic_check_box_outline
                            ),
                            contentDescription = "Recordar contraseña",
                            tint = Color.Unspecified,
                            modifier = Modifier.size(15.dp).clickable { rememberPassword = !rememberPassword }.padding(horizontal = 1.dp)
                        )

                        Text("Recordar contraseña", modifier = Modifier.padding(horizontal = 2.dp))
                    }

                    Text(
                        "Restablecer contraseña?",
                        color = colorResource(R.color.rojoGranada),
                        modifier = Modifier.clickable {
                            controller.navigate(route = AppScreens.RecoverPassword.name)
                        }
                    )
                }
                DefaulButton("Iniciar", 380, 40) {
                    Log.i("TAGIniciarSesion", "Click IniciarSesion")
                    if (input.lowercase() == "entrenador") {
                        controller.navigate(route = AppScreens.HomeCoach.name)
                    } else {
                        controller.navigate(route = AppScreens.Home.name)
                    }
                }
            }
        }
        Image(
            painter = painterResource(id = R.drawable.panter_feliz),
            contentDescription = "Pantera iniciar sesión",
            modifier = Modifier.size(165.dp).align(Alignment.TopEnd).padding(top = 25.dp, end = 45.dp)
        )
    }
}

@Preview (showBackground = true)
@Composable
fun PreviewLogIn (){
    val nc = rememberNavController()
    LogIn(nc)
}



