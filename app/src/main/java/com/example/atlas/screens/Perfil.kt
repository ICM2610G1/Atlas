package com.example.atlas.screens

import android.net.Uri
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
import com.example.atlas.elements.DefaultBottomBarDep
import com.example.atlas.viewmodels.ModeloPerfil
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Perfil(
    controller: NavController,
    modelo: ModeloPerfil = viewModel()
) {
    val estado by modelo.estado.collectAsState()
    val context = LocalContext.current

    // Uri para guardar la foto de cámara
    val uriCamara = FileProvider.getUriForFile(
        context,
        "com.example.atlas.fileprovider",
        File(context.filesDir, "fotoPerfil.jpg")
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
        bottomBar = { DefaultBottomBarDep(R.color.pink, controller) },
        topBar = {
            TopAppBar(
                title = {
                    Box(
                        modifier = Modifier.fillMaxHeight(),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Text(
                            "Perfil del deportista",
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
                            .clickable { controller.navigate("Appstart") }
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

                // Botón cámara superpuesto — abre selector cámara/galería
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

            // Botón galería como texto clickeable
            Text(
                text = "Seleccionar de galería",
                color = colorResource(R.color.rojoGranada),
                fontSize = 12.sp,
                modifier = Modifier.clickable { lanzadorGaleria.launch("image/*") }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Campos del formulario — estado movido al ViewModel
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
                horizontalArrangement = Arrangement.spacedBy(60.dp, Alignment.CenterHorizontally)
            ) {
                Text("Peso", fontWeight = FontWeight.SemiBold)
                TextField(
                    value = estado.peso,
                    onValueChange = { modelo.actualizarPeso(it) },
                    placeholder = { Text("kg") },
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
                Text("Estatura", fontWeight = FontWeight.SemiBold)
                TextField(
                    value = estado.estatura,
                    onValueChange = { modelo.actualizarEstatura(it) },
                    placeholder = { Text("cm") },
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

            Text("Observaciones médicas", fontWeight = FontWeight.SemiBold,
                modifier = Modifier.fillMaxWidth())
            TextField(
                value = estado.obMedicas,
                onValueChange = { modelo.actualizarObMedicas(it) },
                placeholder = { Text("Agregue observaciones médicas a tener en cuenta") },
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
fun PreviewPerfil() {
    Perfil(rememberNavController())
}