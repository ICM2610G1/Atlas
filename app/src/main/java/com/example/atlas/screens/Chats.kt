package com.example.atlas.screens

import android.net.Uri
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
import androidx.compose.runtime.collectAsState
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.atlas.R
import com.example.atlas.elements.DefaultBottomBarDep
import com.example.atlas.elements.DefaultTopAppBar
import com.example.atlas.navegation.AppScreens
import com.example.atlas.objetosDB.ChatDB
import com.example.atlas.objetosDB.UsuariosGen
import com.example.atlas.viewmodels.ModeloChatsUsuario
import com.example.atlas.viewmodels.ModeloCrearChat
import com.example.atlas.viewmodels.ModeloUsuariosChat
@Composable
fun Chats(controller: NavController, modeloUsuariosChat: ModeloUsuariosChat = viewModel(), modeloChatsUsuario: ModeloChatsUsuario = viewModel(), modeloCrearChat: ModeloCrearChat = viewModel()
) {
    var busqueda by remember { mutableStateOf("") }

    val usuarios by modeloUsuariosChat.usuarios.collectAsState()
    val chats by modeloChatsUsuario.chats.collectAsState()

    val chatsFiltrados = chats.filter {
        it.nombreOtroUsuario.contains(busqueda, ignoreCase = true)
    }

    val usuariosFiltrados = usuarios.filter {
        it.nombre.contains(busqueda, ignoreCase = true) ||
                it.rol.contains(busqueda, ignoreCase = true)
    }

    Scaffold(
        topBar = { DefaultTopAppBar("Chats") },
        bottomBar = { DefaultBottomBarDep(R.color.white, controller) },
        containerColor = colorResource(R.color.pink)
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
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
                modifier = Modifier
                    .padding(30.dp)
                    .align(Alignment.Start),
                shape = CircleShape,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedIndicatorColor = colorResource(R.color.rojoGranada),
                    unfocusedPlaceholderColor = Color.Black
                )
            )

            LazyColumn(
                modifier = Modifier.fillMaxWidth()
            ) {

                item {
                    Text(
                        text = "Conversaciones",
                        modifier = Modifier.padding(
                            start = 20.dp,
                            top = 5.dp,
                            bottom = 5.dp
                        ),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                if (chatsFiltrados.isEmpty()) {
                    item {
                        Text(
                            text = "Todavía no tienes conversaciones.",
                            modifier = Modifier.padding(start = 20.dp, bottom = 10.dp),
                            fontSize = 13.sp
                        )
                    }
                }

                items(chatsFiltrados) { item ->
                    ElevatedCard(
                        modifier = Modifier.padding(
                            top = 5.dp,
                            bottom = 5.dp
                        )
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(
                                10.dp,
                                Alignment.Start
                            ),
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    val nombreSeguro = Uri.encode(item.nombreOtroUsuario)

                                    controller.navigate(
                                        route = AppScreens.chatsP.name +
                                                "/${item.idChat}/$nombreSeguro"
                                    )
                                }
                                .padding(15.dp)
                        ) {
                            Icon(
                                Icons.Default.AccountCircle,
                                contentDescription = "Simbolo de persona",
                                modifier = Modifier.size(50.dp)
                            )

                            Column(
                                modifier = Modifier.weight(1f),
                                verticalArrangement = Arrangement.spacedBy(
                                    5.dp,
                                    Alignment.CenterVertically
                                ),
                                horizontalAlignment = Alignment.Start
                            ) {
                                Text(
                                    text = item.nombreOtroUsuario,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold
                                )

                                Text(
                                    text = if (item.ultimoMensaje.isBlank()) {
                                        "Chat creado"
                                    } else {
                                        item.ultimoMensaje
                                    },
                                    fontSize = 12.sp
                                )
                            }

                            Column(horizontalAlignment = Alignment.End) {
                                Text("Ahora")

                                Box(
                                    modifier = Modifier
                                        .size(20.dp)
                                        .background(
                                            colorResource(id = R.color.rojoGranada)
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "",
                                        color = Color.White
                                    )
                                }
                            }
                        }
                    }
                }

                item {
                    Text(
                        text = "Usuarios",
                        modifier = Modifier.padding(
                            start = 20.dp,
                            top = 15.dp,
                            bottom = 5.dp
                        ),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                if (usuariosFiltrados.isEmpty()) {
                    item {
                        Text(
                            text = "No hay usuarios disponibles.",
                            modifier = Modifier.padding(start = 20.dp, bottom = 10.dp),
                            fontSize = 13.sp
                        )
                    }
                }

                items(usuariosFiltrados) { usuario ->
                    ElevatedCard(
                        modifier = Modifier.padding(
                            top = 5.dp,
                            bottom = 5.dp
                        )
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(
                                10.dp,
                                Alignment.Start
                            ),
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    modeloCrearChat.crearChat(usuario) { idChat, nombre ->
                                        val nombreSeguro = Uri.encode(nombre)

                                        controller.navigate(
                                            route = AppScreens.chatsP.name +
                                                    "/$idChat/$nombreSeguro"
                                        )
                                    }
                                }
                                .padding(15.dp)
                        ) {
                            Icon(
                                Icons.Default.AccountCircle,
                                contentDescription = "Simbolo de persona",
                                modifier = Modifier.size(50.dp)
                            )

                            Column(
                                modifier = Modifier.weight(1f),
                                verticalArrangement = Arrangement.spacedBy(
                                    5.dp,
                                    Alignment.CenterVertically
                                ),
                                horizontalAlignment = Alignment.Start
                            ) {
                                Text(
                                    text = usuario.nombre,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold
                                )

                                Text(
                                    text = usuario.rol,
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}