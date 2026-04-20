package com.example.atlas.screens

import android.widget.Toast
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.atlas.R
import com.example.atlas.elements.DefaulButton
import com.example.atlas.elements.DefaultBottomBarDep
import com.example.atlas.elements.DefaultTopAppBar
import com.example.atlas.modelos.ElevationPoint
import com.example.atlas.navegation.AppScreens
import com.example.atlas.viewmodels.ModeloMiUbicacion
import com.github.tehras.charts.bar.BarChart
import com.github.tehras.charts.bar.BarChartData
@Composable
fun BarrasElevacion(puntosElevacion: List<ElevationPoint>) {

    if (puntosElevacion.isEmpty()) {
        Text(
            text = "Sin datos de elevación",
            color = Color.Gray,
            fontSize = 12.sp,
            modifier = Modifier.padding(16.dp)
        )
        return
    }

    val barras = ArrayList<BarChartData.Bar>()

    puntosElevacion.mapIndexed { index, punto ->
        barras.add(
            BarChartData.Bar(
                label = "",
                value = punto.altitud,
                color = colorResource(R.color.rojoGranada)
            )
        )
    }

    BarChart(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .height(150.dp),
        barChartData = BarChartData(bars = barras)
    )
}

@Composable
fun ResumenTrote(controller: NavController, model: ModeloMiUbicacion= viewModel()) {
    val estado by model.estado.collectAsState()
    val context = LocalContext.current
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
                    .height(130.dp)
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
                    .height(200.dp)
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

                        DatoResumen("Tiempo", "${estado.tiempoSegundos} s")
                        DatoResumen("Distancia", "${estado.distanciaRecorrida*1000} m")
                        DatoResumen("Velocidad", "${estado.distanciaRecorrida*1000/estado.tiempoSegundos} m/s")
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
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Perfil de elevación",
                        color = colorResource(R.color.rojoGranada),
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    BarrasElevacion(puntosElevacion = estado.puntosElevacion)
                }
            }


            Spacer(modifier = Modifier.height(30.dp))
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                DefaulButton("Terminar sesión", 220, 40) {
                    controller.navigate(route = AppScreens.ResumenSesion.name)
                    Toast.makeText(context, "Sesión finalizada y registrada", Toast.LENGTH_LONG)
                        .show()
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
