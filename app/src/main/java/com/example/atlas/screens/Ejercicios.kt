package Pantallas

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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.collections.emptyList
data class SobreEjercicios(
    val lista: List<Ejercicio> = emptyList()
)

class EjercicioViewModel : ViewModel() {

    private val _estado = MutableStateFlow(SobreEjercicios())
    val estado: StateFlow<SobreEjercicios> = _estado.asStateFlow()

    fun agregarEjercicio(nombre: String) {
        val nuevo = Ejercicio(
            id = _estado.value.lista.size + 1,
            nombre = nombre,
            series = 3,
            rep = 10,
            kg = 22,
            completado = false
        )
        _estado.update {
            it.copy(lista = it.lista + nuevo)
        }
    }
    fun toggleCompletado(id: Int) {
        _estado.update {
            it.copy(
                lista = it.lista.map { ejercicio ->
                    if (ejercicio.id == id) {
                        ejercicio.copy(completado = !ejercicio.completado)
                    } else ejercicio
                }
            )
        }
    }
}
data class Ejercicio(
    val id: Int,
    val nombre: String,
    val series: Int,
    val rep: Int,
    val kg: Int,
    val completado: Boolean
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaEjercicios(controller: NavController, ejercicio: String?=null, viewModel: EjercicioViewModel = viewModel() ){

    val estado by viewModel.estado.collectAsState()

    LaunchedEffect(ejercicio) {
        if (ejercicio != null) {
            viewModel.agregarEjercicio(ejercicio)
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

                items(estado.lista) { ejercicio ->

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
                                    text = ejercicio.nombre,
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
                                        Text("${ejercicio.series}")
                                    }

                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Text("Rep", fontWeight = FontWeight.Bold)
                                        Text("${ejercicio.rep}")
                                    }

                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Text("Kg", fontWeight = FontWeight.Bold)
                                        Text("${ejercicio.kg}")
                                    }
                                }
                            }
                        }
                    }
                }
            }
            Column(modifier=Modifier.weight(2f)) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding (vertical = 50.dp),
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