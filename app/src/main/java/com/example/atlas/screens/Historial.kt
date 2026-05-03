package com.example.atlas.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import com.example.atlas.elements.DefaultTopAppBar
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.atlas.R
import com.example.atlas.elements.DefaultBottomBarDep
import com.example.atlas.navegation.AppScreens

data class Sesion(
    val id: Int,
    val nombre: String,
    val completado: Boolean
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Historial(controller: NavController) {

    val sesiones = listOf(
        Sesion(1, "Press de Banca Plano", true),
        Sesion(2, "Sentadilla con Barra", true),
        Sesion(3, "Peso Muerto Rumano", false),
        Sesion(4, "Press Militar", true),
        Sesion(5, "Curl de Bíceps con Mancuerna", false),
        Sesion(6, "Extensión de Tríceps", true)
    )

    Scaffold(topBar = { DefaultTopAppBar("Mis sesiones") },
        bottomBar = { DefaultBottomBarDep(R.color.white, controller)})
    { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(colorResource(id = R.color.pink))
                .padding(padding)
                .padding(horizontal = 16.dp)
                .padding(top = 16.dp)
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp),
                horizontalArrangement = Arrangement.End
            ) {
                Box(
                    modifier = Modifier
                        .border(
                            1.dp,
                            colorResource(id = R.color.rojoGranada),
                            RoundedCornerShape(18.dp)
                        )
                        .padding(12.dp)
                ) {
                    Text(
                        text = "¡Cada sesión cuenta!\nHaz el recuento de tus sesiones anteriores y visualiza tu progreso",
                        color = colorResource(id = R.color.rojoGranada),
                        fontSize = 13.sp
                    )
                }
            }

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(14.dp),
                contentPadding = PaddingValues(bottom = 80.dp)
            ) {
                items(sesiones) { sesion ->
                    CardSesion(sesion,controller)
                }
            }
        }
    }
}

@Composable
private fun CardSesion(sesion: Sesion, controller: NavController) {

    val estadoColor = if (sesion.completado)
        colorResource(id = R.color.teal_700)
    else
        colorResource(id = R.color.rojoGranada)

    val estadoTexto = if (sesion.completado) "COMPLETADO" else "INCOMPLETO"

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.white)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Sesion ${sesion.id}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = sesion.nombre,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .background(estadoColor)
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(
                    text = estadoTexto,
                    color = Color.White,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Box(modifier = Modifier.width(12.dp))

            IconButton(onClick = {controller.navigate(route = AppScreens.DetalleSesion.name) }) {
                Text(
                    text = ">",
                    color = colorResource(id = R.color.rojoGranada),
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
