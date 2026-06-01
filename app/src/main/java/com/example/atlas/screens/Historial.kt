package com.example.atlas.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.example.atlas.viewmodels.ModeloHistorial
import com.example.atlas.viewmodels.SesionConId

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Historial(
    controller: NavController,
    modelo: ModeloHistorial = viewModel()
) {
    val sesiones by modelo.sesiones.collectAsState()

    Scaffold(
        topBar = { DefaultTopAppBar("Mis sesiones") },
        bottomBar = { DefaultBottomBarDep(R.color.white, controller) }
    ) { padding ->
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

            if (sesiones.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = "Aún no tienes sesiones registradas", color = Color.Gray)
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(14.dp),
                    contentPadding = PaddingValues(bottom = 80.dp)
                ) {
                    items(sesiones) { sesionConId ->
                        CardSesion(sesionConId, controller)
                    }
                }
            }
        }
    }
}

@Composable
private fun CardSesion(sesionConId: SesionConId, controller: NavController) {
    val sesion = sesionConId.sesion
    val id = sesionConId.id

    val estadoColor = if (sesion.completado)
        colorResource(id = R.color.teal_700)
    else
        colorResource(id = R.color.rojoGranada)

    val estadoTexto = if (sesion.completado) "COMPLETADO" else "INCOMPLETO"

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.white)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Sesion",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = if (sesion.fecha.isNotEmpty()) sesion.fecha else "Sin fecha",
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

            IconButton(onClick = { 
                controller.navigate(route = "${AppScreens.DetalleSesion.name}/$id") 
            }) {
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
