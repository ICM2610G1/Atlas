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
import com.example.atlas.elements.DefaulButton
import com.example.atlas.elements.DefaultBottomBarDep
import com.example.atlas.elements.DefaultTopAppBar
import java.io.File

@Composable

fun Camara(controller: NavController) {


    val context = LocalContext.current
    val UriCamara = FileProvider.getUriForFile(
        context,
        "com.example.atlas.fileprovider",
        File(context.filesDir, "Camerapic.jpg")
    )
    var UriImagen by remember { mutableStateOf<Uri?>(null) }
    val camera = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture())
    { it ->
        if (it) {
            UriImagen = UriCamara
        }
    }


    var galeria =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { result ->
            UriImagen = result
        }

    Scaffold(
        topBar = { DefaultTopAppBar("Foto") },
        bottomBar = { (DefaultBottomBarDep(R.color.pink, controller)) }) { paddingValues ->
        Column(
            modifier = Modifier.fillMaxSize().padding(paddingValues).padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(30.dp, Alignment.CenterVertically)

        ) {
            if (UriImagen != null) {
                AsyncImage(
                    model = UriImagen,
                    contentDescription = "foto recuperada",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.weight(1F).fillMaxWidth()

                )
            } else {
                Image(
                    painter = painterResource(R.drawable.oso_megafono),
                    contentDescription = "Foto",
                    modifier = Modifier.size(350.dp)
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(40.dp, Alignment.CenterHorizontally)
            ) {
                DefaulButton("Galeria", 100, 40) {
                    galeria.launch("image/*")

                }
                DefaulButton("Camara", 100, 40) {
                    camera.launch(UriCamara)
                }



            }
            DefaulButton("Subir ft", 200, 40) {
                Log.i("TAGuardarFt","Guardo la ft ")
            }


        }
    }
}