package com.example.atlas.screens


import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.atlas.R
import com.example.atlas.auth
import com.example.atlas.database
import com.example.atlas.elements.DefaulButton
import com.example.atlas.elements.DefaultTopAppBar
import com.example.atlas.mStorageRef
import com.example.atlas.navegation.AppScreens
import com.example.atlas.objetosDB.Deportista
import com.example.atlas.objetosDB.Entrenador
import com.example.atlas.objetosDB.UsuariosGen
import com.example.atlas.viewmodels.EntRegisterViewModel
import com.example.atlas.viewmodels.validateEntRegisterForm
import java.io.File


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpEnt(controller: NavController , model : EntRegisterViewModel = viewModel() ) {
    val context = LocalContext.current
    val state by model.registerState.collectAsState()
    val rojo = colorResource(id = R.color.rojoGranada)
    val fondo = colorResource(id = R.color.pink)
    val camaraUri = FileProvider.getUriForFile(
        context,
        "com.example.atlas.fileprovider",
        File(context.filesDir, "CameraPic.jpg")
    )
    val launcherCamara =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { resultado ->
            if (resultado) {
                model.actImagen(camaraUri)
            }
        }
    val launcherGaleria = rememberLauncherForActivityResult(ActivityResultContracts.GetContent())
    { result ->
        model.actImagen(result)
    }

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
                    modifier = Modifier.fillMaxSize().padding(top = 10.dp).background(
                        color = fondo,
                        shape = RoundedCornerShape(topStart = 38.dp, topEnd = 38.dp)
                    ).verticalScroll(rememberScrollState())
                        .padding(horizontal = 20.dp, vertical = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text(
                        text = "Crear cuenta",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = rojo,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(bottom = 20.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Box(
                            contentAlignment = Alignment.BottomEnd
                        ) {
                            if (state.imagenUri != null) {
                                AsyncImage(
                                    model = state.imagenUri,
                                    contentDescription = "Foto de perfil",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.size(90.dp).clip(CircleShape)
                                )
                            } else {
                                Box(
                                    modifier = Modifier
                                        .size(90.dp)
                                        .clip(CircleShape)
                                        .background(Color.White.copy(alpha = 0.6f)), // Contraste limpio con el fondo
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Person,
                                        contentDescription = "Foto de perfil",
                                        tint = rojo,
                                        modifier = Modifier.size(50.dp)
                                    )
                                }
                            }
                        }
                        Column(
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            DefaulButton("Tomar foto", 140, 38) {
                                launcherCamara.launch(camaraUri)
                            }
                            DefaulButton("Seleccionar foto", 140, 38) {
                                launcherGaleria.launch("image/*")
                            }
                        }
                    }
                    TextField(
                        value = state.nombre,
                        onValueChange = { model.updateNombre(it) },
                        label = { Text("Usuario") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 14.dp),
                        singleLine = true,
                        supportingText = {
                            Text(state.nombreError, color = Color.Red)
                        }
                    )
                    TextField(
                        value = state.fechaNacimiento,
                        onValueChange = { model.updateFechaNacimiento(it) },
                        label = { Text("Fecha de nacimiento") },
                        placeholder = { Text("Ej: 20/06/2006") },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Next
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 14.dp),
                        singleLine = true,
                        supportingText = {
                            Text(state.fechaNacimientoError, color = Color.Red)
                        }
                    )

                    TextField(
                        value = state.telefono,
                        onValueChange = { model.updateTelefono(it) },
                        label = { Text("Teléfono") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 14.dp),
                        singleLine = true,
                        supportingText = {
                            Text(state.telefonoError, color = Color.Red)
                        }
                    )

                    TextField(
                        value = state.correo,
                        onValueChange = { model.updateCorreo(it) },
                        label = { Text("Correo") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 14.dp),
                        singleLine = true,
                        supportingText = {
                            Text(state.correoError, color = Color.Red)
                        }
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
                            TextButton(onClick = { model.toggleMostrarPass() }) {
                                Text(if (state.mostrarPass) "Ocultar" else "Ver")
                            }
                        },
                        supportingText = {
                            Text(state.passError, color = Color.Red)
                        }

                    )
                    TextField(
                        value = if (state.experiencia == 0.0) "" else state.experiencia.toString(),
                        onValueChange = { input ->
                            if (input.isBlank()) model.updateExperiencia(0.0)
                            else input.toDoubleOrNull()?.let { model.updateExperiencia(it) }
                        },
                        label = { Text("Años de experiencia") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 14.dp),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Next
                        ),
                        singleLine = true,
                        supportingText = {
                            Text(state.expError, color = Color.Red)
                        }
                    )
                    TextField(
                        value = state.especialidad,
                        onValueChange = { model.updateEspecialidad(it) },
                        label = { Text("Tu especialidad") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 14.dp),
                        singleLine = true,
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp)
                    ) {
                        Checkbox(
                            checked = state.aceptar,
                            onCheckedChange = { model.toggleAceptar() }
                        )
                        Text("Acepto términos y condiciones")
                    }

                    Button(
                        onClick = {
                            if (validateEntRegisterForm(model, state)) {
                                auth.createUserWithEmailAndPassword(state.correo, state.pass)
                                    .addOnCompleteListener {
                                        if (it.isSuccessful) {
                                            val user = auth.currentUser?.uid
                                            val imagen: Uri? = state.imagenUri
                                            if (imagen != null) {
                                                val imageRef =
                                                    mStorageRef.child("images/profile/${user}/image.jpg")
                                                imageRef.putFile(imagen).addOnSuccessListener {
                                                    imageRef.downloadUrl.addOnSuccessListener { uriNube ->
                                                        val url = uriNube.toString()
                                                        val dbRef =
                                                            database.getReference("entrenadores/${user}")
                                                        user?.let {
                                                            val usuario = Entrenador(
                                                                user,
                                                                url,
                                                                state.nombre,
                                                                state.fechaNacimiento,
                                                                state.telefono,
                                                                state.correo,
                                                                state.experiencia,
                                                                state.especialidad,
                                                            )
                                                            dbRef.setValue(usuario)
                                                            val dbRef2 =
                                                                database.getReference("UsuarioGen/${user}")
                                                            val copia = UsuariosGen(
                                                                user,
                                                                url,
                                                                state.nombre,
                                                                "Entrenador",
                                                                state.correo,
                                                            )
                                                            dbRef2.setValue(copia)
                                                            controller.navigate(route = AppScreens.HomeCoach.name) {
                                                                popUpTo(AppScreens.SignUpEnt.name) {
                                                                    inclusive = true
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            } else {
                                                val dbRef =
                                                    database.getReference("entrenadores/${user}")
                                                user?.let {
                                                    val usuario = Entrenador(
                                                        user,
                                                        "",
                                                        state.nombre,
                                                        state.fechaNacimiento,
                                                        state.telefono,
                                                        state.correo,
                                                        state.experiencia,
                                                        state.especialidad
                                                    )
                                                    dbRef.setValue(usuario)
                                                    val dbRef2 =
                                                        database.getReference("UsuarioGen/${user}")
                                                    val copia = UsuariosGen(
                                                        user,
                                                        "",
                                                        state.nombre,
                                                        "Entrenador",
                                                        state.correo
                                                    )
                                                    dbRef2.setValue(copia)
                                                    controller.navigate(route = AppScreens.HomeCoach.name) {
                                                        popUpTo(AppScreens.SignUpEnt.name) {
                                                            inclusive = true
                                                        }
                                                    }
                                                }
                                            }
                                        } else {
                                            Log.e("MYTAG", "Error creating user" + state.correo)
                                        }
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



