package com.example.atlas.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
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
import com.example.atlas.R
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.atlas.elements.DefaulButton
import com.example.atlas.elements.DefaultBottomBarDep
import com.example.atlas.elements.DefaultTopAppBar
import com.example.atlas.elements.DefaultTopAppBarHome
import com.example.atlas.navegation.AppScreens


@Composable
fun Home (controller : NavController){
    Scaffold(topBar = { DefaultTopAppBarHome("Home",controller) }, bottomBar = {(DefaultBottomBarDep(R.color.pink,controller))}) {
            paddingValues ->
        Column(verticalArrangement = Arrangement.spacedBy(20.dp , Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize().padding(paddingValues).padding(vertical = 20.dp)) {

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1F)

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
                modifier = Modifier.weight(1F)

            ) {

                icono_circulo(R.drawable.historial, "Historial")
                DefaulButton("Historial", 220, 40) {
                    Log.i("TAGHistorial", "Click Historial")
                    controller.navigate(route = AppScreens.Historial.name)

                }
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1F)

            ) {

                icono_circulo(R.drawable.mapa, "Ubicacion")
                DefaulButton("Nueva Sesión", 220, 40) {
                    Log.i("TAGNuevaSesion", "Click NuevaSesion")
                    controller.navigate(route = AppScreens.CrearNuevaSesion.name)
                }
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1F)

            ) {

                icono_circulo(R.drawable.pesa, "Progreso")
                DefaulButton("Revisar Progreso", 220, 40) {
                    controller.navigate(route = AppScreens.Progreso.name)
                }
            }

            Row(verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier.fillMaxWidth().weight(4F).height(250.dp)) {
                Image(
                    painter = painterResource(R.drawable.puma),
                    contentDescription = "LogoAtlas",
                    contentScale = ContentScale.FillHeight,
                    modifier = Modifier.fillMaxHeight()

                )
                Box (modifier = Modifier.weight(1F)){
                    Icon(painter = painterResource(R.drawable.burbuja),
                        contentDescription = "DialogoPuma",
                        tint = colorResource(R.color.rojoGranada)
                    )
                    Text("Qué elegirás hoy? ", color = colorResource(R.color.rojoGranada),
                        fontSize = (15.sp),
                        modifier = Modifier.padding (vertical = 60.dp, horizontal = 25.dp))
                }



            }
        }

    }


}
@Preview (showBackground = true)
@Composable
fun PreviewHome (){
    val nc = rememberNavController()
    Home(nc)
}
@Composable
fun icono_circulo(imageId : Int ,cotenido : String) {
    Box(
        modifier = Modifier
            .size(50.dp)
            .border(
                width = 2.dp,
                color = colorResource(R.color.rojoGranada),
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(imageId),
            contentDescription = cotenido,
            tint = colorResource(R.color.rojoGranada)


        )

        // iconos creados por Freepik - Flaticon
    }

}