package com.example.atlas.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.atlas.R
import com.example.atlas.elements.DefaulButton
import com.example.atlas.elements.DefaultBottomBarDep
import com.example.atlas.elements.DefaultBottomBarEnt
import com.example.atlas.elements.DefaultTopAppBar
import com.example.atlas.navegation.AppScreens
import com.example.atlas.viewmodels.EjercicioViewModel
import com.example.atlas.viewmodels.ModeloMiUbicacion

@Composable
fun ResumenSesion(controller: NavController, model1: EjercicioViewModel= viewModel(), model2: ModeloMiUbicacion=viewModel()) {
    val rojoGranada = colorResource(R.color.rojoGranada)
    val stateEj=model1.estado.collectAsState()
    val stateUbi=model2.estado.collectAsState()

    Scaffold(
        topBar = { DefaultTopAppBar("Resumen sesión") },
        bottomBar = { DefaultBottomBarDep(R.color.rojoGranada, controller) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(bottom = 20.dp)
        ) {
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
                        "Mira el resumen de datos\n",
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
                            "Sesión",
                            fontSize = 23.sp,
                            fontWeight = FontWeight.Black,
                            color = rojoGranada
                        )
                        val tiempoHoras = stateUbi.value.tiempoSegundos / 3600f
                        val temperatura = stateUbi.value.temperaturaActual

                        val factorDeTemperatura = when {
                            temperatura < 10f -> 1.15f
                            temperatura < 18f -> 1.05f
                            temperatura in 18f..24f -> 1.0f
                            temperatura < 32f -> 1.08f
                            else -> 1.12f
                        }

                        val calorias = (8f * 70f * tiempoHoras * factorDeTemperatura).toInt()

                        DatoResumenSesion("# Ejercicios", "${stateEj.value.lista.size}")
                        DatoResumenSesion("Calorías", "${calorias} Kcal")
                    }
                }
            }

            Spacer(modifier = Modifier.height(30.dp))
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                DefaulButton("Volver al home", 220, 40) {
                    controller.navigate(route = AppScreens.Home.name)
                }
            }
        }
    }
}

@Composable
fun DatoResumenSesion(label: String, value: String) {
    Column(modifier= Modifier.padding(5.dp),
        horizontalAlignment = Alignment.CenterHorizontally) {
        Text(label, fontSize = 23.sp, fontWeight = FontWeight.Bold, color = Color.Black)
        Text(value, fontSize = 25.sp, fontWeight = FontWeight.Bold, color = colorResource(R.color.rojoGranada))
    }
}