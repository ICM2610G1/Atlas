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
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.atlas.R
import com.example.atlas.elements.DefaultBottomBarEnt
import com.example.atlas.elements.DefaultTopAppBar
import com.example.atlas.viewmodels.ModeloUbicacion
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

@Composable
fun Ubicacion(
    navController: NavController,
    modelo: ModeloUbicacion = viewModel()
) {
    val estado by modelo.estado.collectAsState()
    val contexto = LocalContext.current

    // Sensor de luminosidad
    val sensorManager = contexto.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    val sensorLuz: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)
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
        sensorManager.registerListener(listenerLuz, sensorLuz, SensorManager.SENSOR_DELAY_NORMAL)
        onDispose { sensorManager.unregisterListener(listenerLuz) }
    }

    val mapViewRef = remember { mutableStateOf<MapView?>(null) }

    // Solo cambia la capa cuando cambia esOscuro
    LaunchedEffect(esOscuro) {
        mapViewRef.value?.let { mapView ->
            mapView.setTileSource(
                if (esOscuro) TileSourceFactory.USGS_TOPO else TileSourceFactory.MAPNIK
            )
            mapView.invalidate()
        }
    }

    Scaffold(
        topBar = { DefaultTopAppBar("Monitoreo y ubicación del deportista") },
        bottomBar = { DefaultBottomBarEnt(R.color.rojoGranada, navController) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 30.dp, vertical = 15.dp)
            ) {
                AndroidView(
                    modifier = Modifier.fillMaxSize(),
                    factory = { ctx ->
                        Configuration.getInstance().load(
                            ctx,
                            ctx.getSharedPreferences("osmdroid", Context.MODE_PRIVATE)
                        )
                        Configuration.getInstance().userAgentValue = ctx.packageName
                        MapView(ctx).apply {
                            setTileSource(TileSourceFactory.MAPNIK)
                            setMultiTouchControls(true)
                            controller.setZoom(15.0)
                            // Guardar referencia para LaunchedEffect del sensor
                            mapViewRef.value = this
                        }
                    },
                    update = { mapView ->
                        if (estado.latitud != 0.0 || estado.longitud != 0.0) {
                            val posicion = GeoPoint(estado.latitud, estado.longitud)
                            mapView.controller.setCenter(posicion)
                            mapView.overlays.clear()
                            val marcador = Marker(mapView).apply {
                                position = posicion
                                title = estado.nombreDeportista
                                snippet = estado.direccion
                                setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                            }
                            mapView.overlays.add(marcador)
                            mapView.invalidate()
                        }
                    }
                )
            }

            ElevatedCard(
                modifier = Modifier.padding(vertical = 15.dp, horizontal = 30.dp),
                colors = CardDefaults.cardColors(containerColor = colorResource(R.color.pink))
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
                        Text(estado.nombreDeportista, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        Text("Ubicación: ${estado.direccion}", fontSize = 12.sp)
                        Text("Distancia recorrida: ${estado.distanciaKm} km", fontSize = 12.sp)
                    }
                }
            }
        }
    }
}