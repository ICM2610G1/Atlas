package com.example.atlas.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
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

data class Actividad(
    val nombre: String,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetallesTrote(controller: NavController) {


    val activityOptions = listOf(
        Actividad("Trote"),
        Actividad("Ciclismo"),
        Actividad("Senderismo")
    )

    var selectedActivity by remember { mutableStateOf<String?>(null) }
    var lugarInicio by remember { mutableStateOf("") }
    var lugarFinal by remember { mutableStateOf("") }

    Scaffold(
        containerColor = colorResource(R.color.pink),
        topBar = { DefaultTopAppBar("Detalles de actividad") },
        bottomBar = { DefaultBottomBarDep(R.color.white, controller) }
    ) { paddingValues ->

        Column(
            modifier = Modifier.fillMaxSize().padding(paddingValues).padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.Top
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().height(160.dp).padding(top = 8.dp, bottom = 16.dp),
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
                        text = "¿Que actividad vas a realizar?",
                        color = colorResource(R.color.rojoGranada),
                        fontSize = 18.sp,
                        modifier = Modifier.align(Alignment.Center).padding(horizontal = 32.dp, vertical = 60.dp),
                        textAlign = TextAlign.Center)
                }
            }
            Text(
                text = "Tipo de actividad",
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 10.dp)
            )

            activityOptions.forEach { actividad ->
                val isSelected = selectedActivity == actividad.nombre
                ElevatedCard(
                    onClick = { selectedActivity = actividad.nombre },
                    modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp),
                    shape = RoundedCornerShape(14.dp),
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(42.dp)
                                    .clip(CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Image(
                                    painter = painterResource(R.drawable.puma),
                                    contentDescription = actividad.nombre,
                                    modifier = Modifier.size(32.dp)
                                )
                            }
                            Text(
                                text = actividad.nombre,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 16.sp,
                                color = if (isSelected) colorResource(R.color.rojoGranada) else Color.Black,
                                modifier = Modifier.padding(start = 12.dp)
                            )
                        }
                        RadioButton(
                            selected = isSelected,
                            onClick = { selectedActivity = actividad.nombre },
                            colors = RadioButtonDefaults.colors(
                                selectedColor = colorResource(R.color.rojoGranada),
                                unselectedColor = colorResource(R.color.rojoGranada)
                            )
                        )
                    }
                }
            }

            Text(
                text = "Ruta",
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 10.dp)
            )

            OutlinedTextField(
                value = lugarInicio,
                onValueChange = { lugarInicio = it },
                label = { Text("Lugar de inicio") },
                leadingIcon = {
                    Icon(
                        Icons.Outlined.LocationOn,
                        contentDescription = null,
                        tint = colorResource(R.color.rojoGranada)
                    )
                },
                modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = colorResource(R.color.rojoGranada),
                    unfocusedBorderColor =colorResource(R.color.rojoGranada),
                    focusedLabelColor = colorResource(R.color.rojoGranada),
                    unfocusedContainerColor = Color.White,
                    focusedContainerColor = Color.White
                ),
                singleLine = true
            )

            OutlinedTextField(
                value = lugarFinal,
                onValueChange = { lugarFinal = it },
                label = { Text("Lugar de destino") },
                leadingIcon = {
                    Icon(
                        Icons.Outlined.LocationOn,
                        contentDescription = null,
                        tint = colorResource(R.color.rojoGranada)
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = colorResource(R.color.rojoGranada),
                    unfocusedBorderColor = colorResource(R.color.rojoGranada),
                    focusedLabelColor = colorResource(R.color.rojoGranada),
                    unfocusedContainerColor = Color.White,
                    focusedContainerColor = Color.White
                ),
                singleLine = true
            )

            Button(
                onClick = { controller.navigate(route = AppScreens.Mimapa.name + "/${lugarInicio}/${lugarFinal}/${selectedActivity}") },
                enabled = selectedActivity != null && lugarInicio.isNotBlank() && lugarFinal.isNotBlank(),
                modifier = Modifier.fillMaxWidth().height(52.dp).padding(bottom = 4.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(R.color.rojoGranada),
                    contentColor = Color.White,
                    disabledContainerColor = colorResource(R.color.rojoGranada),
                    disabledContentColor = Color.White
                )
            ) {
                Text(
                    text = "Confirmar",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}