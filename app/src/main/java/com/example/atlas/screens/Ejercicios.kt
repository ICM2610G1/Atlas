package Pantallas

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.atlas.R
import com.example.atlas.elements.DefaultBottomBarDep
import com.example.atlas.elements.DefaultTopAppBar

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
fun PantallaEjercicios(controller: NavController) {

    val rojo = colorResource(id = R.color.rojoGranada)

    val ejercicios = listOf(
        Ejercicio(1, "Press de Banca Plano", 4, 10, 60, true),
        Ejercicio(2, "Sentadilla con Barra", 4, 8, 80, true),
        Ejercicio(3, "Peso Muerto Rumano", 3, 12, 50, false),
        Ejercicio(4, "Press Militar", 3, 10, 18, true),
        Ejercicio(5, "Curl de Bíceps con Mancuerna", 3, 12, 14, false),
        Ejercicio(6, "Extensión de Tríceps", 3, 15, 20, true)
    )

    Scaffold(
        containerColor = colorResource(R.color.pink),
        topBar = { DefaultTopAppBar("Ejercicios") },
        bottomBar = { DefaultBottomBarDep(R.color.white, controller) }
    ) { padding ->

        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {

            items(ejercicios) { ejercicio ->

                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {

                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {

                            Text(
                                text = ejercicio.nombre,
                                color = rojo,
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

                        IconButton(
                            onClick = {},
                            modifier = Modifier
                                .size(50.dp)
                                .border(
                                    width = 2.dp,
                                    color = rojo,
                                    shape = CircleShape
                                )
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = null,
                                tint = rojo
                            )
                        }
                    }
                }
            }
        }
    }
}