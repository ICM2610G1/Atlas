package com.example.atlas.screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.atlas.R
import com.example.atlas.elements.DefaultBottomBarEnt
import com.example.atlas.viewmodels.ModeloPerfilEnt
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerfilEnt(
    controller: NavController,
    modelo: ModeloPerfilEnt = viewModel()
) {
    val estado by modelo.estado.collectAsState()
    val context = LocalContext.current

    // Uri para foto de cámara
    val uriCamara = FileProvider.getUriForFile(
        context,
        "com.example.atlas.fileprovider",
        File(context.filesDir, "fotoPerfilEnt.jpg")
    )

    // ActivityResult API — cámara
    val lanzadorCamara = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { exitoso ->
        if (exitoso) modelo.guardarFotoCamara(uriCamara)
    }

    // ActivityResult API — galería
    val lanzadorGaleria = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri -> modelo.guardarFotoGaleria(uri) }

    Scaffold(
        bottomBar = { DefaultBottomBarEnt(R.color.pink, controller) },
        topBar = {
            TopAppBar(
                title = {
                    Box(
                        modifier = Modifier.fillMaxHeight(),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Text(
                            "Perfil del entrenador",
                            color = Color.White,
                            fontWeight = FontWeight.Black
                        )
                    }
                },
                actions = {
                    Icon(
                        painter = painterResource(R.drawable.exit_to_app),
                        contentDescription = "Cerrar sesión",
                        tint = Color.White,
                        modifier = Modifier
                            .padding(end = 40.dp)
                            .padding(top = 38.dp)
                            .size(30.dp)
                            .clickable { controller.navigate("AppFinal") }
                    )
                },
                colors = TopAppBarColors(
                    containerColor = colorResource(R.color.rojoGranada),
                    scrolledContainerColor = Color.White,
                    navigationIconContentColor = Color.White,
                    titleContentColor = Color.White,
                    actionIconContentColor = Color.White
                ),
                modifier = Modifier
                    .height(100.dp)
                    .clip(RoundedCornerShape(bottomStart = 25.dp, bottomEnd = 25.dp))
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(20.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Foto de perfil
            Box(contentAlignment = Alignment.BottomEnd) {
                if (estado.uriImagen != null) {
                    AsyncImage(
                        model = estado.uriImagen,
                        contentDescription = "Foto de perfil",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(110.dp)
                            .clip(CircleShape)
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Foto de perfil",
                        tint = Color.Gray,
                        modifier = Modifier
                            .size(110.dp)
                            .clip(CircleShape)
                            .background(colorResource(R.color.white))
                    )
                }

                IconButton(
                    onClick = { lanzadorCamara.launch(uriCamara) },
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(colorResource(R.color.rojoGranada))
                ) {
                    Icon(
                        imageVector = Icons.Default.CameraAlt,
                        contentDescription = "Cambiar foto",
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Seleccionar de galería",
                color = colorResource(R.color.rojoGranada),
                fontSize = 12.sp,
                modifier = Modifier.clickable { lanzadorGaleria.launch("image/*") }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Campos del formulario — estado en ViewModel
            Text("Usuario", fontWeight = FontWeight.SemiBold,
                modifier = Modifier.fillMaxWidth())
            TextField(
                value = estado.usuario,
                onValueChange = { modelo.actualizarUsuario(it) },
                placeholder = { Text("Nombre de usuario") },
                modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp),
                shape = RoundedCornerShape(14.dp),
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = colorResource(R.color.white),
                    focusedContainerColor = colorResource(R.color.white),
                    unfocusedPlaceholderColor = Color.Gray,
                    focusedPlaceholderColor = Color.Gray
                )
            )

            Text("Teléfono", fontWeight = FontWeight.SemiBold,
                modifier = Modifier.fillMaxWidth())
            TextField(
                value = estado.telefono,
                onValueChange = { modelo.actualizarTelefono(it) },
                placeholder = { Text("+ 57") },
                modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp),
                shape = RoundedCornerShape(14.dp),
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = colorResource(R.color.white),
                    focusedContainerColor = colorResource(R.color.white),
                    unfocusedPlaceholderColor = Color.Gray,
                    focusedPlaceholderColor = Color.Gray
                )
            )

            Text("Correo", fontWeight = FontWeight.SemiBold,
                modifier = Modifier.fillMaxWidth())
            TextField(
                value = estado.correo,
                onValueChange = { modelo.actualizarCorreo(it) },
                placeholder = { Text("ejemplo@gmail.com") },
                modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp),
                shape = RoundedCornerShape(14.dp),
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = colorResource(R.color.white),
                    focusedContainerColor = colorResource(R.color.white),
                    unfocusedPlaceholderColor = Color.Gray,
                    focusedPlaceholderColor = Color.Gray
                )
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(20.dp, Alignment.CenterHorizontally)
            ) {
                Text("Años ejerciendo", fontWeight = FontWeight.SemiBold)
                TextField(
                    value = estado.aniosEjerciendo,
                    onValueChange = { modelo.actualizarAnios(it) },
                    placeholder = { Text("----") },
                    modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = colorResource(R.color.white),
                        focusedContainerColor = colorResource(R.color.white),
                        unfocusedPlaceholderColor = Color.Gray,
                        focusedPlaceholderColor = Color.Gray
                    )
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(40.dp, Alignment.CenterHorizontally)
            ) {
                Text("Especialidad", fontWeight = FontWeight.SemiBold)
                TextField(
                    value = estado.especialidad,
                    onValueChange = { modelo.actualizarEspecialidad(it) },
                    placeholder = { Text("-") },
                    modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = colorResource(R.color.white),
                        focusedContainerColor = colorResource(R.color.white),
                        unfocusedPlaceholderColor = Color.Gray,
                        focusedPlaceholderColor = Color.Gray
                    )
                )
            }

            Text("Sobre ti", fontWeight = FontWeight.SemiBold,
                modifier = Modifier.fillMaxWidth())
            TextField(
                value = estado.sobreTi,
                onValueChange = { modelo.actualizarSobreTi(it) },
                placeholder = { Text("Agregue información que permita a los deportistas conocer su potencial como entrenador") },
                modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp),
                shape = RoundedCornerShape(14.dp),
                minLines = 3,
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = colorResource(R.color.white),
                    focusedContainerColor = colorResource(R.color.white),
                    unfocusedPlaceholderColor = Color.Gray,
                    focusedPlaceholderColor = Color.Gray
                )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PerfilEntPreview() {
    PerfilEnt(rememberNavController())
}