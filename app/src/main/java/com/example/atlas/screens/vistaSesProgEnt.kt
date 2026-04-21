package com.example.atlas.screens

import android.graphics.Color
import android.text.Layout
import androidx.compose.animation.expandVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import com.example.atlas.R
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Face
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
import com.example.atlas.elements.DefaultBottomBarEnt
import com.example.atlas.elements.DefaultTopAppBar
import com.example.atlas.navegation.AppScreens

data class SesionesActivas(val nombre: String, val actividad: String, val fecha: String, val estado: String)

@Composable

fun vistaSesionesEntrenador(controller: NavController){
    val sesionesActivas= listOf(
        SesionesActivas("Santiago Ramirez", "Trote", "02/03/2026-8:30", "ACTIVA"),
        SesionesActivas("Ricardo Naranjo", "Sesión de entrenamiento", "02/03/2026-9:00", "ACTIVA"),
        SesionesActivas("Juan Vallejo", "Trote y sesión de entrenamiento", "02/03/2026-9:20", "ACTIVA"),
        SesionesActivas("Eddy Daza", "Trote", "02/03/2026-10:00", "ACTIVA"),
        SesionesActivas("Juan Motta", "Trote", "03/03/2026-15:30", "PENDIENTE"),
        SesionesActivas("Sebastian Mendoza", "Trote", "03/03/2026-15:30", "PENDIENTE"),
        SesionesActivas("Thomas Gutierrez", "Trote", "03/03/2026-15:30", "PENDIENTE"),
        SesionesActivas("Salvatore Montenegro", "Trote", "03/03/2026-15:30", "PENDIENTE"),
        SesionesActivas("Jacobo Angulo", "Trote", "03/03/2026-15:30", "PENDIENTE")
    )
    Scaffold(
        topBar = { DefaultTopAppBar("Sesiones programadas") },
        bottomBar = { DefaultBottomBarEnt(R.color.white,controller) }
    ) { paddingValues ->
        Column(modifier=Modifier.padding(paddingValues)) {
            Row(verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier.fillMaxWidth().height(150.dp)) {
                Image(
                    painter = painterResource(R.drawable.puma),
                    contentDescription = "Personaje hablando",
                    contentScale = ContentScale.FillHeight,
                    modifier = Modifier.fillMaxHeight()
                )
                Box (contentAlignment = Alignment.Center){
                    Image(
                        painter = painterResource(R.drawable.burbujadialogo),
                        contentDescription = "Dialogo",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.FillBounds,
                    )
                    Text("Elige al deportista ",
                        color = colorResource(R.color.rojoGranada),
                        fontSize = (18.sp),
                        modifier = Modifier.padding (25.dp),
                        textAlign = TextAlign.Center)


                }
            }
            LazyColumn(
                modifier = Modifier.fillMaxWidth().weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                items(sesionesActivas) { item ->
                    ElevatedCard(
                        modifier = Modifier.padding(
                            vertical = 15.dp, horizontal = 10.dp
                        )
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.Start),
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth().clickable{controller.navigate(route= AppScreens.Ejercicios.name)}.padding(15.dp)
                        ) {
                            Box {
                                Icon(
                                    Icons.Default.AccountCircle,
                                    "Simbolo de persona",
                                    modifier = Modifier.size(50.dp)
                                )
                                Icon(
                                    Icons.Default.CheckCircle,
                                    contentDescription = "Estado",
                                    modifier = Modifier.align(Alignment.BottomEnd),
                                    tint = colorResource(R.color.teal_700)
                                )
                            }
                            Column(
                                modifier = Modifier.weight(1f),
                                verticalArrangement = Arrangement.spacedBy(
                                    5.dp,
                                    Alignment.CenterVertically
                                ),
                                horizontalAlignment = Alignment.Start
                            ) {
                                Text(item.nombre, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                                Text(item.actividad, fontSize = 12.sp)
                                Text(item.fecha, fontSize = 12.sp)
                            }
                            val color =
                                if (item.estado == "ACTIVA") colorResource(R.color.teal_700) else colorResource(
                                    R.color.rojos
                                )
                            Box(
                                modifier = Modifier.padding(5.dp)
                                    .background(color, shape = CircleShape)
                            ) {
                                Text(
                                    item.estado,
                                    color = colorResource(R.color.white),
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(5.dp)
                                )
                            }
                        }
                    }

                }
            }
        }

}
}