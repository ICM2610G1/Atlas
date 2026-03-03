package com.example.atlas.screens

import android.R.attr.padding
import android.text.Layout
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.atlas.R
import com.example.atlas.elements.DefaultBottomBarDep
import com.example.atlas.elements.DefaultTopAppBar

data class Mensaje(val contenido: String, val enviado: Boolean, val hora: String)
@Composable
fun chatP(contacto: String, controller: NavController) {
    var mensaje by remember { mutableStateOf("") }
    val mensajes = listOf(
        Mensaje("Hola $contacto, ¿Cómo vas?", true, "7:10 am"),
        Mensaje("Hola, Muy Bien y Tu?", false, "7:29 am"),
        Mensaje("Bien gracias. Hoy a que hora nos vemos para trotar", true, "7:29 am"),
        Mensaje("Entreno a las 11?", false, "7:41 am")
    )
    Scaffold(
        topBar = { DefaultTopAppBar(contacto) },
        containerColor = colorResource(R.color.pink),
        bottomBar = { DefaultBottomBarDep(R.color.white, controller) }
    ) { paddingValues ->
        Column(
            modifier = Modifier.fillMaxSize().padding(paddingValues).imePadding()
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxWidth().padding(paddingValues).weight(1f),
            ) {
                items(mensajes) { item ->

                    val alineacion = if (item.enviado) Alignment.End else Alignment.Start
                    val color = if (item.enviado) colorResource(R.color.rojos) else colorResource(R.color.rojoGranada)
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
                        horizontalAlignment = alineacion
                    ) {
                        ElevatedCard(
                            modifier = Modifier.padding(horizontal = 8.dp),
                            colors = CardDefaults.cardColors(color)
                        ) {
                            Text(item.contenido, modifier = Modifier.padding(8.dp), fontSize = 15.sp, color = Color.White
                            )
                        }
                        Text(
                            item.hora,
                            color = colorResource(R.color.black),
                            modifier = Modifier.padding(5.dp).align(alignment = alineacion)
                        )
                    }
                }
            }
            TextField(
                value = mensaje,
                onValueChange = { mensaje = it },
                placeholder = { Text("Mensaje") },
                shape = CircleShape,
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = {
                    Icon(
                        Icons.Default.Face,
                        contentDescription = "Barra de busqueda"
                    )
                },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    unfocusedPlaceholderColor = Color.Black
                ),
                trailingIcon = {
                    IconButton(onClick = {}) {
                        Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Enviar mensaje")
                    }
                }
            )
        }
    }
}

