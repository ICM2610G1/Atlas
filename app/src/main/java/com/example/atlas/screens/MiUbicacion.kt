package com.example.atlas.screens

import android.Manifest
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.content.pm.PackageManager
import android.hardware.SensorManager
import android.os.Looper
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.atlas.R
import com.example.atlas.elements.DefaulButton
import com.example.atlas.elements.DefaultBottomBarDep
import com.example.atlas.elements.DefaultTopAppBar
import com.example.atlas.navegation.AppScreens
import com.example.atlas.sensorManager
import com.example.atlas.viewmodels.ModeloMiUbicacion
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.*
import kotlinx.coroutines.delay

@Composable
fun MiUbicacion(
    navController: NavController,
    inicio: String="",
    final: String="",
    actividad: String="",
    modelo: ModeloMiUbicacion = viewModel()
) {
    val estado by modelo.estado.collectAsState()
    val contexto = LocalContext.current

    // Sensor de luminosidad
    val sensorLuz = remember {
        sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)
    }
    var esOscuro by remember { mutableStateOf(false) }

    val listenerLuz = remember {
        object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent?) {
                if (event?.sensor?.type == Sensor.TYPE_LIGHT) {
                    esOscuro = event.values[0] < 2000
                }
            }
            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }
    }



    DisposableEffect(Unit) {
        sensorManager.registerListener(listenerLuz, sensorLuz, SensorManager.SENSOR_DELAY_NORMAL)
        onDispose { sensorManager.unregisterListener(listenerLuz) }
    }

    val estiloMapa = remember(esOscuro) {
        MapStyleOptions.loadRawResourceStyle(
            contexto,
            if (esOscuro) R.raw.mapa_oscuro else R.raw.mapa_claro
        )
    }

    val sensorTemperatura = remember {
        sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE)
    }
    var temperaturaActual by remember { mutableStateOf(0f) }
    val sumaTemperaturas = remember { mutableFloatStateOf(0f) }
    val contadorTemperaturas = remember { mutableIntStateOf(0) }

    val listenerTemperatura = remember {
        object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent?) {
                if (event?.sensor?.type == Sensor.TYPE_AMBIENT_TEMPERATURE) {
                    val temp = event.values[0]
                    temperaturaActual = temp
                    sumaTemperaturas.floatValue += temp
                    contadorTemperaturas.intValue++
                    modelo.actualizarTemperatura(
                        temp,
                        sumaTemperaturas.floatValue / contadorTemperaturas.intValue
                    )
                }
            }
            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }
    }

    DisposableEffect(Unit) {
        sensorManager.registerListener(listenerTemperatura, sensorTemperatura, SensorManager.SENSOR_DELAY_NORMAL)
        onDispose { sensorManager.unregisterListener(listenerTemperatura) }
    }

    val sensorPresion = remember {
        sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE)
    }
    var lastRecordTime by remember { mutableStateOf(0L) }
    val INTERVALO_MS = 5000L

    val listenerPresion = remember {
        object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent?) {
                if (event?.sensor?.type == Sensor.TYPE_PRESSURE) {
                    val altitud = SensorManager.getAltitude(
                        SensorManager.PRESSURE_STANDARD_ATMOSPHERE,
                        event.values[0]
                    )
                    val ahora = System.currentTimeMillis()
                    if (ahora - lastRecordTime >= INTERVALO_MS) {
                        modelo.agregarPuntoElevacion(estado.distanciaRecorrida, altitud)
                        lastRecordTime = ahora
                    }
                }
            }
            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }
    }
    DisposableEffect(Unit) {
        sensorManager.registerListener(listenerPresion, sensorPresion, SensorManager.SENSOR_DELAY_NORMAL)
        onDispose { sensorManager.unregisterListener(listenerPresion) }
    }

    var tiempoSegundos by remember { mutableStateOf(0) }
    LaunchedEffect(Unit) {
        while (true) {
            delay(1000L)
            tiempoSegundos++
        }
    }
    fun formatearTiempo(segundos: Int): String {
        val h = segundos / 3600
        val m = (segundos % 3600) / 60
        val s = segundos % 60
        return "%02d:%02d:%02d".format(h, m, s)
    }
    fun condicionClima(temp: Float): String {
        return when {
            temp < 10f -> "La temperatura es bajo, abrigate"
            temp < 18f -> "Fresco — ideal para trotar"
            temp < 25f -> "Agradable"
            temp < 32f -> "Calor — hidrátate seguido"
            else       -> "Muy caliente — ten cuidado"
        }
    }

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

    val lanzadorPermisoUbicacion = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { concedido ->
        if (!concedido) {
            Toast.makeText(
                contexto,
                "Se requiere permiso de ubicación para monitorear la actividad",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    LaunchedEffect(Unit) {
        if (ContextCompat.checkSelfPermission(
                contexto, Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            lanzadorPermisoUbicacion.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    DisposableEffect(Unit) {
        if (ContextCompat.checkSelfPermission(
                contexto, Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            locationClient.requestLocationUpdates(
                locationRequest, locationCallback, Looper.getMainLooper()
            )
        }
        onDispose { locationClient.removeLocationUpdates(locationCallback) }
    }

    LaunchedEffect(inicio, final) {
        if (inicio.isNotBlank()) modelo.resolverOrigen(inicio)
        if (final.isNotBlank()) modelo.resolverDestino(final)
    }

    val posicionCamara = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(4.627293, -74.063228), 13f)
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

    LaunchedEffect(estado.posicionOrigen, estado.posicionDestino) {
        if (estado.posicionOrigen != null && estado.posicionDestino != null) {
            modelo.calcularRuta(contexto)
        }
    }

    Scaffold(
        topBar = { DefaultTopAppBar("Monitoreo y ubicación de tu actividad") },
        bottomBar = { DefaultBottomBarDep(R.color.rojoGranada, navController) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Box(
                modifier = Modifier
                    .weight(2f)
                    .padding(horizontal = 30.dp, vertical = 15.dp)
            ) {
                GoogleMap(
                    modifier = Modifier.fillMaxSize(),
                    cameraPositionState = posicionCamara,
                    properties = MapProperties(
                        mapStyleOptions = estiloMapa,
                        isMyLocationEnabled = ContextCompat.checkSelfPermission(
                            contexto, Manifest.permission.ACCESS_FINE_LOCATION
                        ) == PackageManager.PERMISSION_GRANTED
                    ),
                    uiSettings = MapUiSettings(
                        zoomControlsEnabled = true,
                        myLocationButtonEnabled = true
                    )
                ) {
                    if (estado.latitud != 0.0 || estado.longitud != 0.0) {
                        Marker(
                            state = MarkerState(
                                position = LatLng(estado.latitud, estado.longitud)
                            ),
                            title = "Mi ubicación",
                            snippet = estado.direccionActual
                        )
                    }

                    estado.posicionOrigen?.let {
                        Marker(
                            state = MarkerState(position = it),
                            title = "Origen",
                            snippet = inicio
                        )
                    }

                    if (estado.puntosRuta.isNotEmpty()) {
                        Polyline(points = estado.puntosRuta, width = 10f, color = colorResource(R.color.rojoGranada))
                    }
                    
                    estado.posicionDestino?.let {
                        Marker(
                            state = MarkerState(position = it),
                            title = "Destino",
                            snippet = final
                        )
                    }
                }
            }

            ElevatedCard(
                modifier = Modifier
                    .padding(vertical = 15.dp, horizontal = 30.dp)
                    .weight(1f),
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
                        Text(estado.direccionActual, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        Text("Distancia recorrida: ${estado.distanciaRecorrida} km", fontSize = 12.sp)
                        Text("Actividad: $actividad", fontSize = 12.sp)
                        Text("Tiempo: ${formatearTiempo(tiempoSegundos)}", fontSize = 12.sp)
                        Text("${"%.1f".format(temperaturaActual)}°C — ${condicionClima(temperaturaActual)}", fontSize = 12.sp)
                    }
                }
            }

            Column(
                modifier = Modifier.weight(1f).fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                DefaulButton("Finalizar actividad", 220, 40) {
                    navController.navigate(route = AppScreens.CrearNuevaSesion.name)
                    modelo.actualizarTiempo(tiempoSegundos)
                }
            }
        }
    }
}

