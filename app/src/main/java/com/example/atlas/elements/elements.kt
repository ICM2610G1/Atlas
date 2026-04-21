package com.example.atlas.elements

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import com.example.atlas.R
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.atlas.auth
import com.example.atlas.navegation.AppScreens
import kotlin.random.Random

@Composable
fun ScaffoldDesign (titulo : String, color_id : Int , controller : NavController){
 Scaffold(topBar = ({DefaultTopAppBar(titulo)}), bottomBar = {DefaultBottomBarEnt(color_id,controller)})
 {paddingValues -> Column (verticalArrangement = Arrangement.Center,
     horizontalAlignment = Alignment.CenterHorizontally,
     modifier = Modifier.padding(paddingValues).fillMaxSize()){
     DefaulButton("Hola",240,40) {
         Log.i("TAG","Clickeado")
     }

 }


 }
}
@Composable
@Preview (showBackground = true)
fun ScaffolView (){
    val nc = rememberNavController();
    ScaffoldDesign("Home",R.color.pink,nc)

}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DefaultTopAppBar (nombre : String ){
   TopAppBar({Box(
       modifier = Modifier.fillMaxHeight(),
       contentAlignment = Alignment.CenterStart
   ) {
       Text(nombre,color=Color.White, fontWeight = FontWeight.Black)
   }},
        colors = TopAppBarColors(
            containerColor = colorResource(R.color.rojoGranada),
            scrolledContainerColor= Color.White,
            navigationIconContentColor =Color.White,
            titleContentColor = Color.White ,
            actionIconContentColor =Color.White ,
        ),

        modifier = Modifier.height(100.dp).clip(shape =  RoundedCornerShape(bottomStart = 25.dp, bottomEnd = 25.dp))
   )
}
@Composable
fun DefaultBottomBarDep (colorId : Int, controller: NavController ){
                BottomAppBar(contentPadding = PaddingValues(horizontal = 50.dp),
                    containerColor = colorResource(colorId),

                    actions = {

                        IconButton(onClick = {controller.navigate(route =AppScreens.Home.name)}, modifier = Modifier.padding(horizontal = 20.dp)) {
                            Icon(
                                Icons.Outlined.Home,
                                contentDescription = "Home",
                            )
                        }
                        IconButton(onClick = { controller.navigate(route= AppScreens.Perfil.name) }, modifier = Modifier.padding(horizontal = 30.dp)) {
                            Icon(
                                Icons.Outlined.Person,
                                contentDescription = "Perfil",
                            )
                        }
                        IconButton(onClick = { controller.navigate(route = AppScreens.Calificar.name) } , modifier = Modifier.padding(horizontal = 10.dp)) {
                            Icon(
                                Icons.Outlined.Star,
                                contentDescription = "Estrella",
                            )
                        }
                    },

                )
            }
@Composable
fun DefaultBottomBarEnt (colorId : Int, controller: NavController ){
    BottomAppBar(contentPadding = PaddingValues(horizontal = 100.dp),
        containerColor = colorResource(colorId),

        actions = {

            IconButton(onClick = {controller.navigate(route =AppScreens.HomeCoach.name)}, modifier = Modifier.padding(horizontal = 20.dp)) {
                Icon(
                    Icons.Outlined.Home,
                    contentDescription = "Home",
                )
            }
            IconButton(onClick = { controller.navigate(route= AppScreens.PerfilEnt.name) }, modifier = Modifier.padding(horizontal = 30.dp)) {
                Icon(
                    Icons.Outlined.Person,
                    contentDescription = "Perfil",
                )
            }

        },

        )
}





@Composable
fun DefaulButton (text : String  , ancho : Int , alto : Int , funcion : () -> Unit ){
    Button(
        modifier = Modifier.width(ancho.dp).height(alto.dp),
        onClick = funcion ,
        colors = ButtonColors(
            colorResource(R.color.rojoGranada),
            contentColor = Color.White,
            disabledContainerColor = Color.Gray,
            disabledContentColor = Color.Black,
        )
    ) {
        Text(text)
    }

}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DefaultTopAppBarHome (nombre : String , controller: NavController){

    TopAppBar({Box(
        modifier = Modifier.fillMaxHeight(),
        contentAlignment = Alignment.CenterStart,

        ) {
        Text(nombre,color=Color.White, fontWeight = FontWeight.Black)
    }},
        colors = TopAppBarColors(
            containerColor = colorResource(R.color.rojoGranada),
            scrolledContainerColor= Color.White,
            navigationIconContentColor =Color.White,
            titleContentColor = Color.White ,
            actionIconContentColor =Color.White ,
        ),

        modifier = Modifier.height(100.dp).clip(shape =  RoundedCornerShape(bottomStart = 25.dp, bottomEnd = 25.dp)),
        actions = {
            IconButton(onClick = {
                auth.signOut()
                controller.navigate(AppScreens.LogIn.name){
                    popUpTo(AppScreens.Home.name){
                        inclusive = true
                    }
                }
            }
            ) {Icon(Icons.Default.ExitToApp, contentDescription = null) }
        }
    )
}





// Existe el  CenterAlignedTopAppBar para centra el texto
