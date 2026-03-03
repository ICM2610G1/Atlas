package com.example.atlas.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.material3.TopAppBarColors
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.atlas.R
import com.example.atlas.elements.DefaultBottomBarDep

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Perfil (controller : NavController) {
    var usuario by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var peso by remember { mutableStateOf("") }
    var estatura by remember { mutableStateOf("") }
    var obMedicas by remember { mutableStateOf("") }

    Scaffold(
        bottomBar = { (DefaultBottomBarDep(R.color.pink, controller)) },
        topBar = {
            TopAppBar(
                title = {
                    Box(
                        modifier = Modifier.fillMaxHeight(),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Text(
                            "Perfil del deportista",
                            color = Color.White,
                            fontWeight = FontWeight.Black
                        )
                    }
                },
                actions = {
                    Icon(
                        painter = painterResource(R.drawable.exit_to_app),
                        contentDescription = "Cerrar sesión",
                        tint = Color.White,
                        modifier = Modifier.padding(end = 40.dp).padding(top = 38.dp).size(30.dp)
                            .clickable { controller.navigate("Appstart") }
                    )
                },

                colors = TopAppBarColors(
                    containerColor = colorResource(R.color.rojoGranada),
                    scrolledContainerColor = Color.White,
                    navigationIconContentColor = Color.White,
                    titleContentColor = Color.White,
                    actionIconContentColor = Color.White,
                ),

                modifier = Modifier.height(100.dp)
                    .clip(RoundedCornerShape(bottomStart = 25.dp, bottomEnd = 25.dp))
            )
        }
    )
    { paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues).fillMaxSize().padding(20.dp),
        ) {
            Text("Usuario", fontWeight = FontWeight.SemiBold)
            Box {
                TextField(
                    value = usuario, onValueChange = {usuario = it}, placeholder = {Text("Nombre de usuario")},
                    modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = colorResource(R.color.white),
                        focusedContainerColor = colorResource(R.color.white),
                        unfocusedPlaceholderColor = Color.Gray,
                        focusedPlaceholderColor = Color.Gray
                    )
                )

            }

            Text("Teléfono", fontWeight = FontWeight.SemiBold)
            Box {
                TextField(
                    value = telefono, onValueChange = {telefono = it}, placeholder = {Text("+ 57")},
                    modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = colorResource(R.color.white),
                        focusedContainerColor = colorResource(R.color.white),
                        unfocusedPlaceholderColor = Color.Gray,
                        focusedPlaceholderColor = Color.Gray
                    )
                )

            }

            Text("Correo", fontWeight = FontWeight.SemiBold)
            Box {
                TextField(
                    value = correo, onValueChange = {correo = it}, placeholder = {Text("ejemplo@gmail.com")},
                    modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = colorResource(R.color.white),
                        focusedContainerColor = colorResource(R.color.white),
                        unfocusedPlaceholderColor = Color.Gray,
                        focusedPlaceholderColor = Color.Gray
                    )
                )

            }
            Row(modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(60.dp, Alignment.CenterHorizontally)) {
                Text("Peso", fontWeight = FontWeight.SemiBold)
                TextField (
                    value = peso, onValueChange = {peso = it}, placeholder = {Text("aaaa")},
                    modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = colorResource(R.color.white),
                        focusedContainerColor = colorResource(R.color.white),
                        unfocusedPlaceholderColor = Color.Gray,
                        focusedPlaceholderColor = Color.Gray
                    )
                )
            }

            Row(modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(40.dp, Alignment.CenterHorizontally)) {
                Text("Estatura", fontWeight = FontWeight.SemiBold)
                TextField (
                    value = estatura, onValueChange = {estatura = it}, placeholder = {Text("aaaa")},
                    modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = colorResource(R.color.white),
                        focusedContainerColor = colorResource(R.color.white),
                        unfocusedPlaceholderColor = Color.Gray,
                        focusedPlaceholderColor = Color.Gray
                    )
                )
            }

            Text("Observaciones médicas", fontWeight = FontWeight.SemiBold)
            Box {
                TextField(
                    value = obMedicas, onValueChange = {obMedicas = it}, placeholder = {Text("Agregue observaciones médicas a tener en cuenta")},
                    modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = colorResource(R.color.white),
                        focusedContainerColor = colorResource(R.color.white),
                        unfocusedPlaceholderColor = Color.Gray,
                        focusedPlaceholderColor = Color.Gray
                    )
                )

            }
        }
    }
}

@Preview (showBackground = true)
@Composable
fun PreviewPerfil (){
    val nc = rememberNavController()
    Perfil(nc)
}
