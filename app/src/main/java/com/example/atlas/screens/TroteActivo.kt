package com.example.atlas.screens

import android.Manifest
import android.content.pm.PackageManager
import android.os.Looper
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
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
import coil.compose.AsyncImage
import com.example.atlas.R
import com.example.atlas.elements.DefaulButton
import com.example.atlas.elements.DefaultBottomBarEnt
import com.example.atlas.elements.DefaultTopAppBar
import com.example.atlas.modelos.EstadoDeportista
import com.example.atlas.navegation.AppScreens
import com.example.atlas.viewmodels.ModeloTrote
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult


@Composable
fun TroteActivo(controller: NavController, modelo: ModeloTrote = viewModel()) {
    val estado by modelo.deportistas.collectAsState()

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
                .padding(horizontal = 16.dp),
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

        LazyColumn(
            modifier = Modifier.padding(10.dp)
        ) {
            items(estado) { item ->
                ElevatedCard(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.elevatedCardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AsyncImage(
                            model = item.imagen,
                            contentDescription = "Foto",
                            modifier = Modifier.size(75.dp).clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                        Column(
                            modifier = Modifier.weight(1f).fillMaxWidth().padding(start = 10.dp),
                            verticalArrangement = Arrangement.Center,
                        ) {
                            Text(
                                text = item.nombre,
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp,
                                color = colorResource(R.color.rojoGranada)
                            )
                            Text(
                                text = "Usuario disponible",
                                fontSize = 13.sp,
                                color = colorResource(R.color.black),
                                modifier = Modifier.padding(top = 3.dp)
                            )
                            DefaulButton("Ver ubicacion", 240, 32) {
                                controller.navigate(route = AppScreens.Ubicacion.name + "/${item.id}")
                            }
                        }
                    }
                }
            }
            }
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