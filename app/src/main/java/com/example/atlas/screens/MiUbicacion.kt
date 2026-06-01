package com.example.atlas.screens

import android.Manifest
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.content.pm.PackageManager
import android.hardware.SensorManager
import android.net.Uri
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.atlas.R
import com.example.atlas.elements.DefaulButton
import com.example.atlas.elements.DefaultBottomBarDep
import com.example.atlas.elements.DefaultTopAppBar
import com.example.atlas.navegation.AppScreens
import com.example.atlas.sensorManager
import com.example.atlas.viewmodels.CrearSesionViewModel
import com.example.atlas.viewmodels.ModeloMiUbicacion
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.*
import kotlinx.coroutines.delay
import org.json.JSONObject

@Composable
fun MiUbicacion(
    controller: NavController,
    final: String = "",
    actividad: String = "",
    modelo: ModeloMiUbicacion = viewModel(),
    modeloSesion: CrearSesionViewModel = viewModel()
) {
    val estado by modelo.estado.collectAsState()
    val sesionState by modeloSesion.uiState.collectAsState()
    val contexto = LocalContext.current
    val idSesionGrande = sesionState.idSesionActiva
    Log.i("DEBUG_SESION", "MiUbicacion lee idSesion: '$idSesionGrande'")

    var placeUrl by remember { mutableStateOf<String?>(null) }

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
            else -> "Muy caliente — ten cuidado"
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
                modelo.establecerOrigen(location.latitude, location.longitude)
            }
            modelo.actualizarPosicion(contexto, location.latitude, location.longitude)
            modelo.resolverDireccion(contexto, location.latitude, location.longitude)
            modelo.compartirUbicacion(location.latitude,location.longitude)
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
        onDispose { locationClient.removeLocationUpdates(locationCallback)
            modelo.descompartirUbicacion()
        }
    }

    LaunchedEffect(final) {
        if (final.isNotBlank()) {
            modelo.limpiarRuta()
            modelo.resolverDestino(final)
        }
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

    LaunchedEffect(Unit) {
        loadPlacePhoto(contexto, final) { photoUrl ->
            placeUrl = photoUrl
        }
    }

    Scaffold(
        topBar = { DefaultTopAppBar("Monitoreo y ubicación de tu actividad") },
        bottomBar = { DefaultBottomBarDep(R.color.white, controller) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = paddingValues.calculateBottomPadding())
        ) {
            GoogleMap(
                modifier = Modifier.matchParentSize(),
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
                        snippet = "Ubicación actual"
                    )
                }

                if (estado.puntosRuta.isNotEmpty()) {
                    Polyline(
                        points = estado.puntosRuta,
                        width = 10f,
                        color = colorResource(R.color.rojoGranada)
                    )
                }

                estado.posicionDestino?.let {
                    Marker(
                        state = MarkerState(position = it),
                        title = "Destino",
                        snippet = final
                    )
                }
            }

            ElevatedCard(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(
                        start = 8.dp,
                        top = paddingValues.calculateTopPadding() + 8.dp
                    )
                    .size(150.dp),
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.Transparent
                )
            ) {
                AsyncImage(
                    model = placeUrl,
                    contentDescription = final,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
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
                            Text(estado.direccionActual, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                            Text("Distancia: ${estado.distanciaRecorrida} km", fontSize = 12.sp)
                            Text("Actividad: $actividad", fontSize = 12.sp)
                            Text("Tiempo: ${formatearTiempo(tiempoSegundos)}", fontSize = 12.sp)
                            Text(
                                "${"%.1f".format(temperaturaActual)}°C",
                                fontSize = 12.sp
                            )
                            Text("— ${condicionClima(temperaturaActual)}")
                        }
                    }

                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        DefaulButton("Finalizar actividad", 220, 40) {
                            modelo.actualizarTiempo(tiempoSegundos)
                            modelo.finalizarYGuardarSesion(idSesionGrande, tiempoSegundos, final, actividad)

                            controller.navigate(route = AppScreens.CrearNuevaSesion.name)
                        }
                    }
                }
            }
        }
    }
}

fun loadPlacePhoto(context: Context, placeName: String, onSuccess: (String) -> Unit) {
    val apiKey = context.packageManager
        .getApplicationInfo(context.packageName, PackageManager.GET_META_DATA)
        .metaData
        .getString("com.google.android.geo.API_KEY") ?: ""

    val url = "https://maps.googleapis.com/maps/api/place/findplacefromtext/json" +
            "?input=$placeName" +
            "&fields=photos" +
            "&inputtype=textquery" +
            "&key=$apiKey"

    val queue = Volley.newRequestQueue(context)

    val request = StringRequest(
        url,
        { response ->
            try {
                Log.i("PLACES", response)

                val json = JSONObject(response)

                val candidates = json.optJSONArray("candidates")

                if (candidates == null || candidates.length() == 0) {
                    Log.e("PLACES", "No se encontraron candidatos para '$placeName'")
                    return@StringRequest
                }

                val candidate = candidates.optJSONObject(0)

                val photos = candidate?.optJSONArray("photos")

                if (photos == null || photos.length() == 0) {
                    Log.e("PLACES", "El lugar no tiene fotos")
                    return@StringRequest
                }

                val photoReference = photos
                    .getJSONObject(0)
                    .getString("photo_reference")

                val imageUrl = "https://maps.googleapis.com/maps/api/place/photo" +
                        "?maxwidth=400" +
                        "&photo_reference=$photoReference" +
                        "&key=$apiKey"

                onSuccess(imageUrl)

            } catch (e: Exception) {
                Log.e("PLACES", "Error procesando respuesta", e)
            }
        },
        { error ->
            Log.e("PLACES", "Error Volley", error)
        }
    )

    queue.add(request)
}