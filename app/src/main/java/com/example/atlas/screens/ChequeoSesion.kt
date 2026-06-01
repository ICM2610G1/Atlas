package com.example.atlas.screens

import EjercicioViewModel
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.atlas.R
import com.example.atlas.elements.DefaulButton
import com.example.atlas.elements.DefaultBottomBarDep
import com.example.atlas.elements.DefaultTopAppBar
import com.example.atlas.navegation.AppScreens
import kotlinx.coroutines.flow.compose


@Composable
fun ChequeoSesion(controller: NavController, viewModel: EjercicioViewModel) {
    // Escuchamos la lista en tiempo real directamente desde tu StateFlow
    val ejercicios by viewModel.ejerciciosSesion.collectAsState()

    Scaffold(
        topBar = { DefaultTopAppBar("Chequeo de Sesión") },
        bottomBar = { DefaultBottomBarDep(R.color.rojoGranada, controller) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Control de Rutina",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = colorResource(R.color.rojoGranada),
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Lista de los ejercicios agregados a esta sesión
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(ejercicios, key = { it.firebaseKey }) { item ->
                    val ej = item.ejercicio

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (ej.completado) Color(0xFFE8F5E9) else MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = ej.nombre,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Row {
                                    Text("Series: ${ej.series}", fontSize = 14.sp)
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Text("Reps: ${ej.repeticiones}", fontSize = 14.sp)
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Text("Peso: ${ej.peso} Kg", fontSize = 14.sp)
                                }
                            }
                            IconButton(
                                onClick = {
                                    viewModel.toggleCompletado(item.firebaseKey, ej.completado)
                                }
                            ) {
                                Icon(
                                    imageVector = if (ej.completado) Icons.Default.Check else Icons.Default.Info,
                                    contentDescription = "Estado",
                                    tint = if (ej.completado) Color(0xFF2E7D32) else Color.Gray
                                )
                            }
                        }
                    }
                }
            }
            DefaulButton(text = "Terminar sesión", ancho = 240, alto = 50) {
                controller.navigate(route = AppScreens.CrearNuevaSesion.name)
            }
        }
    }
}
