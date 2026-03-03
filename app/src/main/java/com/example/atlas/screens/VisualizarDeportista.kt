package com.example.atlas.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.atlas.R
import com.example.atlas.elements.DefaulButton
import com.example.atlas.elements.DefaultBottomBarEnt
import com.example.atlas.elements.DefaultTopAppBar
import com.example.atlas.navegation.AppScreens

@Composable
fun VisualizarDeportista(controller: NavController) {
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
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Icon(
                        imageVector = Icons.Filled.Person,
                        contentDescription = "Foto de perfil",
                        tint = Color.Gray,
                        modifier = Modifier.size(100.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Andres Carvajal",
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    AtributoDeportista(titulo = "Edad:", valor = "20 años")
                    Spacer(modifier = Modifier.height(8.dp))

                    AtributoDeportista(titulo = "Peso:", valor = "75 kg")
                    Spacer(modifier = Modifier.height(8.dp))

                    AtributoDeportista(titulo = "Altura:", valor = "1.75 cm")
                    Spacer(modifier = Modifier.height(8.dp))

                    AtributoDeportista(titulo = "Recomendaciones médicas:", valor = "N.A")
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Text(text = "Objetivo actual", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Text(
                            text = "Aumentar masa\nmuscular en un\n50%",
                            color = Color.Gray,
                            fontSize = 16.sp,
                            textAlign = TextAlign.End
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            DefaulButton(text = "Ver rutina", ancho = 300, alto = 50) {
                controller.navigate(route= AppScreens.Ubicacion.name)
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
fun PreviewVisualizarDeportista() {
    VisualizarDeportista(rememberNavController())
}
