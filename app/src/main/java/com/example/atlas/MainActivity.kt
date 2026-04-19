package com.example.atlas

import android.hardware.SensorManager
import android.location.Geocoder
import android.os.Bundle
import android.os.StrictMode
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.atlas.navegation.Navigation
import com.google.firebase.auth.FirebaseAuth
lateinit var auth : FirebaseAuth

lateinit var sensorManager : SensorManager
lateinit var geocoder : Geocoder

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        geocoder = Geocoder(this)

        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        enableEdgeToEdge()
        setContent {
            auth = FirebaseAuth.getInstance()
            Navigation()
        }
    }
}
