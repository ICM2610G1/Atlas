package com.example.atlas

import android.content.Context
import android.hardware.SensorManager
import android.location.Geocoder
import android.os.Bundle
import android.os.StrictMode
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.example.atlas.navegation.Navigation
import com.google.firebase.auth.FirebaseAuth

import androidx.fragment.app.FragmentActivity
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

lateinit var auth : FirebaseAuth

lateinit var database : FirebaseDatabase
lateinit var mStorageRef: StorageReference

lateinit var sensorManager : SensorManager
lateinit var promptInfo: BiometricPrompt.PromptInfo
var canAutenticate=false
lateinit var geocoder : Geocoder

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupAuth()

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        geocoder = Geocoder(this)
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        mStorageRef = FirebaseStorage.getInstance().getReference()
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        enableEdgeToEdge()
        setContent {
            Navigation()
        }
    }
    fun setupAuth(){
        if (BiometricManager.from(this)
                .canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG)== BiometricManager.BIOMETRIC_SUCCESS) {
            canAutenticate=true
            promptInfo = BiometricPrompt.PromptInfo.Builder()
                .setTitle("Autenticación con huella")
                .setSubtitle("Sensor biométrico")
                .setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_STRONG)
                .setNegativeButtonText("Cancelar")
                .build()

        }
    }
    fun Authenticate(autenticacion: (autenticacion: Boolean) -> Unit) {
        if (canAutenticate) {
            val executor = ContextCompat.getMainExecutor(this)
            val biometricPrompt = BiometricPrompt(this, executor, object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    autenticacion(true)
                }

                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    autenticacion(false) // <--- Maneja el error para que no se quede trabado
                }

                // Opcional: onAuthenticationFailed para intentos fallidos sin cerrar el diálogo
            })
            biometricPrompt.authenticate(promptInfo)
        } else {
            autenticacion(true)
        }
    }
}
