package com.example.atlas.screens

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.atlas.R
import com.example.atlas.elements.DefaulButton
import com.example.atlas.elements.DefaultBottomBarDep
import com.example.atlas.elements.DefaultTopAppBar
import com.example.atlas.navegation.AppScreens


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CrearNuevaSesion(controller: NavController) {
    var troteSeleccionado by remember { mutableStateOf(false) }
    var gymSeleccionado by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = colorResource(R.color.pink),
        topBar = { DefaultTopAppBar("Crear nueva sesión") },
        bottomBar = {
            DefaultBottomBarDep(R.color.white, controller)
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 18.dp)
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth().height(180.dp).padding(15.dp)
            ) {

                Box(
                    modifier = Modifier.weight(1f).fillMaxHeight()
                ) {
                    Image(
                        painter = painterResource(R.drawable.burbujadialogo),
                        contentDescription = "Dialogo",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.FillBounds
                    )

                    Text(
                        text = "Programa tu sesion",
                        color = colorResource(R.color.rojoGranada),
                        fontSize = 18.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(horizontal = 24.dp, vertical = 40.dp)
                    )
                }

                Image(
                    painter = painterResource(R.drawable.oso_megafono),
                    contentDescription = "Oso megáfono",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(120.dp)
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth().height(180.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                ElevatedCard(
                    modifier = Modifier.weight(1f).fillMaxHeight()
                        .clickable { troteSeleccionado = !troteSeleccionado
                                   controller.navigate(route= AppScreens.detallesTrote.name)},
                    colors = CardDefaults.elevatedCardColors(
                        containerColor = Color.White
                    ),
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize().padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Image(painterResource(R.drawable.oso_trote), null, Modifier.size(100.dp))
                        Text(
                            "Actividad en movimiento",
                            fontWeight = FontWeight.Bold,
                            color = colorResource(R.color.rojoGranada),
                            textAlign = TextAlign.Center
                        )
                        RadioButton(
                            selected = troteSeleccionado,
                            onClick = null,
                            colors = RadioButtonDefaults.colors(selectedColor = colorResource(R.color.rojoGranada)),
                            modifier= Modifier.padding(10.dp)
                        )
                    }
                }
                ElevatedCard(
                    onClick = { gymSeleccionado = !gymSeleccionado
                              controller.navigate(route= AppScreens.Ejercicios.name)},
                    modifier = Modifier.weight(1f).fillMaxHeight(),
                    colors = CardDefaults.elevatedCardColors(
                        containerColor = Color.White
                    ),
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize().padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Image(painterResource(R.drawable.oso_gym), null, Modifier.size(100.dp))
                        Text(
                            "Actividad\n Anaerobica",
                            fontWeight = FontWeight.Bold,
                            color = colorResource(R.color.rojoGranada),
                            textAlign = TextAlign.Center
                        )
                        RadioButton(
                            selected = gymSeleccionado,
                            onClick = null,
                            colors = RadioButtonDefaults.colors(selectedColor = colorResource(R.color.rojoGranada)),
                            modifier= Modifier.padding(10.dp)
                        )
                    }
                }
            }
            Column(
                modifier = Modifier.padding(15.dp).fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                DefaulButton("Terminar sesión", 220, 40) {
                    Log.i("TAGInicio", "Click Iniciar ")
                    controller.navigate(route = AppScreens.ResumenTrote.name)
                }
            }
        }
    }
}

