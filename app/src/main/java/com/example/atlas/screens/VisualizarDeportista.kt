package com.example.atlas.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.atlas.R
import com.example.atlas.elements.DefaulButton
import com.example.atlas.elements.DefaultBottomBarEnt
import com.example.atlas.elements.DefaultTopAppBar
import com.example.atlas.navegation.AppScreens
import com.example.atlas.viewmodels.ModeloVisualizarDeportista

@Composable
fun VisualizarDeportista(
    controller: NavController,
    modelo: ModeloVisualizarDeportista = viewModel()
) {
    val estado by modelo.estado.collectAsState()

    Scaffold(
        topBar = { DefaultTopAppBar(nombre = "Deportista") },
        bottomBar = { DefaultBottomBarEnt(colorId = R.color.white, controller = controller) }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.puma),
                    contentDescription = "Mascota Puma",
                    modifier = Modifier.size(180.dp),
                    alignment = Alignment.TopStart
                )
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.burbuja),
                        contentDescription = "DialogoPuma",
                        tint = colorResource(R.color.rojoGranada),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text(
                        text = "Antes de visualizar la\nrutina del deportista,\ndale un vistazo a su\nobjetivo actual",
                        color = colorResource(R.color.rojoGranada),
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Filled.Person,
                        contentDescription = "Foto de perfil",
                        tint = Color.Gray,
                        modifier = Modifier.size(100.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    // nombre viene del estado del ViewModel
                    Text(
                        text = estado.nombre,
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp
                    )
                    Spacer(modifier = Modifier.height(24.dp))

                    AtributoDeportista(titulo = "Edad:", valor = estado.edad)
                    Spacer(modifier = Modifier.height(8.dp))
                    AtributoDeportista(titulo = "Peso:", valor = estado.peso)
                    Spacer(modifier = Modifier.height(8.dp))
                    AtributoDeportista(titulo = "Altura:", valor = estado.altura)
                    Spacer(modifier = Modifier.height(8.dp))
                    AtributoDeportista(titulo = "Recomendaciones médicas:", valor = estado.recomendaciones)
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Text(text = "Objetivo actual", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Text(
                            text = estado.objetivo,
                            color = Color.Gray,
                            fontSize = 16.sp,
                            textAlign = TextAlign.End
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            DefaulButton(text = "Ver rutina", ancho = 300, alto = 50) {
                controller.navigate(route = AppScreens.Ubicacion.name)
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun AtributoDeportista(titulo: String, valor: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = titulo, fontWeight = FontWeight.Bold, fontSize = 16.sp)
        Text(text = valor, color = Color.Gray, fontSize = 16.sp)
    }
}

@Preview(showBackground = true)
@Composable
fun VistaVisualizarDeportista() {
    VisualizarDeportista(rememberNavController())
}