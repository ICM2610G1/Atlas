package com.example.atlas.screens

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.atlas.R
import com.example.atlas.elements.DefaultBottomBarEnt
import com.example.atlas.elements.DefaultTopAppBar
import com.example.atlas.viewmodels.ModeloUbicacion
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.*

@Composable
fun Ubicacion(
    controller: NavController,
    modelo: ModeloUbicacion = viewModel()
) {
    val estado by modelo.estado.collectAsState()
    val contexto = LocalContext.current

    val sensorManager = contexto.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    val sensorLuz: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)

    val mapaClaro = MapStyleOptions.loadRawResourceStyle(contexto, R.raw.mapa_claro)
    val mapaOscuro = MapStyleOptions.loadRawResourceStyle(contexto, R.raw.mapa_oscuro)

    var estiloActual by remember { mutableStateOf(mapaClaro) }

    val listenerLuz = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent?) {
            if (event?.sensor?.type == Sensor.TYPE_LIGHT) {
                val lux = event.values[0]
                // < 2000 lux → mapa oscuro | >= 2000 lux → mapa claro
                estiloActual = if (lux < 2000) mapaOscuro else mapaClaro
            }
        }
        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
    }

    // DisposableEffect — suscripción/desuscripción al sensor
    DisposableEffect(Unit) {
        sensorManager.registerListener(
            listenerLuz,
            sensorLuz,
            SensorManager.SENSOR_DELAY_NORMAL
        )
        onDispose {
            sensorManager.unregisterListener(listenerLuz)
        }
    }

    val posDeportista = LatLng(estado.latitud, estado.longitud)

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(posDeportista, 15f)
    }

    // Actualiza la cámara cuando cambie la posición del deportista
    LaunchedEffect(estado.latitud, estado.longitud) {
        cameraPositionState.position =
            CameraPosition.fromLatLngZoom(posDeportista, 15f)
    }

    Scaffold(
        topBar = { DefaultTopAppBar("Monitoreo y ubicación del deportista") },
        bottomBar = { DefaultBottomBarEnt(R.color.rojoGranada, controller) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {

            // Mapa Google
            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 30.dp, vertical = 15.dp)
            ) {
                GoogleMap(
                    modifier = Modifier.fillMaxSize(),
                    cameraPositionState = cameraPositionState,
                    properties = MapProperties(mapStyleOptions = estiloActual),
                    uiSettings = MapUiSettings(
                        zoomControlsEnabled = true,
                        compassEnabled = true
                    )
                ) {
                    Marker(
                        state = rememberMarkerState(position = posDeportista),
                        title = estado.nombreDeportista,
                        snippet = estado.direccion
                    )
                }
            }

            // Tarjeta info deportista
            ElevatedCard(
                modifier = Modifier.padding(vertical = 15.dp, horizontal = 30.dp),
                colors = CardDefaults.cardColors(colorResource(R.color.pink))
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.Start),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(15.dp)
                ) {
                    Box {
                        Icon(
                            Icons.Default.AccountCircle,
                            "Símbolo de persona",
                            modifier = Modifier.size(50.dp)
                        )
                        Icon(
                            Icons.Default.CheckCircle,
                            contentDescription = "Estado",
                            modifier = Modifier.align(Alignment.BottomEnd),
                            tint = colorResource(R.color.teal_700)
                        )
                    }
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(5.dp, Alignment.CenterVertically),
                        horizontalAlignment = Alignment.Start
                    ) {
                        Text(
                            estado.nombreDeportista,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            "Ubicación: ${estado.direccion}",
                            fontSize = 12.sp
                        )
                        Text(
                            "Distancia recorrida: ${estado.distanciaKm} km",
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }
    }
}