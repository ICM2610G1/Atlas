package com.example.atlas.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Face
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.atlas.R
import com.example.atlas.auth
import com.example.atlas.elements.DefaultBottomBarDep
import com.example.atlas.elements.DefaultTopAppBar
import com.example.atlas.viewmodels.ModeloEnviarMensaje
import com.example.atlas.viewmodels.ModeloMensajesChat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun chatP(
    idChat: String,
    contacto: String,
    controller: NavController,
    modeloMensajesChat: ModeloMensajesChat = viewModel(),
    modeloEnviarMensaje: ModeloEnviarMensaje = viewModel()
) {
    var mensaje by remember { mutableStateOf("") }

    val mensajes by modeloMensajesChat.mensajes.collectAsState()

    val idUsuarioActual = auth.currentUser?.uid ?: ""

    LaunchedEffect(idChat) {
        modeloMensajesChat.cargarMensajes(idChat)
    }

    Scaffold(
        topBar = {
            DefaultTopAppBar(contacto)
        },
        containerColor = colorResource(R.color.pink),
        bottomBar = {
            DefaultBottomBarDep(R.color.white, controller)
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .imePadding()
        ) {

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 8.dp)
            ) {
                items(mensajes) { item ->

                    val mensajeEsMio = item.idEmisor == idUsuarioActual

                    val alineacion = if (mensajeEsMio) {
                        Alignment.End
                    } else {
                        Alignment.Start
                    }

                    val colorMensaje = if (mensajeEsMio) {
                        colorResource(R.color.rojos)
                    } else {
                        colorResource(R.color.rojoGranada)
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        horizontalAlignment = alineacion
                    ) {
                        ElevatedCard(
                            colors = CardDefaults.cardColors(
                                containerColor = colorMensaje
                            )
                        ) {
                            Text(
                                text = item.texto,
                                modifier = Modifier.padding(10.dp),
                                fontSize = 15.sp,
                                color = Color.White
                            )
                        }

                        Text(
                            text = formatearHora(item.timestamp),
                            color = colorResource(R.color.black),
                            modifier = Modifier.padding(5.dp),
                            fontSize = 11.sp
                        )
                    }
                }
            }

            TextField(
                value = mensaje,
                onValueChange = { mensaje = it },
                placeholder = {
                    Text("Mensaje")
                },
                shape = CircleShape,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                leadingIcon = {
                    Icon(
                        Icons.Default.Face,
                        contentDescription = "Mensaje"
                    )
                },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    unfocusedPlaceholderColor = Color.Black
                ),
                trailingIcon = {
                    IconButton(
                        onClick = {
                            modeloEnviarMensaje.enviarMensaje(
                                idChat = idChat,
                                texto = mensaje
                            )

                            mensaje = ""
                        }
                    ) {
                        Icon(
                            Icons.AutoMirrored.Filled.Send,
                            contentDescription = "Enviar mensaje"
                        )
                    }
                }
            )
        }
    }
}

private fun formatearHora(timestamp: Long): String {
    if (timestamp == 0L) {
        return ""
    }

    val formato = SimpleDateFormat("h:mm a", Locale.getDefault())
    return formato.format(Date(timestamp))
}