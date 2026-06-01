package com.example.atlas

import com.example.atlas.notificaciones.TrotePendienteNotificacion
import android.Manifest
import android.hardware.SensorManager
import android.location.Geocoder
import android.os.Build
import android.os.Bundle
import android.os.StrictMode
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import com.example.atlas.navegation.Navigation
import com.example.atlas.notificaciones.AdministradorNotificaciones
import com.example.atlas.notificaciones.ChatPendienteNotificacion
import com.example.atlas.notificaciones.TokenFCM
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

lateinit var auth: FirebaseAuth

lateinit var database: FirebaseDatabase
lateinit var mStorageRef: StorageReference

lateinit var sensorManager: SensorManager
lateinit var promptInfo: BiometricPrompt.PromptInfo
var canAutenticate = false
lateinit var geocoder: Geocoder

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupAuth()

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        geocoder = Geocoder(this)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        mStorageRef = FirebaseStorage.getInstance().getReference()

        val permisoNotificaciones = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permisoNotificaciones.launch(Manifest.permission.POST_NOTIFICATIONS)
        }

        TokenFCM.guardarTokenActual()

        AdministradorNotificaciones.crearCanalNotificaciones(this)

        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        val tipoNotificacion = intent.getStringExtra("tipo") ?: "chat"

        if (tipoNotificacion == "trote") {
            val idDeportistaDesdeNotificacion = intent.getStringExtra("idDeportista") ?: ""
            val nombreDeportistaDesdeNotificacion = intent.getStringExtra("nombreDeportista") ?: ""

            if (idDeportistaDesdeNotificacion.isNotBlank()) {
                TrotePendienteNotificacion.guardar(
                    idDeportistaNuevo = idDeportistaDesdeNotificacion,
                    nombreDeportistaNuevo = nombreDeportistaDesdeNotificacion
                )
            }
        } else {
            val idChatDesdeNotificacion = intent.getStringExtra("idChat") ?: ""
            val nombreDesdeNotificacion = intent.getStringExtra("nombre") ?: ""

            if (idChatDesdeNotificacion.isNotBlank()) {
                ChatPendienteNotificacion.guardar(
                    idChatNuevo = idChatDesdeNotificacion,
                    nombreNuevo = nombreDesdeNotificacion
                )
            }
        }

        enableEdgeToEdge()

        setContent {
            Navigation()
        }
    }

    fun setupAuth() {
        if (
            BiometricManager.from(this)
                .canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG)
            == BiometricManager.BIOMETRIC_SUCCESS
        ) {
            canAutenticate = true

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

            val biometricPrompt = BiometricPrompt(
                this,
                executor,
                object : BiometricPrompt.AuthenticationCallback() {

                    override fun onAuthenticationSucceeded(
                        result: BiometricPrompt.AuthenticationResult
                    ) {
                        super.onAuthenticationSucceeded(result)
                        autenticacion(true)
                    }

                    override fun onAuthenticationError(
                        errorCode: Int,
                        errString: CharSequence
                    ) {
                        super.onAuthenticationError(errorCode, errString)
                        autenticacion(false)
                    }
                }
            )

            biometricPrompt.authenticate(promptInfo)
        } else {
            autenticacion(true)
        }
    }
}