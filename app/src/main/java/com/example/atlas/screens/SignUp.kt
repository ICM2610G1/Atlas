package com.example.atlas.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.atlas.R
import com.example.atlas.auth
import com.example.atlas.elements.DefaultTopAppBar
import com.example.atlas.modelos.RegisterState
import com.example.atlas.navegation.AppScreens
import com.example.atlas.viewmodels.RegisterViewModel
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow







@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUp(controller: NavController , model : RegisterViewModel = viewModel() ) {
    val state by model.registerState.collectAsState()


    val rojo = colorResource(id = R.color.rojoGranada)
    val fondo = colorResource(id = R.color.pink)


    Scaffold(
        containerColor = rojo,
        topBar = { DefaultTopAppBar("Registrarse") }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(rojo)
        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 10.dp)
                    .background(
                        color = fondo,
                        shape = RoundedCornerShape(topStart = 38.dp, topEnd = 38.dp)
                    )
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp, vertical = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = "Crear cuenta",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = rojo,
                    modifier = Modifier.padding(bottom = 20.dp)
                )

                TextField(
                    value = state.usuario,
                    onValueChange = { model.updateUsuario(it) },
                    label = { Text("Usuario") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 14.dp),
                    singleLine = true
                )

                TextField(
                    value = state.telefono,
                    onValueChange = { model.updateTelefono(it) },
                    label = { Text("Teléfono") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 14.dp),
                    singleLine = true
                )

                TextField(
                    value = state.correo,
                    onValueChange = {model.updateCorreo(it)},
                    label = { Text("Correo") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 14.dp),
                    singleLine = true
                )

                TextField(
                    value = state.pass,
                    onValueChange = { model.updatePass(it) },
                    label = { Text("Contraseña") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 14.dp),
                    singleLine = true,
                    visualTransformation = if (state.mostrarPass)
                        VisualTransformation.None
                    else
                        PasswordVisualTransformation(),
                    trailingIcon = {
                        TextButton(onClick = {model.toggleMostrarPass() }) {
                            Text(if (state.mostrarPass) "Ocultar" else "Ver")
                        }
                    }
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                ) {
                    Checkbox(
                        checked = state.aceptar,
                        onCheckedChange = { model.toggleAceptar()}
                    )
                    Text("Acepto términos y condiciones")
                }

                Button(
                    onClick = {
                        auth.createUserWithEmailAndPassword(state.correo, state.pass).addOnCompleteListener {
                            if (it.isSuccessful){
                                val user = auth.currentUser
                                user?.let{
                                    var upcrb = UserProfileChangeRequest.Builder()
                                    upcrb.setDisplayName(state.usuario)
                                    if (state.correo.lowercase().contains("entrenador")) {
                                        controller.navigate(route = AppScreens.HomeCoach.name)
                                    } else {
                                        controller.navigate(route = AppScreens.Home.name)
                                    }

                                }
                            }else{
                                Log.e("MYTAG", "Error creating user"+state.correo)
                            }
                        }

                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp)
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = rojo,
                        contentColor = Color.White
                    )
                ) {
                    Text("Crear cuenta")
                }
            }
        }
    }
}



