package com.example.atlas.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
fun RecoverPassword(controller: NavController){
    var input by remember { mutableStateOf("") }
    Box(modifier = Modifier.fillMaxSize()){
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Recuperar" + "\n" + "Contraseña",color = colorResource(R.color.white), fontWeight = FontWeight.Black)},
                    colors = TopAppBarDefaults.topAppBarColors(colorResource(R.color.rojoGranada)),
                    modifier = Modifier.padding(vertical = 40.dp).padding(horizontal = 10.dp))},
            containerColor = colorResource(R.color.rojoGranada)
        ) { paddingValues ->
            Column(modifier = Modifier.fillMaxSize().padding(paddingValues).fillMaxHeight(0.989f)
                .clip(RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp))
                .background(colorResource(R.color.pink)).padding(vertical = 40.dp).padding(horizontal = 25.dp),
                verticalArrangement = Arrangement.Top,
            ) {
                Text("Correo",
                    fontWeight = FontWeight.Medium,
                    fontSize = 19.sp)

                TextField(
                    value = input,
                    onValueChange = {input = it},
                    placeholder = {Text("ejemplo@email.com")},
                    modifier = Modifier.fillMaxWidth().padding(vertical = 5.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = colorResource(R.color.white),
                        focusedContainerColor = colorResource(R.color.white),
                        unfocusedPlaceholderColor = Color.Gray,
                        focusedPlaceholderColor = Color.Gray
                    )
                )
                Text("Introduce tu dirección de correo electrónico para recibir un enlace para restablecer tu contraseña.",
                    modifier = Modifier.fillMaxWidth().padding(vertical = 20.dp),
                    fontSize = 12.sp)

                Spacer(modifier = Modifier.weight(1f))
                DefaulButton("Siguiente", 380, 40) {
                    Log.i("TAGSiguiente", "Click Siguiente")
                    controller.navigate(route = AppScreens.MessageEmail.name)
                }
            }
        }
        Image(
            painter = painterResource(id = R.drawable.panter_sorprendido),
            contentDescription = "Pantera sorprendida",
            modifier = Modifier.size(148.dp).align(Alignment.TopEnd).padding(top = 30.dp, end = 45.dp)
        )
    }

}

@Preview (showBackground = true)
@Composable
fun PreviewRecoverPassword(){
    val nc = rememberNavController()
    RecoverPassword(nc)
}


