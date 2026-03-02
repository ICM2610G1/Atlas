package com.example.atlas.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.atlas.R
import com.example.atlas.elements.DefaultBottomBarDep
import com.example.atlas.elements.DefaultTopAppBar
import com.example.atlas.navegation.AppScreens
import kotlin.collections.getValue

data class vistaMensaje(val nombre: String, val ultimoMensaje: String, val mensajesSinLeer: Int, val tiempo: Int)

@Composable
fun Chats(controller: NavController) {
        var busqueda by remember{ mutableStateOf("") }
        var chats: List<vistaMensaje> = listOf<vistaMensaje>(
            vistaMensaje("Alexander Caneva", "Entreno a las 11?",1, 10),
            vistaMensaje("Yesid Lemus", "Voy a entrenar despues de clase",3, 50),
            vistaMensaje("Entrenador Martín", "Te hice ciertos cambios en la rutina de mañana",1,90),
            vistaMensaje("Alex Hunter", "Entreno a las 11?",1, 10),
            vistaMensaje("Ansu Fati", "Entreno a las 11?",1, 10),
            vistaMensaje("Raphael Bellolli Dias", "Entreno a las 11?",1, 10))


        Scaffold(
            topBar = { DefaultTopAppBar("Chats") },
            bottomBar = {DefaultBottomBarDep(R.color.rojoGranada,controller)},
            containerColor = colorResource(R.color.rojop)
        ) { paddingValues ->
            Column(
                modifier = Modifier.padding(paddingValues).fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TextField(
                    value = busqueda,
                    onValueChange = { busqueda = it },
                    placeholder = { Text("Buscar") },
                    leadingIcon = {
                        Icon(
                            Icons.Default.Search,
                            contentDescription = "Barra de busqueda"
                        )
                    },
                    modifier = Modifier.padding(30.dp).align(Alignment.Start),
                    shape = CircleShape,
                    colors= TextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor=Color.White,
                        focusedIndicatorColor = colorResource(R.color.rojoGranada),
                        unfocusedPlaceholderColor = Color.Black
                    )
                )
                LazyColumn(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(chats) { item ->
                        ElevatedCard(
                            modifier = Modifier.padding(
                                top = 5.dp,
                                bottom = 5.dp
                            )
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.Start),
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth().clickable {
                                    controller.navigate(route= AppScreens.chatsP.name + "/${item.nombre}")
                                }.padding(15.dp)
                            ) {
                                Icon(Icons.Default.AccountCircle, "Simbolo de persona", modifier=Modifier.size(50.dp))
                                Column(modifier= Modifier.weight(1f),
                                    verticalArrangement = Arrangement.spacedBy(5.dp, Alignment.CenterVertically),
                                    horizontalAlignment = Alignment.Start) {
                                    Text(item.nombre, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                                    Text(item.ultimoMensaje, fontSize = 12.sp)
                                }
                                Column(horizontalAlignment = Alignment.End) {
                                    Text("${item.tiempo} min")
                                    Box(
                                        modifier = Modifier.size(20.dp).background(colorResource(id = R.color.rojoGranada)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text("${item.mensajesSinLeer}", color=Color.White)
                                    }
                                }
                            }
                        }
                    }
                }
            }

        }
    }
