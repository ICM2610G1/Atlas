package com.example.atlas.screens

import android.util.Log
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
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.atlas.R
import com.example.atlas.elements.DefaulButton
import com.example.atlas.elements.DefaultBottomBarEnt
import com.example.atlas.elements.DefaultTopAppBar
import com.example.atlas.navegation.AppScreens


@Composable
fun HomeCoach(controller : NavController) {
    Scaffold(
        topBar = { DefaultTopAppBar("Home") },
        bottomBar = { (DefaultBottomBarEnt(R.color.pink, controller)) }) { paddingValues ->
        Column(
            verticalArrangement = Arrangement.spacedBy(40.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize().padding(paddingValues)
        ) {

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically,

                ) {

                icono_circulo(R.drawable.mensajero, "Chats")
                DefaulButton("Chats", 220, 40) {
                    Log.i("TAGChats", "Click Chat")
                    controller.navigate(route = AppScreens.Chats.name)
                }
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically,

                ) {

                icono_circulo(R.drawable.icon_ojo, "VerSesiones")
                DefaulButton("VerSesiones", 220, 40) {
                    Log.i("TAGVerSesiones", "Click VerSesiones")
                    controller.navigate(route = AppScreens.vistaSesionesEnt.name)
                }
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically,

                ) {

                icono_circulo(R.drawable.mapa, "Seguimiento en Vivo")
                DefaulButton("Seguimiento en vivo", 220, 40) {
                    Log.i("TAGSeguimineto", "Click Seguimineto en vivo ")
                    controller.navigate(route = AppScreens.troteActivo.name)
                }
            }
            Row(
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier.fillMaxWidth().height(250.dp)
            ) {
                Image(
                    painter = painterResource(R.drawable.puma),
                    contentDescription = "LogoAtlas",
                    contentScale = ContentScale.FillHeight,
                    modifier = Modifier.fillMaxHeight()

                )
                Box {
                    Icon(
                        painter = painterResource(R.drawable.burbuja),
                        contentDescription = "DialogoPuma",
                        tint = colorResource(R.color.rojoGranada)
                    )
                    Text(
                        "Qué elegirás hoy? ", color = colorResource(R.color.rojoGranada),
                        fontSize = (18.sp),
                        modifier = Modifier.padding(vertical = 80.dp, horizontal = 25.dp)
                    )
                }


            }
        }

    }
}
@Preview (showBackground = true)
@Composable
fun PreviewHomeCoach () {
    val nc = rememberNavController()
    HomeCoach(nc)

}
