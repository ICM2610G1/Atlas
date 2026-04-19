package com.example.atlas.screens

import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.atlas.R
import com.example.atlas.elements.DefaulButton
import com.example.atlas.navegation.AppScreens
import com.example.atlas.sensores.HelperBiometrico
import com.example.atlas.viewmodels.ModeloLogIn

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogIn(
    controller: NavController,
    modelo: ModeloLogIn = viewModel()
) {
    val estado by modelo.estado.collectAsState()
    val contexto = LocalContext.current

    val helperBiometrico = remember { HelperBiometrico(contexto) }

    val lanzadorBiometrico = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { concedido: Boolean ->
        if (concedido) {
            Log.i("LogIn", "Autenticación biométrica concedida")
            modelo.marcarAutenticado()
        } else {
            Log.w("LogIn", "Autenticación biométrica denegada")
            modelo.registrarError("Autenticación no completada")
        }
    }

    if (estado.autenticado) {
        controller.navigate(route = AppScreens.Home.name)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            "Iniciar Sesión",
                            color = colorResource(R.color.white),
                            fontWeight = FontWeight.Black
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(colorResource(R.color.rojoGranada)),
                    modifier = Modifier
                        .padding(vertical = 40.dp)
                        .padding(horizontal = 10.dp)
                )
            },
            containerColor = colorResource(R.color.rojoGranada)
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .fillMaxHeight(0.989f)
                    .clip(RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp))
                    .background(colorResource(R.color.pink))
                    .padding(vertical = 40.dp)
                    .padding(horizontal = 25.dp),
                verticalArrangement = Arrangement.Top
            ) {
                Text("Usuario", fontWeight = FontWeight.Medium, fontSize = 19.sp)

                TextField(
                    value = estado.usuario,
                    onValueChange = { modelo.actualizarUsuario(it) },
                    placeholder = { Text("Nombre de usuario") },
                    modifier = Modifier.fillMaxWidth().padding(vertical = 5.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = colorResource(R.color.white),
                        focusedContainerColor = colorResource(R.color.white),
                        unfocusedPlaceholderColor = Color.Gray,
                        focusedPlaceholderColor = Color.Gray
                    )
                )

                Text(
                    "Contraseña",
                    fontWeight = FontWeight.Medium,
                    fontSize = 19.sp,
                    modifier = Modifier.padding(vertical = 10.dp).fillMaxWidth()
                )

                TextField(
                    trailingIcon = {
                        Icon(
                            painter = painterResource(R.drawable.icon_ojo),
                            contentDescription = "Ojo",
                            tint = colorResource(R.color.rojoGranada)
                        )
                    },
                    value = estado.contrasena,
                    onValueChange = { modelo.actualizarContrasena(it) },
                    placeholder = { Text("Al menos 8 caracteres") },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.padding(vertical = 6.dp).fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = colorResource(R.color.white),
                        focusedContainerColor = colorResource(R.color.white),
                        unfocusedPlaceholderColor = Color.Gray,
                        focusedPlaceholderColor = Color.Gray
                    )
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp, horizontal = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Restablecer contraseña?",
                        color = colorResource(R.color.rojoGranada),
                        modifier = Modifier.clickable {
                            controller.navigate(route = AppScreens.RecoverPassword.name)
                        }
                    )
                }

                // Mostrar error biométrico si existe
                if (estado.mensajeError.isNotBlank()) {
                    Text(
                        text = estado.mensajeError,
                        color = Color.Red,
                        fontSize = 13.sp,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }

                DefaulButton("Iniciar", 380, 40) {
                    Log.i("TAGLogIn", "Click IniciarSesion usuario: ${estado.usuario}")
                    if (estado.usuario.lowercase() == "entrenador") {
                        controller.navigate(route = AppScreens.HomeCoach.name)
                    } else {
                        controller.navigate(route = AppScreens.Home.name)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                if (helperBiometrico.estaDisponible()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = {
                                try {
                                    lanzadorBiometrico.launch(
                                        "android.permission.USE_BIOMETRIC"
                                    )
                                } catch (e: Exception) {
                                    Log.e("LogIn", "Error al lanzar biométrico", e)
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Fingerprint,
                                contentDescription = "Ingresar con huella",
                                tint = colorResource(R.color.rojoGranada),
                                modifier = Modifier.size(52.dp)
                            )
                        }
                        Text(
                            text = "Ingresar con huella",
                            color = colorResource(R.color.rojoGranada),
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }

        Image(
            painter = painterResource(id = R.drawable.panter_feliz),
            contentDescription = "Pantera iniciar sesión",
            modifier = Modifier
                .size(165.dp)
                .align(Alignment.TopEnd)
                .padding(top = 25.dp, end = 45.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun VistaLogIn() {
    LogIn(rememberNavController())
}