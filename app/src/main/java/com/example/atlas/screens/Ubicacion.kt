package com.example.atlas.screens

import SesionActivaViewModel
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
import com.example.atlas.elements.bitmapDescriptorFromVector
import com.example.atlas.sensorManager
import com.example.atlas.viewmodels.UbicacionViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.*

@Composable
fun Ubicacion(
    navController: NavController,
    id: String,
    modelo: UbicacionViewModel = viewModel()
) {
    val estado by modelo.user.collectAsState()
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
        sensorManager.registerListener(
            listenerLuz,
            sensorLuz,
            android.hardware.SensorManager.SENSOR_DELAY_NORMAL
        )
        onDispose { sensorManager.unregisterListener(listenerLuz) }
    }
    val estiloMapa = remember(esOscuro) {
        MapStyleOptions.loadRawResourceStyle(
            contexto,
            if (esOscuro) R.raw.mapa_oscuro else R.raw.mapa_claro
        )
    }

    val posicionCamara = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(
            LatLng(
                estado?.lat ?: 0.0, estado?.long ?: 0.0,
            ), 15f
        )
    }
    val otherMarkerState = rememberUpdatedMarkerState(
        position = LatLng(estado?.lat ?: 0.0, estado?.long ?: 0.0)
    )

    LaunchedEffect(estado?.lat, estado?.long) {
        if (estado?.lat != 0.0 || estado?.long != 0.0) {
            posicionCamara.animate(
                CameraUpdateFactory.newLatLngZoom(
                    LatLng(estado?.lat ?: 4.46, estado?.long ?: -74.2444), 15f
                )
            )

        }
    }
    LaunchedEffect(id) {
        modelo.ubicar(id)
    }

    Scaffold(
        topBar = { DefaultTopAppBar("Monitoreo y ubicación del deportista") },
        bottomBar = { DefaultBottomBarEnt(R.color.rojoGranada, navController) }
    ) { paddingValues ->
        Box(
            modifier = Modifier.fillMaxSize().padding(paddingValues)
        ) {
            GoogleMap(
                modifier = Modifier.matchParentSize(),
                cameraPositionState = posicionCamara,
                properties = MapProperties(mapStyleOptions = estiloMapa),
                uiSettings = MapUiSettings(zoomControlsEnabled = true)
            ) {
                if (estado!!.lat != 0.0 || estado!!.long != 0.0) {
                    Marker(
                        state = otherMarkerState,
                        title = estado?.nombre ?: "Usuario elegido",
                        icon = bitmapDescriptorFromVector(
                            contexto,
                            R.drawable.outline_directions_walk_24
                        )
                    )
                }
            }

            ElevatedCard(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth(0.75f),
                colors = CardDefaults.cardColors(
                    containerColor = colorResource(R.color.pink).copy(alpha = 0.85f)
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(15.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(5.dp)
                        ) {
                            Text(
                                estado?.nombre ?: "Usuario Seleccionado",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text("Actividad: ${estado?.tipoActividad}", fontSize = 12.sp)
                            Text("Destino: ${estado?.lugarFinal}", fontSize = 12.sp)
                        }
                    }

                }
            }
        }
    }
}
