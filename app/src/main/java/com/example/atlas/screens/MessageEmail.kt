package com.example.atlas.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.atlas.R
import com.example.atlas.elements.DefaulButton
import com.example.atlas.navegation.AppScreens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessageEmail(controller: NavController) {
    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("",color = colorResource(R.color.white))},
                    colors = TopAppBarDefaults.topAppBarColors(colorResource(R.color.white)),
                    modifier = Modifier.padding(vertical = 250.dp).padding(horizontal = 10.dp))},
            containerColor = colorResource(R.color.white)
        ) { paddingValues ->
            Column(
                modifier = Modifier.fillMaxSize().padding(paddingValues).fillMaxHeight(0.989f)
                    .clip(RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp))
                    .background(colorResource(R.color.pink)).padding(vertical = 40.dp)
                    .padding(horizontal = 25.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Verifica tu correo 📨", fontSize = 19.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(10.dp))
                Text("Te hemos enviado un correo electrónico ¿No lo recibiste? Revisa tu carpeta de correo no deseado o inténtalo de nuevo.",fontSize = 12.sp, modifier = Modifier.padding(20.dp))
                DefaulButton("Ok",380,40){
                    Log.i("TAGOkEmail", "Click Ok Email")
                    controller.navigate(route = AppScreens.LogIn.name)
                }
            }
        }
        Image(
            painter = painterResource(id = R.drawable.panter_carta),
            contentDescription = "Panter con carta",
            modifier = Modifier
                .fillMaxHeight()
                .align(Alignment.CenterEnd)
                .padding(top = 185.dp, end = 50.dp, )
        )
    }
}

@Preview (showBackground = true)
@Composable
fun MessageEmailPreview (){
    val nc = rememberNavController()
    MessageEmail(nc)
}
