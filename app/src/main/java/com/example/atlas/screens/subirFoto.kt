package com.example.atlas.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
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
import com.example.atlas.elements.DefaultBottomBarEnt
import com.example.atlas.elements.DefaultTopAppBar

@Composable
fun subirFoto(controller: NavController) {
    Scaffold(
        topBar = { DefaultTopAppBar("Sesiones programadas") },
        bottomBar = { DefaultBottomBarDep(R.color.rojoGranada, controller) }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            Row(
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth().height(150.dp)
            ) {
                Image(
                    painter = painterResource(R.drawable.puma),
                    contentDescription = "Personaje hablando",
                    contentScale = ContentScale.FillHeight,
                    modifier = Modifier.fillMaxHeight()
                )
                Box(contentAlignment = Alignment.Center) {
                    Image(
                        painter = painterResource(R.drawable.burbujadialogo),
                        contentDescription = "Dialogo",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.FillBounds,
                    )
                    Text(
                        "Mira un resumen de tu sesión ",
                        color = colorResource(R.color.rojoGranada),
                        fontSize = (18.sp),
                        modifier = Modifier.padding(25.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }
            Row(modifier = Modifier.height(250.dp)) {
                ElevatedCard(modifier = Modifier.weight(1f).padding(10.dp).fillMaxHeight()) {
                    Column(
                        modifier = Modifier.padding(15.dp).fillMaxWidth(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            "Trote",
                            fontSize = 30.sp,
                            fontWeight = FontWeight.Bold,
                            color = colorResource(R.color.rojoGranada)
                        )
                        Text(
                            "Tiempo",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = colorResource(R.color.black)
                        )
                        Text(
                            "00:00 min",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = colorResource(R.color.rojoGranada)
                        )
                        Text(
                            "Pasos totales",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = colorResource(R.color.black)
                        )
                        Text(
                            "0 pasos",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = colorResource(R.color.rojoGranada)
                        )
                        Text(
                            "Velocidad ",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = colorResource(R.color.black)
                        )
                        Text(
                            "promedio",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = colorResource(R.color.black)
                        )
                        Text(
                            "0.0 km/h",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = colorResource(R.color.rojoGranada)
                        )
                    }
                }
                ElevatedCard(modifier = Modifier.weight(1f).padding(10.dp).fillMaxHeight()) {
                    Column(
                        modifier = Modifier.padding(15.dp).fillMaxWidth(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            "Sesión",
                            fontSize = 30.sp,
                            fontWeight = FontWeight.Bold,
                            color = colorResource(R.color.rojoGranada)
                        )
                        Text(
                            "# Ejercicios",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = colorResource(R.color.black)
                        )
                        Text(
                            "x",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = colorResource(R.color.rojoGranada)
                        )
                        Text(
                            "Calorias",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = colorResource(R.color.black)
                        )
                        Text(
                            "Quemadas",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = colorResource(R.color.black)
                        )
                        Text(
                            "0 Kcal",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = colorResource(R.color.rojoGranada)
                        )
                    }
                }
            }
            ElevatedCard(modifier = Modifier.padding(15.dp).fillMaxWidth()) {
                Column(
                    modifier = Modifier.fillMaxWidth().clickable {},
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "Captura aqui tu progreso",
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Bold,
                        color = colorResource(R.color.rojoGranada),
                        modifier = Modifier.align(alignment = Alignment.CenterHorizontally)
                    )
                    Icon(
                        painterResource(R.drawable.camara),
                        contentDescription = "Camara",
                        modifier = Modifier.size(80.dp)
                            .align(alignment = Alignment.CenterHorizontally)
                    )
                }
            }
            Column(modifier=Modifier.fillMaxWidth(),verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                DefaulButton("Terminar sesion", 220, 40) { Log.i("Camara", "Abriendo camara") }
            }
        }
    }
}

