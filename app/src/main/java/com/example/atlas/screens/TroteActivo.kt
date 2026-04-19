package com.example.atlas.screens

import android.Manifest
import android.content.pm.PackageManager
import android.os.Looper
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.atlas.R
import com.example.atlas.elements.DefaultBottomBarEnt
import com.example.atlas.elements.DefaultTopAppBar
import com.example.atlas.modelos.EstadoDeportista
import com.example.atlas.navegation.AppScreens
import com.example.atlas.viewmodels.ModeloTrote
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.location.LocationRequest

val locationPermission = Manifest.permission.ACCESS_FINE_LOCATION

@Composable
fun TroteActivo(
    controller: NavController,
    modelo: ModeloTrote = viewModel()
) {
    val estado by modelo.estado.collectAsState()
    val contexto = LocalContext.current

    SideEffect {
        if (estado.deportistas.isEmpty()) {
            modelo.cargarDeportistas()
        }
    }

    val locationClient = LocationServices.getFusedLocationProviderClient(contexto)

    val locationRequest = LocationRequest.Builder(
        Priority.PRIORITY_HIGH_ACCURACY, 10000L
    )
        .setWaitForAccurateLocation(true)
        .setMinUpdateIntervalMillis(5000L)
        .build()

    val locationCallback = createLocationCallback { result: LocationResult ->
        result.lastLocation?.let { location ->
            modelo.actualizarUbicacion(0, location.latitude, location.longitude)
            modelo.resolverDireccion(contexto, 0, location.latitude, location.longitude)
        }
    }

    DisposableEffect(Unit) {
        if (ContextCompat.checkSelfPermission(
                contexto, locationPermission
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            locationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
        }
        onDispose {
            locationClient.removeLocationUpdates(locationCallback)
        }
    }

    Scaffold(
        topBar = { DefaultTopAppBar(nombre = "Seguimiento en vivo") },
        bottomBar = {
            DefaultBottomBarEnt(
                colorId = R.color.white,
                controller = controller
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.puma),
                    contentDescription = "Mascota Puma",
                    modifier = Modifier.size(150.dp),
                    alignment = Alignment.TopStart
                )
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.burbuja),
                        contentDescription = "DialogoPuma",
                        tint = colorResource(R.color.rojoGranada),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text(
                        text = "Ubica a los deportistas\na tu cargo mientras\ntrotan",
                        color = colorResource(R.color.rojoGranada),
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }

            estado.deportistas.chunked(2).forEach { fila ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    fila.forEach { deportista ->
                        CardDeportista(
                            deportista = deportista,
                            modifier = Modifier
                                .weight(1f)
                                .clickable {
                                    controller.navigate(route = AppScreens.Ubicacion.name)
                                }
                        )
                    }
                    if (fila.size == 1) Spacer(modifier = Modifier.weight(1f))
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun CardDeportista(deportista: EstadoDeportista, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(contentAlignment = Alignment.BottomEnd) {
                Icon(
                    imageVector = Icons.Filled.Person,
                    contentDescription = "Foto de perfil",
                    tint = Color.Gray,
                    modifier = Modifier.size(80.dp)
                )
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .background(
                            color = if (deportista.enLinea) Color(0xFF27AE60) else Color.Gray,
                            shape = androidx.compose.foundation.shape.CircleShape
                        )
                        .border(
                            width = 2.dp,
                            color = Color.White,
                            shape = androidx.compose.foundation.shape.CircleShape
                        )
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = deportista.nombre,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Ubicación:",
                color = Color.Gray,
                fontSize = 12.sp,
                textAlign = TextAlign.Center
            )
            Text(
                text = deportista.direccion,
                color = Color.Gray,
                fontSize = 12.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}

fun createLocationCallback(
    onLocationChange: (LocationResult) -> Unit
): LocationCallback {
    val callback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)
            onLocationChange(locationResult)
        }
    }
    return callback
}

@Preview(showBackground = true)
@Composable
fun PreviewTroteActivo() {
    TroteActivo(rememberNavController())
}