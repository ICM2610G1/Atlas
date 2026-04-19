package com.example.atlas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.atlas.navegation.Navigation
import com.google.firebase.auth.FirebaseAuth
import org.osmdroid.config.Configuration
import java.io.File

lateinit var auth : FirebaseAuth

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Configuración global de OSM
        Configuration.getInstance().apply {
            userAgentValue = packageName
            osmdroidBasePath = getExternalFilesDir(null)
            osmdroidTileCache = File(getExternalFilesDir(null), "tiles")
            load(applicationContext, getSharedPreferences("osmdroid", MODE_PRIVATE))
        }

        enableEdgeToEdge()
        setContent {
            auth = FirebaseAuth.getInstance()
            Navigation()
        }
    }
}
