package com.example.atlas.screens

import EjercicioViewModel
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
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
import com.example.atlas.viewmodels.CatalogoEjercicioViewModel
import com.example.atlas.viewmodels.FormularioEjercicioState

import com.example.atlas.viewmodels.FormularioEjercicioViewModel

@Composable
fun CatalogoActividades(controller: NavController, model: CatalogoEjercicioViewModel = viewModel(), modelE: EjercicioViewModel = viewModel(), formularioViewModel: FormularioEjercicioViewModel = viewModel(), ) {
    val disponibles by model.listaDisponibles.collectAsState()
    val formularios by formularioViewModel.formularios.collectAsState()

    Scaffold(
        topBar = { DefaultTopAppBar("Seleccionar Ejercicio") },
        bottomBar = { DefaultBottomBarDep(R.color.rojoGranada, controller) }
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(vertical = 15.dp)
            ) {
                items(disponibles , key = { it.idActividad }) { ejercicio ->
                    val form = formularios[ejercicio.idActividad] ?: FormularioEjercicioState()
                    ElevatedCard(
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = ejercicio.nombre,
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold,
                                color = colorResource(R.color.rojoGranada)
                            )
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                TextField(
                                    value = form.series,
                                    onValueChange = { formularioViewModel.updateSeries(ejercicio.idActividad, it) },
                                    label = { Text("Series") },
                                    modifier = Modifier.weight(1f),
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                                )
                                TextField(
                                    value = form.repeticiones,
                                    onValueChange = { formularioViewModel.updateRepeticiones(ejercicio.idActividad, it)},
                                    label = { Text("Reps") },
                                    modifier = Modifier.weight(1f),
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                                )
                                TextField(
                                    value = form.peso,
                                    onValueChange = { formularioViewModel.updatePeso(ejercicio.idActividad, it) },
                                    label = { Text("Kg") },
                                    modifier = Modifier.weight(1f),
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                                )
                            }

                    
                            DefaulButton("Agregar a la sesión", 200, 40) {
                                modelE.guardarEjercicio(
                                    idActividad   = ejercicio.idActividad,
                                    nombre        = ejercicio.nombre,
                                    grupoMuscular = ejercicio.grupoMuscular,
                                    series        = form.series.toIntOrNull() ?: 3,
                                    repeticiones  = form.repeticiones.toIntOrNull() ?: 10,
                                    peso          = form.peso.toDoubleOrNull() ?: 0.0
                                )
                                controller.navigate(route= AppScreens.Ejercicios.name)
                            }
                        }
                    }
                }
            }
        }
    }
}
