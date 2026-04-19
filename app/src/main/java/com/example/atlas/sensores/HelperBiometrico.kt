package com.example.atlas.sensores

import android.content.Context
import android.util.Log
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity

class HelperBiometrico(private val contexto: Context) {

    fun estaDisponible(): Boolean {
        val administrador = BiometricManager.from(contexto)
        val resultado = administrador.canAuthenticate(BIOMETRIC_STRONG or DEVICE_CREDENTIAL)
        Log.i("HelperBiometrico", "Estado biométrico: $resultado")
        return resultado == BiometricManager.BIOMETRIC_SUCCESS
    }

    fun autenticar(
        alAutenticar: () -> Unit,
        alFallar: (String) -> Unit
    ) {
        val actividad = contexto as? FragmentActivity ?: return
        val executor = ContextCompat.getMainExecutor(contexto)

        val prompt = BiometricPrompt(
            actividad,
            executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(
                    result: BiometricPrompt.AuthenticationResult
                ) {
                    alAutenticar()
                }
                override fun onAuthenticationError(
                    errorCode: Int,
                    errString: CharSequence
                ) {
                    alFallar(errString.toString())
                }
                override fun onAuthenticationFailed() {
                    alFallar("Huella no reconocida")
                }
            }
        )

        val info = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Ingresar a Atlas")
            .setSubtitle("Usa tu huella digital para continuar")
            .setNegativeButtonText("Usar contraseña")
            .build()

        prompt.authenticate(info)
    }
}