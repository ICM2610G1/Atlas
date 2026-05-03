package com.example.atlas.screens

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.atlas.R
import com.example.atlas.elements.DefaultBottomBarDep
import com.example.atlas.elements.DefaultTopAppBarHome
import com.example.atlas.navegation.AppScreens
import com.example.atlas.sensorManager

@Composable
fun Home(controller: NavController) {

    val sensorTemperatura = remember {
        sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE)
    }

    var temperatura by remember { mutableStateOf(0f) }

    val listener = remember {
        object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent?) {
                if (event?.sensor?.type == Sensor.TYPE_AMBIENT_TEMPERATURE) {
                    temperatura = event.values[0]
                }
            }
            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }
    }

    DisposableEffect(Unit) {
        sensorManager.registerListener(listener, sensorTemperatura, SensorManager.SENSOR_DELAY_NORMAL)
        onDispose {
            sensorManager.unregisterListener(listener)
        }
    }

    val recomendacion = remember(temperatura) {
        when {
            temperatura == 0f -> "Obteniendo clima..."
            temperatura < 10 -> "Hace frío, trota con ropa térmica"
            temperatura < 18 -> "Clima fresco, ideal para trotar"
            temperatura < 25 -> "Clima perfecto para entrenar"
            temperatura < 32 -> "Hace calor, hidrátate bien"
            else -> "Muy caliente, mejor entrena indoor"
        }
    }

    val iconoClima = when {
        temperatura < 18 -> Icons.Default.Cloud
        temperatura < 30 -> Icons.Default.WbSunny
        else -> Icons.Default.Whatshot
    }

    Scaffold(
        topBar = { DefaultTopAppBarHome("Home", controller) },
        bottomBar = { DefaultBottomBarDep(R.color.white, controller) }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .background(colorResource(R.color.pink))
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Text("Hola", fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Text("¿Listo para entrenar hoy?", color = Color.Gray)

            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(Color.White),
                elevation = CardDefaults.cardElevation(6.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Icon(
                        iconoClima,
                        contentDescription = null,
                        tint = colorResource(R.color.rojoGranada),
                        modifier = Modifier.size(40.dp)
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            "${"%.1f".format(temperatura)}°C",
                            fontWeight = FontWeight.Bold
                        )
                        Text(recomendacion, fontSize = 13.sp)
                    }
                }
            }

            Text("Lugares para trotar", fontWeight = FontWeight.Bold)

            Row(
                modifier = Modifier.horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                LugarCard("Monserrate", Icons.Default.Terrain) {
                    controller.navigate(AppScreens.Mimapa.name + "/Monserrate Pico de montaña/Trote")
                }

                LugarCard("Parque Simón Bolívar", Icons.Default.Park) {
                    controller.navigate(AppScreens.Mimapa.name + "/Parque Simón Bolívar Bogota/Trote")
                }

                LugarCard("La Calera", Icons.Default.LocationOn) {
                    controller.navigate(AppScreens.Mimapa.name + "/La Calera/Trote")
                }
            }
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(Color.White)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    ProgressItem(
                        Icons.Default.LocalFireDepartment,
                        "Racha",
                        "3 días",
                        modifier = Modifier.weight(1f)
                    )

                    Divider(
                        modifier = Modifier
                            .height(40.dp)
                            .width(1.dp),
                        color = Color.LightGray
                    )

                    ProgressItem(
                        Icons.Default.DirectionsRun,
                        "Distancia",
                        "12 km",
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Image( painter = painterResource(R.drawable.puma),
                contentDescription = null,
                modifier = Modifier.height(140.dp),
                contentScale = ContentScale.FillHeight
            )
        }
    }
}

@Composable
fun LugarCard(nombre: String, icono: androidx.compose.ui.graphics.vector.ImageVector, onClick: () -> Unit) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(colorResource(R.color.rojoGranada)),
        modifier = Modifier
            .width(170.dp)
            .height(120.dp)
            .clickable { onClick() }
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(Color.White, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(icono, contentDescription = null, tint = colorResource(R.color.rojoGranada))
            }

            Spacer(modifier = Modifier.weight(1f))

            Text(nombre, color = Color.White, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun ProgressItem(
    icon: ImageVector,
    title: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(icon, contentDescription = null, tint = colorResource(R.color.rojoGranada))

        Spacer(modifier = Modifier.height(4.dp))

        Text(title, fontSize = 12.sp, color = Color.Gray)

        Text(value, fontWeight = FontWeight.Bold)
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewHome() {
    Home(rememberNavController())
}
