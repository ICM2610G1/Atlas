package com.example.atlas.viewmodels

import androidx.lifecycle.ViewModel
import com.example.atlas.modelos.Authstate
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class UserAuthViewModel : ViewModel() {
    private val _authState = MutableStateFlow<Authstate>(Authstate())
    val authState = _authState.asStateFlow()
    fun updateEmail(newValue : String){
        _authState.update { it.copy(email = newValue) }
    }
    fun updatePassword(newValue : String){
        _authState.update { it.copy(password = newValue) }
    }
    fun updateEmailError(newValue : String){
        _authState.update { it.copy(emailError = newValue) }
    }
    fun updatePasswordError(newValue : String){
        _authState.update { it.copy(passwordError = newValue) }
    }
}

