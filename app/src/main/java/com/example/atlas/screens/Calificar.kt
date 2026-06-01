package com.example.atlas.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.atlas.R
import com.example.atlas.elements.DefaulButton
import com.example.atlas.elements.DefaultBottomBarDep
import com.example.atlas.elements.DefaultTopAppBar
import com.example.atlas.objetosDB.Entrenador
import com.example.atlas.viewmodels.ModeloCalificar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Calificar(
    controller: NavController,
    modelo: ModeloCalificar = viewModel()
) {
    val state by modelo.uiState.collectAsState()
    val context = LocalContext.current
    var showRatingDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = { DefaultTopAppBar(nombre = "Califica") },
        bottomBar = { DefaultBottomBarDep(colorId = R.color.white, controller = controller) }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.Start
        ) {

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Nuestros Entrenadores",
                color = colorResource(R.color.rojoGranada),
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(state.todosEntrenadores) { entrenador ->
                    EntrenadorCard(
                        entrenador = entrenador,
                        isAdded = state.misEntrenadoresIds.contains(entrenador.id),
                        onAddClick = { modelo.agregarEntrenador(entrenador.id) },
                        onRateClick = {
                            modelo.seleccionarEntrenador(entrenador)
                            showRatingDialog = true
                        }
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(80.dp))
        }

        if (showRatingDialog) {
            RatingDialog(
                entrenador = state.entrenadorSeleccionado,
                calificacion = state.calificacion,
                comentario = state.comentario,
                onCalificacionChange = { modelo.actualizarCalificacion(it) },
                onComentarioChange = { modelo.actualizarComentario(it) },
                onDismiss = { showRatingDialog = false },
                onConfirm = {
                    modelo.enviarCalificacion {
                        showRatingDialog = false
                        Toast.makeText(context, "Calificación enviada", Toast.LENGTH_SHORT).show()
                    }
                }
            )
        }
    }
}

@Composable
fun EntrenadorCard(
    entrenador: Entrenador,
    isAdded: Boolean,
    onAddClick: () -> Unit,
    onRateClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = entrenador.imagen.ifEmpty { R.drawable.puma_think },
                contentDescription = null,
                modifier = Modifier
                    .size(60.dp)
                    .background(Color.LightGray, RoundedCornerShape(30.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(text = entrenador.nombre, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(text = entrenador.especialidad, fontSize = 12.sp, color = Color.Gray)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = colorResource(R.color.rojoGranada),
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = String.format("%.1f", entrenador.calificacionPromedio),
                        fontSize = 12.sp,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Button(
                    onClick = onAddClick,
                    enabled = !isAdded,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(R.color.rojoGranada),
                        disabledContainerColor = Color.Gray
                    ),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                    modifier = Modifier.height(30.dp)
                ) {
                    Text(if (isAdded) "Agregado" else "Agregar", fontSize = 10.sp)
                }

                Spacer(modifier = Modifier.height(4.dp))

                Button(
                    onClick = onRateClick,
                    enabled = isAdded,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(R.color.teal_700),
                        disabledContainerColor = Color.Gray
                    ),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                    modifier = Modifier.height(30.dp)
                ) {
                    Text("Calificar", fontSize = 10.sp)
                }
            }
        }
    }
}

@Composable
fun RatingDialog(
    entrenador: Entrenador?,
    calificacion: Int,
    comentario: String,
    onCalificacionChange: (Int) -> Unit,
    onComentarioChange: (String) -> Unit,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Calificar a ${entrenador?.nombre ?: ""}") },
        text = {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    for (i in 1..5) {
                        IconButton(onClick = { onCalificacionChange(i) }) {
                            Icon(
                                imageVector = if (i <= calificacion) Icons.Filled.Star else Icons.Outlined.Star,
                                contentDescription = null,
                                tint = if (i <= calificacion) colorResource(R.color.rojoGranada) else Color.Gray,
                                modifier = Modifier.size(32.dp)
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = comentario,
                    onValueChange = onComentarioChange,
                    placeholder = { Text("Escribe un comentario...") },
                    modifier = Modifier.fillMaxWidth().height(100.dp)
                )
            }
        },
        confirmButton = {
            TextButton(onClick = onConfirm, enabled = calificacion > 0) {
                Text("Enviar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}
