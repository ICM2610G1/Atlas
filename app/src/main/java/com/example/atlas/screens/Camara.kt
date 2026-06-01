package com.example.atlas.screens


import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.atlas.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.atlas.elements.DefaulButton
import com.example.atlas.elements.DefaultBottomBarDep
import com.example.atlas.elements.DefaultTopAppBar
import com.example.atlas.viewmodels.ProgresoViewModel
import java.io.File

@Composable
fun Camara(controller: NavController, model: ProgresoViewModel = viewModel()) {

    val context = LocalContext.current
    val uriCamara = remember {
        FileProvider.getUriForFile(
            context,
            "com.example.atlas.fileprovider",
            File(context.filesDir, "Camerapic.jpg")
        )
    }

    var uriImagen by remember { mutableStateOf<Uri?>(null) }
    val state by model.progreso.collectAsState()

    val camera = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { ok ->
        if (ok) uriImagen = uriCamara
    }

    val galeria = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { result ->
        uriImagen = result
    }

    Scaffold(
        topBar = { DefaultTopAppBar("Foto") },
        bottomBar = { DefaultBottomBarDep(R.color.pink, controller) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(30.dp, Alignment.CenterVertically)
        ) {
            if (uriImagen != null) {
                AsyncImage(
                    model = uriImagen,
                    contentDescription = "Foto seleccionada",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.size(300.dp)
                )
            } else {
                Image(
                    painter = painterResource(R.drawable.oso_megafono),
                    contentDescription = "Foto placeholder",
                    modifier = Modifier.size(300.dp)
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterHorizontally)
            ) {
                DefaulButton("Galeria", 100, 40) {
                    galeria.launch("image/*")
                }
                DefaulButton("Camara", 100, 40) {
                    camera.launch(uriCamara)
                }
                DefaulButton("Subir", 100, 40) {
                    uriImagen?.let { uri ->
                        model.subirFoto(uri)
                        controller.popBackStack()
                    }
                }
            }
        }
    }
}




