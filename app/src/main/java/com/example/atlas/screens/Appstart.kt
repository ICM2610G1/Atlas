package com.example.atlas.screens


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.atlas.R
import androidx.navigation.compose.rememberNavController
import com.example.atlas.elements.DefaulButton
import com.example.atlas.navegation.AppScreens

@Composable
fun AppStart (controller : NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        colorResource(R.color.rojoOscuro),
                        colorResource(R.color.rojoOscuro),
                        colorResource(R.color.rojoOscuro),
                        Color.White,
                        Color.White


                    ),
                    start = Offset(0f, 0f),
                    end = Offset(1000f, 1000f)
                )
            ),
        verticalArrangement = Arrangement.spacedBy(40.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        Image(
            painter = painterResource(R.drawable.gan),
            contentDescription = "Atlas",
            contentScale = ContentScale.FillHeight,
            modifier = Modifier.height(500.dp)

        )
        DefaulButton("¡Comienza a entrenar, registrate!",300,40) {controller.navigate(route = AppScreens.SignUp.name)}
        Button(onClick = { controller.navigate(route = AppScreens.LogIn.name) },
            modifier = Modifier.width(300.dp).height(40.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = colorResource(R.color.rojoGranada)
            ),
            border = BorderStroke(1.dp,colorResource(R.color.rojoGranada))
        ) {
            Text("Ya tengo una cuenta")
        }
    }
}

@Preview (showBackground = true)
@Composable
fun Preview (){
    val nc = rememberNavController();
    AppStart(nc)

}

