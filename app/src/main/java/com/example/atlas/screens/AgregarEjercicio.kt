package com.example.atlas.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.atlas.R
import com.example.atlas.elements.DefaultBottomBarDep
import com.example.atlas.elements.DefaultBottomBarEnt
import com.example.atlas.elements.DefaultTopAppBar
import com.example.atlas.navegation.AppScreens


data class TipoActividad(
    val nombre: String,
    val descripcion: String,
    val grupoMuscular: String
)

@Composable
fun CatalogoActividades(controller: NavController) {
    val actividades = listOf(
        TipoActividad("Press de Banca", "Empuje horizontal con barra para fuerza", "Pecho"),
        TipoActividad("Sentadilla", "Flexión de rodillas con carga sobre hombros", "Cuádriceps"),
        TipoActividad("Peso Muerto", "Levantamiento de carga desde punto muerto", "Espalda"),
        TipoActividad("Press Militar", "Empuje vertical para fuerza de hombro", "Hombros"),
        TipoActividad("Dominadas", "Tracción vertical de peso corporal", "Espalda")
    )

    Scaffold(
        topBar = { DefaultTopAppBar("Catálogo de Actividades Atlas") },
        bottomBar = { DefaultBottomBarDep(R.color.rojoGranada, controller) }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(paddingValues).padding(horizontal = 30.dp),
            verticalArrangement = Arrangement.spacedBy(15.dp),
        ) {
            items(actividades) { actividad ->
                ElevatedCard(
                    modifier = Modifier.fillMaxWidth().clickable{controller.navigate(AppScreens.Ejercicios.name + "/${actividad.nombre}")},
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(15.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(15.dp)
                    ) {
                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = actividad.nombre,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = colorResource(R.color.rojoGranada)
                            )
                            Text(
                                text = actividad.descripcion,
                                fontSize = 12.sp,
                                lineHeight = 16.sp
                            )
                            Text(
                                text = "Grupo: ${actividad.grupoMuscular}",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }
        }
    }
}