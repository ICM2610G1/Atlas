package com.example.atlas.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import androidx.navigation.NavController
import com.example.atlas.R
import com.example.atlas.elements.DefaultBottomBarEnt
import com.example.atlas.elements.DefaultTopAppBar

@Composable
fun Ubicacion (controller: NavController){
    Scaffold(
        topBar = { DefaultTopAppBar("Monitoreo y ubicación del deportista") },
        bottomBar = { DefaultBottomBarEnt(R.color.rojoGranada, controller) }
    ) {paddingValues ->
        Column(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            Box(modifier = Modifier.weight(1f).padding(horizontal = 30.dp, vertical =15.dp)) {
                Image(
                    painterResource(R.drawable.mapatemporal),
                    "Mapa",
                    modifier = Modifier.fillMaxSize(),

                    contentScale = ContentScale.Crop
                )
                Icon(Icons.Default.LocationOn, "Ubicación", modifier = Modifier.align(alignment = Alignment.TopStart).size(100.dp), tint=colorResource(R.color.rojoGranada))
                Icon(Icons.Default.ArrowDropDown, "Objetivo", modifier = Modifier.align(alignment = Alignment.BottomStart).size(200.dp), tint= colorResource(R.color.teal_700)
                )
            }
            ElevatedCard(
                modifier = Modifier.padding(vertical = 15.dp, horizontal = 30.dp),
                colors= CardDefaults.cardColors(colorResource(R.color.pink))
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.Start),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth().padding(15.dp)
                ) {
                    Box {
                        Icon(
                            Icons.Default.AccountCircle,
                            "Simbolo de persona",
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
                        verticalArrangement = Arrangement.spacedBy(
                            5.dp,
                            Alignment.CenterVertically
                        ),
                        horizontalAlignment = Alignment.Start
                    ) {
                        Text("Nombre", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        Text("Distancia recorrida: X km", fontSize = 12.sp)
                        Text("Tiempo: 00:00 min", fontSize = 12.sp)
                    }
                }
            }
        }
    }


}
