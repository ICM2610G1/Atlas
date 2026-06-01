package Pantallas

import EjercicioViewModel
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.atlas.R
import com.example.atlas.elements.DefaulButton
import com.example.atlas.elements.DefaultBottomBarDep
import com.example.atlas.elements.DefaultTopAppBar
import com.example.atlas.navegation.AppScreens
import com.example.atlas.screens.TipoCuenta
import com.example.atlas.viewmodels.CrearSesionViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.collections.emptyList



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaEjercicios(controller: NavController, ejercicio: String? = null, modelEjercicios: EjercicioViewModel = viewModel(), modelSesion: CrearSesionViewModel = viewModel()
) {
    val sesionState by modelSesion.uiState.collectAsState()
    val ejercicios by modelEjercicios.ejerciciosSesion.collectAsState()

    LaunchedEffect(sesionState.idSesionActiva) {
        if (sesionState.idSesionActiva.isNotEmpty()) {
            modelEjercicios.iniciarSesionFuerza(sesionState.idSesionActiva)
        }
    }

    Scaffold(
        containerColor = colorResource(R.color.pink),
        topBar = { DefaultTopAppBar("Ejercicios") },
        bottomBar = { DefaultBottomBarDep(R.color.white, controller) }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding)) {
            LazyColumn(
                modifier = Modifier.padding(16.dp).weight(8f),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(ejercicios, key = { it.firebaseKey }) { ej ->
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Column(
                                modifier = Modifier.weight(1f),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    text = ej.ejercicio.nombre,
                                    color = colorResource(R.color.rojoGranada),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp
                                )

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Text("Series", fontWeight = FontWeight.Bold)
                                        Text("${ej.ejercicio.series}")
                                    }

                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Text("Rep", fontWeight = FontWeight.Bold)
                                        Text("${ej.ejercicio.repeticiones}")
                                    }

                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Text("Kg", fontWeight = FontWeight.Bold)
                                        Text("${ej.ejercicio.peso}")
                                    }
                                }
                            }
                        }
                    }
                }
            }
            Column(modifier = Modifier.weight(2f)) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 50.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    DefaulButton("Agregar ejercicios", 220, 40) {
                        controller.navigate(route = AppScreens.agregarEjercicio.name)
                    }
                    DefaulButton("Iniciar actividad", 220, 40) {
                        controller.navigate(route = AppScreens.ChequeoSesion.name)
                    }
                }
            }
        }
    }
}