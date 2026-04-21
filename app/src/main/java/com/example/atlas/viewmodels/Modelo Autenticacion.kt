package com.example.atlas.viewmodels

import androidx.lifecycle.ViewModel
import com.example.atlas.modelos.Authstate
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class UserAuthViewModel : ViewModel() {
    private val _authState = MutableStateFlow<Authstate>(Authstate())
    val authState = _authState.asStateFlow()
    fun updateEmail(newEmail: String) { _authState.value = _authState.value.copy(email = newEmail) }
    fun updatePassword(newPass: String) { _authState.value = _authState.value.copy(password = newPass) }
    fun updateEmailError(error: String) { _authState.value = _authState.value.copy(emailError = error) }
    fun updatePasswordError(error: String) { _authState.value = _authState.value.copy(passwordError = error) }
}
