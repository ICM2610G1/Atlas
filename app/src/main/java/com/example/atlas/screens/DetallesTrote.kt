package com.example.atlas.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.atlas.R
import com.example.atlas.elements.DefaultBottomBarDep
import com.example.atlas.elements.DefaultTopAppBar
import com.example.atlas.navegation.AppScreens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetallesTrote(controller: NavController) {

    val rojoGranada = colorResource(R.color.rojoGranada)

    Scaffold(
        containerColor = colorResource(R.color.pink),
        topBar = { DefaultTopAppBar("Detalles de trote") },
        bottomBar = { DefaultBottomBarDep(R.color.white, controller) }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Top
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .padding(top = 8.dp, bottom = 12.dp),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.Start
            ) {
                Image(
                    painter = painterResource(R.drawable.puma),
                    contentDescription = "Puma",
                    contentScale = ContentScale.FillHeight,
                    modifier = Modifier.fillMaxHeight()
                )

                Box(
                    modifier = Modifier
                        .size(250.dp)
                ) {

                    Image(
                        painter = painterResource(R.drawable.burbujadialogo),
                        contentDescription = "Dialogo",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.FillBounds
                    )

                    Text(
                        text = "¡Rugido de victoria! Has terminado",
                        color = rojoGranada,
                        fontSize = 18.sp,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(horizontal = 32.dp, vertical = 60.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }

            InfoCardAtlas(
                icon = {
                    Icon(
                        Icons.Outlined.ShoppingCart,
                        contentDescription = null,
                        tint = rojoGranada
                    )
                },
                title = "Hora de inicio",
                subtitle = "6:30 AM",
                accent = rojoGranada,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            InfoCardAtlas(
                icon = {
                    Icon(
                        Icons.Outlined.ShoppingCart,
                        contentDescription = null,
                        tint = rojoGranada
                    )
                },
                title = "Duración",
                subtitle = "75 minutos",
                accent = rojoGranada,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            InfoCardAtlas(
                icon = {
                    Icon(
                        Icons.Outlined.LocationOn,
                        contentDescription = null,
                        tint = rojoGranada
                    )
                },
                title = "Lugar de inicio",
                subtitle = "Parque Virrey (Carrera 15)",
                accent = rojoGranada,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            InfoCardAtlas(
                icon = {
                    Icon(
                        Icons.Outlined.LocationOn,
                        contentDescription = null,
                        tint = rojoGranada
                    )
                },
                title = "Lugar final",
                subtitle = "Parque de la 93",
                accent = rojoGranada,
                modifier = Modifier.padding(bottom = 20.dp)
            )

            Button(
                onClick = {controller.navigate(route= AppScreens.crearSesion.name)},
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp)
                    .padding(bottom = 24.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = rojoGranada,
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = "Confirmar",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
private fun InfoCardAtlas(
    icon: @Composable () -> Unit,
    title: String,
    subtitle: String,
    accent: Color,
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = Color.White),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(CircleShape),
                contentAlignment = Alignment.Center
            ) {
                icon()
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 14.dp)
            ) {
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    fontSize = 16.sp
                )
                Text(
                    text = subtitle,
                    color = Color.Gray,
                    fontSize = 13.sp
                )
            }
        }
    }
}