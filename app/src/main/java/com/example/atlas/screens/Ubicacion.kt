package com.example.atlas.screens

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
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
import com.example.atlas.sensorManager
import com.example.atlas.viewmodels.ModeloUbicacion
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.*

@Composable
fun Ubicacion(
    navController: NavController,
    modelo: ModeloUbicacion = viewModel()
) {
    val estado by modelo.estado.collectAsState()
    val contexto = LocalContext.current
    val sensorLuz = remember {
        sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)
    }
    var esOscuro by remember { mutableStateOf(false) }

    val listenerLuz = remember {
        object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent?) {
                if (event?.sensor?.type == Sensor.TYPE_LIGHT) {
                    esOscuro = event.values[0] < 50
                }
            }
            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }
    }

    DisposableEffect(Unit) {
        sensorManager.registerListener(listenerLuz, sensorLuz, android.hardware.SensorManager.SENSOR_DELAY_NORMAL)
        onDispose { sensorManager.unregisterListener(listenerLuz) }
    }

    // Estilo del mapa según luminosidad
    val estiloMapa = remember(esOscuro) {
        MapStyleOptions.loadRawResourceStyle(
            contexto,
            if (esOscuro) R.raw.mapa_oscuro else R.raw.mapa_claro
        )
    }

    // Cámara del mapa
    val posicionCamara = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(4.627293, -74.063228), 15f)
    }

    LaunchedEffect(estado.latitud, estado.longitud) {
        if (estado.latitud != 0.0 || estado.longitud != 0.0) {
            posicionCamara.animate(
                CameraUpdateFactory.newLatLngZoom(
                    LatLng(estado.latitud, estado.longitud), 15f
                )
            )
        }
    }

    Scaffold(
        topBar = { DefaultTopAppBar("Monitoreo y ubicación del deportista") },
        bottomBar = { DefaultBottomBarEnt(R.color.rojoGranada, navController) }
    ) { paddingValues ->
        Column(
            modifier = Modifier.fillMaxSize().padding(paddingValues)
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 30.dp, vertical = 15.dp)
            ) {
                GoogleMap(
                    modifier = Modifier.fillMaxSize(),
                    cameraPositionState = posicionCamara,
                    properties = MapProperties(mapStyleOptions = estiloMapa),
                    uiSettings = MapUiSettings(zoomControlsEnabled = true)
                ) {
                    if (estado.latitud != 0.0 || estado.longitud != 0.0) {
                        Marker(
                            state = MarkerState(
                                position = LatLng(estado.latitud, estado.longitud)
                            ),
                            title = estado.nombreDeportista,
                            snippet = estado.direccion
                        )
                    }
                }
            }

            ElevatedCard(
                modifier = Modifier.padding(vertical = 15.dp, horizontal = 30.dp),
                colors = CardDefaults.cardColors(containerColor = colorResource(R.color.pink))
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.Start),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth().padding(15.dp)
                ) {
                    Box {
                        Icon(Icons.Default.AccountCircle, "Símbolo de persona",
                            modifier = Modifier.size(50.dp))
                        Icon(Icons.Default.CheckCircle, contentDescription = "Estado",
                            modifier = Modifier.align(Alignment.BottomEnd),
                            tint = colorResource(R.color.teal_700))
                    }
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(5.dp, Alignment.CenterVertically),
                        horizontalAlignment = Alignment.Start
                    ) {
                        Text(estado.nombreDeportista, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        Text("Ubicación: ${estado.direccion}", fontSize = 12.sp)
                        Text("Distancia recorrida: ${estado.distanciaKm} km", fontSize = 12.sp)
                    }
                }
            }
        }
    }
}