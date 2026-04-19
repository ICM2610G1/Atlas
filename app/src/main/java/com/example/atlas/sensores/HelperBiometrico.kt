package com.example.atlas.sensores

import android.content.Context
import android.util.Log
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL


class HelperBiometrico(private val contexto: Context) {

    fun estaDisponible(): Boolean {
        val administrador = BiometricManager.from(contexto)
        val resultado = administrador.canAuthenticate(BIOMETRIC_STRONG or DEVICE_CREDENTIAL)
        Log.i("HelperBiometrico", "Estado biométrico: $resultado")
        return resultado == BiometricManager.BIOMETRIC_SUCCESS
    }
}