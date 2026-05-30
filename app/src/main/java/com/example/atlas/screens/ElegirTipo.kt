package com.example.atlas.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.atlas.R
import com.example.atlas.elements.DefaultBottomBarDep
import com.example.atlas.elements.DefaultTopAppBar
import com.example.atlas.navegation.AppScreens

data class TipoCuenta(
    val nombre: String,
    val descripcion: String // Un plus para ayudar al usuario a elegir
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ElegirTipo(controller: NavController) {

    val opcionesRol = listOf(
        TipoCuenta("Deportista", "Entrena, registra tus actividades y sigue tus rutinas."),
        TipoCuenta("Entrenador", "Gestiona alumnos, diseña rutinas y haz seguimiento profesional.")
    )

    var selectedRol by remember { mutableStateOf<String?>(null) }

    Scaffold(
        containerColor = colorResource(R.color.pink),
        topBar = { DefaultTopAppBar("Registro de cuenta") },
        // Mantenemos una barra inferior o la quitamos si en el registro no aplica
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.Top
        ) {
            // Sección del Puma con la burbuja de diálogo adaptada al registro
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .padding(top = 8.dp, bottom = 16.dp),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.Start
            ) {
                Image(
                    painter = painterResource(R.drawable.puma),
                    contentDescription = "Puma",
                    contentScale = ContentScale.FillHeight,
                    modifier = Modifier.fillMaxHeight()
                )
                Box(modifier = Modifier.size(230.dp)) {
                    Image(
                        painter = painterResource(R.drawable.burbujadialogo),
                        contentDescription = "Dialogo",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.FillBounds
                    )
                    Text(
                        text = "¿Cuál va a ser tu rol en la aplicación?",
                        color = colorResource(R.color.rojoGranada),
                        fontSize = 17.sp, // Ajustado ligeramente para que quepa bien el texto largo
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(horizontal = 28.dp, vertical = 50.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }

            Text(
                text = "Selecciona tu tipo de cuenta",
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 12.dp)
            )
            opcionesRol.forEach { rol ->
                val isSelected = selectedRol == rol.nombre
                ElevatedCard(
                    onClick = { selectedRol = rol.nombre },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                    shape = RoundedCornerShape(14.dp),
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            modifier = Modifier.weight(1f), // Evita que el texto pise al RadioButton
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(42.dp)
                                    .clip(CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Image(
                                    painter = painterResource(R.drawable.puma),
                                    contentDescription = rol.nombre,
                                    modifier = Modifier.size(32.dp)
                                )
                            }
                            Column(modifier = Modifier.padding(start = 12.dp)) {
                                Text(
                                    text = rol.nombre,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                    color = if (isSelected) colorResource(R.color.rojoGranada) else Color.Black
                                )
                                Text(
                                    text = rol.descripcion,
                                    fontSize = 12.sp,
                                    color = Color.Gray,
                                    modifier = Modifier.padding(top = 2.dp)
                                )
                            }
                        }
                        RadioButton(
                            selected = isSelected,
                            onClick = { selectedRol = rol.nombre },
                            colors = RadioButtonDefaults.colors(
                                selectedColor = colorResource(R.color.rojoGranada),
                                unselectedColor = colorResource(R.color.rojoGranada)
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f)) // Empuja el botón al fondo de la pantalla limpia

            // Botón de Confirmar que navega pasando el parámetro del rol elegido
            Button(
                onClick = {
                    if (selectedRol == "Deportista") {
                        controller.navigate(route = AppScreens.SignUpDep.name)
                    } else if (selectedRol == "Entrenador") {
                        controller.navigate(route = AppScreens.SignUpEnt.name)
                    }
                },
                enabled = selectedRol != null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(bottom = 8.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(R.color.rojoGranada),
                    contentColor = Color.White,
                    disabledContainerColor = colorResource(R.color.rojoGranada).copy(alpha = 0.5f), // Opacidad para el deshabilitado
                    disabledContentColor = Color.White
                )
            ) {
                Text(
                    text = "Siguiente",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}