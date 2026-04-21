package com.example.atlas.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
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
import com.example.atlas.viewmodels.ModeloCalificar

@Composable
fun Calificar(
    controller: NavController,
    modelo: ModeloCalificar = viewModel()
) {
    val estado by modelo.estado.collectAsState()
    val context = LocalContext.current

    Scaffold(
        topBar = { DefaultTopAppBar(nombre = "Califica") },
        bottomBar = { DefaultBottomBarDep(colorId = R.color.white, controller = controller) }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.Start
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Entrenador(a)",
                color = colorResource(R.color.rojoGranada),
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
            TextField(
                value = estado.entrenador,
                onValueChange = { modelo.actualizarEntrenador(it) },
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                placeholder = { Text("Escribe el nombre de tu entrenador...") }
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Calificación",
                color = colorResource(R.color.rojoGranada),
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                horizontalArrangement = Arrangement.Start
            ) {
                for (i in 1..5) {
                    IconButton(onClick = { modelo.actualizarCalificacion(i) }) {
                        Icon(
                            imageVector = if (i <= estado.calificacion) Icons.Filled.Star else Icons.Outlined.Star,
                            contentDescription = "Estrella $i",
                            tint = if (i <= estado.calificacion) colorResource(R.color.rojoGranada) else Color.Gray,
                            modifier = Modifier.size(36.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Comentarios adicionales",
                color = colorResource(R.color.rojoGranada),
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
            TextField(
                value = estado.comentario,
                onValueChange = { modelo.actualizarComentario(it) },
                modifier = Modifier.fillMaxWidth().height(150.dp).padding(top = 8.dp),
                placeholder = { Text("Agregue comentarios adicionales sobre su experiencia con el entrenador...") }
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                DefaulButton(text = "Enviar Calificación", ancho = 240, alto = 50) {
                    Toast.makeText(
                        context,
                        "Calificación de ${estado.calificacion} estrellas enviada",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Image(
                painter = painterResource(id = R.drawable.puma_think),
                contentDescription = "Atlas Pensante",
                modifier = Modifier.size(150.dp).align(Alignment.Start)
            )
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun VistaCalificar() {
    Calificar(rememberNavController())
}