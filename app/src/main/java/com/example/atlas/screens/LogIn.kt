package com.example.atlas.screens

import android.content.Context
import android.content.ContextWrapper
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.atlas.MainActivity
import com.example.atlas.R
import com.example.atlas.auth
import com.example.atlas.elements.DefaulButton
import com.example.atlas.navegation.AppScreens
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

data class Authstate(
    val email: String = "",
    val password: String = "",
    val emailError: String = "",
    val passwordError: String = ""
)

class UserAuthViewModel : ViewModel() {
    private val _authState = MutableStateFlow<Authstate>(Authstate())
    val authState = _authState.asStateFlow()
    fun updateEmail(newEmail: String) { _authState.value = _authState.value.copy(email = newEmail) }
    fun updatePassword(newPass: String) { _authState.value = _authState.value.copy(password = newPass) }
    fun updateEmailError(error: String) { _authState.value = _authState.value.copy(emailError = error) }
    fun updatePasswordError(error: String) { _authState.value = _authState.value.copy(passwordError = error) }
}

private fun validEmailAddress(email: String): Boolean {
    val regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\$"
    return email.matches(regex.toRegex())
}

fun validateForm(model: UserAuthViewModel, email: String, password: String): Boolean {
    if (email.isEmpty()) { model.updateEmailError("Email is empty"); return false } else model.updateEmailError("")
    if (!validEmailAddress(email)) { model.updateEmailError("Not a valid address"); return false } else model.updateEmailError("")
    if (password.isEmpty()) { model.updatePasswordError("Password is empty"); return false } else model.updatePasswordError("")
    if (password.length < 6) { model.updatePasswordError("Password is too short"); return false } else model.updatePasswordError("")
    return true
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogIn(controller: NavController, model: UserAuthViewModel = viewModel()) {
    val context = LocalContext.current
    val state by model.authState.collectAsState()


    Box(modifier = Modifier.fillMaxSize()) {

        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            "Iniciar Sesión",
                            color = colorResource(R.color.white),
                            fontWeight = FontWeight.Black
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(colorResource(R.color.rojoGranada)),
                    modifier = Modifier
                        .padding(vertical = 40.dp)
                        .padding(horizontal = 10.dp)
                )
            },
            containerColor = colorResource(R.color.rojoGranada)
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .fillMaxHeight(0.989f)
                    .clip(RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp))
                    .background(colorResource(R.color.pink))
                    .padding(vertical = 40.dp)
                    .padding(horizontal = 25.dp),
                verticalArrangement = Arrangement.Top,
            ) {
                Text("Usuario", fontWeight = FontWeight.Medium, fontSize = 19.sp)

                TextField(
                    value = state.email,
                    onValueChange = { model.updateEmail(it) },
                    placeholder = { Text("Nombre de usuario") },
                    modifier = Modifier.fillMaxWidth().padding(vertical = 5.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = colorResource(R.color.white),
                        focusedContainerColor = colorResource(R.color.white),
                        unfocusedPlaceholderColor = Color.Gray,
                        focusedPlaceholderColor = Color.Gray
                    ),
                    supportingText = { Text(state.emailError, color = Color.Red) }
                )

                Text(
                    "Contraseña",
                    fontWeight = FontWeight.Medium,
                    fontSize = 19.sp,
                    modifier = Modifier.padding(vertical = 10.dp).fillMaxWidth()
                )

                TextField(
                    trailingIcon = {
                        Icon(
                            painter = painterResource(R.drawable.icon_ojo),
                            contentDescription = "Ojo",
                            tint = colorResource(R.color.rojoGranada)
                        )
                    },
                    value = state.password,
                    onValueChange = { model.updatePassword(it) },
                    placeholder = { Text("Al menos 6 caracteres") },
                    modifier = Modifier.padding(vertical = 6.dp).fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = colorResource(R.color.white),
                        focusedContainerColor = colorResource(R.color.white),
                        unfocusedPlaceholderColor = Color.Gray,
                        focusedPlaceholderColor = Color.Gray
                    ),
                    supportingText = { Text(state.passwordError, color = Color.Red) }
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp, horizontal = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Restablecer contraseña?",
                        color = colorResource(R.color.rojoGranada),
                        modifier = Modifier.clickable {
                            controller.navigate(route = AppScreens.RecoverPassword.name)
                        }
                    )
                }

                DefaulButton("Iniciar", 380, 40) {
                    if (validateForm(model, state.email, state.password)) {
                        auth.signInWithEmailAndPassword(state.email, state.password)
                            .addOnCompleteListener {
                                if (it.isSuccessful) {
                                    if (state.email.lowercase().contains("entrenador")) {
                                        controller.navigate(route = AppScreens.HomeCoach.name)
                                    } else {
                                        controller.navigate(route = AppScreens.Home.name)
                                    }
                                } else {
                                    Toast.makeText(
                                        context, "Login error ${it.exception.toString()}",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))


                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = {
                            val actividad = context.findActivity()
                            actividad?.Authenticate { exito ->
                                if (exito) {
                                    controller.navigate(route = AppScreens.Home.name)
                                } else {
                                    Toast.makeText(context, "Error de autenticación", Toast.LENGTH_SHORT).show()
                                }

                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Fingerprint,
                            contentDescription = "Ingresar con huella",
                            tint = colorResource(R.color.rojoGranada),
                            modifier = Modifier.size(52.dp)
                        )
                    }
                    Text(
                        "Ingresar con huella",
                        color = colorResource(R.color.rojoGranada),
                        fontSize = 14.sp
                    )
                }

            }


            Image(
                painter = painterResource(id = R.drawable.panter_feliz),
                contentDescription = "Pantera iniciar sesión",
                modifier = Modifier
                    .size(165.dp)
                    .align(Alignment.TopEnd)
                    .padding(top = 25.dp, end = 45.dp)
            )
        }
    }
}
fun Context.findActivity(): MainActivity? {
    var ctx = this
    while (ctx is ContextWrapper) {
        if (ctx is MainActivity) return ctx
        ctx = ctx.baseContext
    }
    return null
}