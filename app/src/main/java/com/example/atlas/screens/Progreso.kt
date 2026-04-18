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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.atlas.R
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.atlas.elements.DefaulButton
import com.example.atlas.elements.DefaultBottomBarDep
import com.example.atlas.elements.DefaultTopAppBar
import com.example.atlas.navegation.AppScreens
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import androidx.compose.runtime.getValue
import androidx.compose.ui.text.style.TextAlign


data class Progreso(
    val objetivo: String = "",
    val metaCalorica : String = "" ,
    val fecha  : String = "" ,
    val enableObjetivo  : Boolean = false,
    val enablemetacalorica: Boolean   = false,
    val enablefecha: Boolean = false

)

class ProgresoViewModel: ViewModel(){
    private val _progreso = MutableStateFlow(Progreso())
    val progreso = _progreso.asStateFlow()
    fun updateObjetivo(newObjetivo : String){
        _progreso.value = _progreso.value.copy(objetivo = newObjetivo)
    }
    fun updateMetaCaloria(newMetacalorica : String ){
        _progreso.value = _progreso.value.copy(metaCalorica = newMetacalorica)
    }
    fun updateFecha(newFecha : String ){
        _progreso.value = _progreso.value.copy(fecha = newFecha)
    }
    fun updateEnableObjetivo(estado : Boolean ){
        _progreso.value = _progreso.value.copy(enableObjetivo =  estado)
    }
    fun updateEnableMetacaloria(estado : Boolean ){
        _progreso.value = _progreso.value.copy(enablemetacalorica =  estado)
    }
    fun updateEnableFecha(estado : Boolean ){
        _progreso.value = _progreso.value.copy(enablefecha =  estado)
    }



    /*fun updateEmailError(error:String){
        _authState.value = _authState.value.copy(emailError = error)
    }
    */
}


@Composable
fun Progreso (controller : NavController , model : ProgresoViewModel = viewModel()) {
    val state by model.progreso.collectAsState()
    Scaffold(
        topBar = { DefaultTopAppBar("Progreso") },
        bottomBar = { (DefaultBottomBarDep(R.color.pink, controller)) }) { paddingValues ->
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize().padding(paddingValues).padding(horizontal =25.dp)
        ) {
            Row(modifier = Modifier.weight(1F).fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Text(
                    "Establezca su objetivo",
                    fontWeight = FontWeight.Medium,
                    fontSize = 19.sp,
                    textAlign = TextAlign.Left
                )
            }

            TextField(
                trailingIcon = {
                    IconButton(onClick ={model.updateEnableObjetivo(true)} ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Editar",
                            tint = colorResource(R.color.rojoGranada)
                        )
                    }
                },
                value = state.objetivo, onValueChange = {model.updateObjetivo(it)}, placeholder = {Text("objetivo")},
                modifier = Modifier.fillMaxWidth().padding(vertical = 3.dp).weight(2F),
                shape = RoundedCornerShape(14.dp),
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = colorResource(R.color.white),
                    focusedContainerColor = colorResource(R.color.white),
                    unfocusedPlaceholderColor = Color.Gray,
                    focusedPlaceholderColor = Color.Gray
                ),
                enabled = state.enableObjetivo
            )

            Row(modifier = Modifier.weight(1F).fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Text(
                    "Ingrese su meta calorica",
                    fontWeight = FontWeight.Medium,
                    fontSize = 19.sp
                )
            }
            TextField(
                trailingIcon = {
                    IconButton(onClick ={model.updateEnableMetacaloria(true)} ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Editar",
                            tint = colorResource(R.color.rojoGranada)
                        )
                    }
                },
                value = state.metaCalorica, onValueChange = {model.updateMetaCaloria(it)}, placeholder = {Text("calorias")},
                modifier = Modifier.fillMaxWidth().padding(vertical = 3.dp).weight(2F),
                shape = RoundedCornerShape(14.dp),
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = colorResource(R.color.white),
                    focusedContainerColor = colorResource(R.color.white),
                    unfocusedPlaceholderColor = Color.Gray,
                    focusedPlaceholderColor = Color.Gray
                ),
                enabled = state.enablemetacalorica
            )
            Row(modifier = Modifier.weight(1F).fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Text(
                    "Ingrese la fecha",
                    fontWeight = FontWeight.Medium,
                    fontSize = 19.sp
                )
            }
            TextField(
                trailingIcon = {
                    IconButton(onClick ={model.updateEnableFecha(true)} ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Editar",
                            tint = colorResource(R.color.rojoGranada)
                        )
                    }
                },
                value = state.fecha, onValueChange = {model.updateFecha(it)}, placeholder = {Text("Fecha")},
                modifier = Modifier.fillMaxWidth().padding(vertical = 3.dp).weight(2F),
                shape = RoundedCornerShape(14.dp),
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = colorResource(R.color.white),
                    focusedContainerColor = colorResource(R.color.white),
                    unfocusedPlaceholderColor = Color.Gray,
                    focusedPlaceholderColor = Color.Gray
                ),
                enabled = state.enablefecha
            )
            Row(
                modifier = Modifier.weight(8F)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Button(
                    onClick = {controller.navigate(AppScreens.Camara.name)},
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(R.color.rojoGranada),
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Tomar foto", fontWeight = FontWeight.Bold)
                }


                Button(
                    onClick = { /*Aqui se gguarda */ Log.e("TAGuardar","Se guardo ") },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.rojoGranada), contentColor = Color.White),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Guardar")
                }
            }

        }
    }
}
@Preview (showBackground = true)
@Composable
fun PreviewProgreso (){
    val nc = rememberNavController()
    Progreso(nc)
}