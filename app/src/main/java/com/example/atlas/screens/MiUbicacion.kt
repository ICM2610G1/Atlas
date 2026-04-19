package com.example.atlas.screens

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Looper
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
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.atlas.R
import com.example.atlas.elements.DefaulButton
import com.example.atlas.elements.DefaultBottomBarDep
import com.example.atlas.elements.DefaultTopAppBar
import com.example.atlas.navegation.AppScreens
import com.example.atlas.viewmodels.ModeloMiUbicacion
import com.google.android.gms.location.*
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

@Composable
fun MiUbicacion(
    navController: NavController,
    inicio: String,
    final: String,
    actividad: String,
    modelo: ModeloMiUbicacion = viewModel()
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

    // GPS del deportista
    val locationClient = LocationServices.getFusedLocationProviderClient(contexto)

    val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 10000L)
        .setWaitForAccurateLocation(true)
        .setMinUpdateIntervalMillis(5000L)
        .build()

    val locationCallback = createLocationCallback { result ->
        result.lastLocation?.let { location ->
            if (estado.latInicio == 0.0 && estado.lngInicio == 0.0) {
                modelo.registrarInicio(location.latitude, location.longitude)
            }
            modelo.actualizarPosicion(location.latitude, location.longitude)
            modelo.resolverDireccion(contexto, location.latitude, location.longitude)
        }
    }

    DisposableEffect(Unit) {
        if (ContextCompat.checkSelfPermission(
                contexto, Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            locationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
        }
        onDispose { locationClient.removeLocationUpdates(locationCallback) }
    }

    val mapViewRef = remember { mutableStateOf<MapView?>(null) }

    // Cambia la capa del mapa cuando cambia esOscuro
    LaunchedEffect(esOscuro) {
        mapViewRef.value?.let { mapView ->
            mapView.setTileSource(
                if (esOscuro) TileSourceFactory.USGS_TOPO else TileSourceFactory.MAPNIK
            )
            mapView.invalidate()
        }
    }

    // ── UI
    Scaffold(
        topBar = { DefaultTopAppBar("Monitoreo y ubicación de tu actividad") },
        bottomBar = { DefaultBottomBarDep(R.color.rojoGranada, navController) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {

            // Mapa OpenStreetMap
            Box(
                modifier = Modifier
                    .weight(2f)
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
                            controller.setZoom(13.0)
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
                                title = "Mi ubicación"
                                snippet = estado.direccionActual
                                setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                            }
                            mapView.overlays.add(marcador)
                            mapView.invalidate()
                        }
                    }
                )
            }

            // Tarjeta de datos
            ElevatedCard(
                modifier = Modifier
                    .padding(vertical = 15.dp, horizontal = 30.dp)
                    .weight(0.7f),
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
                        Text(
                            estado.direccionActual,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            "Distancia recorrida: ${estado.distanciaRecorrida} km",
                            fontSize = 12.sp
                        )
                        Text(
                            "Actividad: $actividad",
                            fontSize = 12.sp
                        )
                    }
                }
            }

            // Botón finalizar
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                DefaulButton("Finalizar actividad", 220, 40) {
                    navController.navigate(route = AppScreens.crearSesion.name)
                }
            }
        }
    }
}