package com.example.atlas.screens

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
import com.example.atlas.viewmodels.EjercicioViewModel

@Composable
fun ChequeoSesion(controller: NavController, viewModel: EjercicioViewModel = viewModel()) {
    val estado by viewModel.estado.collectAsState()
    Scaffold(
        topBar = { DefaultTopAppBar(nombre = "Estado de sesion") },
        bottomBar = { DefaultBottomBarDep(colorId = R.color.white, controller = controller) }
    ) { paddingValues ->

        Column(
            modifier = Modifier.fillMaxSize().padding(paddingValues).padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))


            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {

                Image(
                    painter = painterResource(id = R.drawable.puma),
                    contentDescription = "Mascota Puma",
                    modifier = Modifier.size(150.dp)
                )

                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.weight(1f)
                ) {

                    Icon(
                        painter = painterResource(R.drawable.burbuja),
                        contentDescription = "DialogoPuma",
                        tint = colorResource(R.color.rojoGranada)
                    )

                    Text(
                        text = "¡Así se hace! Marca\ncada ejercicio\ncompletado para seguir\nel progreso.",
                        color = colorResource(R.color.rojoGranada),
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            LazyColumn {
                items(estado.lista) { ejercicio ->

                    Card(
                        modifier = Modifier.padding(8.dp),
                        shape = RoundedCornerShape(16.dp)
                    ) {

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Column {
                                Text(ejercicio.nombre, fontWeight = FontWeight.Bold)

                                Row {
                                    Text("Series: ${ejercicio.series}")
                                    Spacer(Modifier.width(8.dp))
                                    Text("Rep: ${ejercicio.rep}")
                                    Spacer(Modifier.width(8.dp))
                                    Text("Kg: ${ejercicio.kg}")
                                }
                            }
                            IconButton(
                                onClick = {
                                    viewModel.toggleCompletado(ejercicio.id)
                                }
                            ) {
                                Icon(
                                    imageVector = if (ejercicio.completado)
                                        Icons.Default.Check
                                    else
                                        Icons.Default.Info,
                                    contentDescription = null,
                                    tint = if (ejercicio.completado)
                                        Color.Red
                                    else
                                        Color.Gray
                                )
                            }
                        }
                    }
                }
            }

            DefaulButton(text = "Terminar sesion", ancho = 240, alto = 50) {
                controller.navigate(route = AppScreens.CrearNuevaSesion.name)
            }

        }
        }
    }


@Preview(showBackground = true)
@Composable
fun PreviewChequeoSesion() {
    ChequeoSesion(rememberNavController())
}