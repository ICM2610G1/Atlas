package com.example.atlas.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.atlas.R
import com.example.atlas.elements.DefaulButton
import com.example.atlas.elements.DefaultBottomBarDep
import com.example.atlas.elements.DefaultTopAppBar
import com.example.atlas.navegation.AppScreens

@Composable
fun ResumenTrote(controller: NavController) {
    val context = LocalContext.current
    val historialPresion = listOf(10f, 30f, 25f, 50f, 70f, 60f, 90f, 40f, 30f, 55f)
    val rojoGranada = colorResource(R.color.rojoGranada)

    Scaffold(
        topBar = { DefaultTopAppBar("Resumen trote") },
        bottomBar = { DefaultBottomBarDep(R.color.rojoGranada, controller) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(bottom = 20.dp)
        ) {
            // --- Bloque del Puma y Diálogo ---
            Row(
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp) // Un poco más pequeño para dar espacio
            ) {
                Image(
                    painter = painterResource(R.drawable.puma),
                    contentDescription = "Personaje hablando",
                    contentScale = ContentScale.FillHeight,
                    modifier = Modifier.fillMaxHeight()
                )
                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                    Image(
                        painter = painterResource(R.drawable.burbujadialogo),
                        contentDescription = "Dialogo",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.FillBounds,
                    )
                    Text(
                        "Mira el resumen de datos\nde tus sensores",
                        color = rojoGranada,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 30.dp, vertical = 20.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }

            Row(
                modifier = Modifier
                    .height(280.dp)
                    .padding(horizontal = 10.dp)
            ) {
                ElevatedCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp)
                        .fillMaxHeight()
                ) {
                    Column(
                        modifier = Modifier
                            .padding(10.dp)
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Text(
                            "Movimiento",
                            fontSize = 23.sp,
                            fontWeight = FontWeight.Black,
                            color = rojoGranada
                        )

                        DatoResumen("Tiempo", "00:00 min")
                        DatoResumen("Distancia", "0 m")
                        DatoResumen("Velocidad", "0.0 km/h")
                    }
                }
            }
            Text(
                "Altitud durante el entrenamiento",
                modifier = Modifier.padding(start = 20.dp, top = 20.dp, bottom = 10.dp),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Gray
            )

            ElevatedCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(horizontal = 15.dp),
                colors = CardDefaults.elevatedCardColors(containerColor = Color.White)
            ) {
                Canvas(modifier = Modifier.fillMaxSize().padding(20.dp)) {
                    val spacing = size.width / historialPresion.size
                    val barWidth = spacing * 0.6f
                    val maxVal = historialPresion.maxOrNull() ?: 1f

                    historialPresion.forEachIndexed { index, value ->
                        val barHeight = (value / maxVal) * size.height
                        drawRoundRect(
                            color = rojoGranada,
                            topLeft = Offset(index * spacing, size.height - barHeight),
                            size = Size(barWidth, barHeight),
                            cornerRadius = CornerRadius(4.dp.toPx())
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(30.dp))
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                DefaulButton("Detalle pesas", 220, 40) {
                    controller.navigate(route = AppScreens.ResumenSesion.name)
                }
            }
        }
    }
}

// Función auxiliar para no repetir tanto código de texto
@Composable
fun DatoResumen(label: String, value: String) {
    Column(modifier= Modifier.padding(5.dp),
        horizontalAlignment = Alignment.CenterHorizontally) {
        Text(label, fontSize = 23.sp, fontWeight = FontWeight.Bold, color = Color.Black)
        Text(value, fontSize = 25.sp, fontWeight = FontWeight.Bold, color = colorResource(R.color.rojoGranada))
    }
}