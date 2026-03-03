package com.example.atlas.navegation
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.atlas.screens.AppStart
import com.example.atlas.screens.Calificar
import com.example.atlas.screens.Chats
import com.example.atlas.screens.DetalleSesion
import com.example.atlas.screens.Historial
import com.example.atlas.screens.Home
import com.example.atlas.screens.HomeCoach
import com.example.atlas.screens.LogIn
import com.example.atlas.screens.Perfil
import com.example.atlas.screens.PerfilEnt
import com.example.atlas.screens.SignUp
import com.example.atlas.screens.Ubicacion
import com.example.atlas.screens.ChequeoSesion
import com.example.atlas.screens.subirFoto

enum class AppScreens{
    Appstart,
    Home,
    HomeCoach,
    Perfil,
    Chats,
    Ubicacion,
    Historial,
    PerfilEnt,
    Calificar,
    SignUp,
    LogIn,
    crearSesion,
    detallesTrote,
    DetalleSesion,
    ChequeoSesion,
    chatsP,
    Ejercicios,
    vistaSesionesEnt,
    resumenSesion


}

@Composable
fun Navigation(){
    val navController = rememberNavController()
    NavHost(navController, startDestination = AppScreens.Appstart.name) {
        composable (route= AppScreens.Appstart.name){
            AppStart(navController)
        }
        composable (route= AppScreens.Home.name){
            Home(navController)
        }
        composable (route= AppScreens.Perfil.name){
            Perfil(navController)
        }
        composable (route= AppScreens.Chats.name){
            Chats(navController)
        }
        composable(route= AppScreens.chatsP.name + "/{nombre}") {  backStackEntry ->
            val nombre = backStackEntry.arguments?.getString("nombre") ?: ""
            chatP(nombre, navController)
        }
        composable(route= AppScreens.vistaSesionesEnt.name) {
            vistaSesionesEntrenador(navController)
        }
        composable (route= AppScreens.crearSesion.name){
            CrearNuevaSesion(navController)
        }
        composable (route= AppScreens.Ejercicios.name){
            PantallaEjercicios(navController)
        }
        composable (route= AppScreens.detallesTrote.name){
            DetallesTrote(navController)
        }
        composable (route= AppScreens.Ubicacion.name){
            Ubicacion(navController)
        }
        composable(route= AppScreens.resumenSesion.name ){
            subirFoto(navController)
        }
        composable (route= AppScreens.Historial.name){
            Historial(navController)
        }
        composable (route= AppScreens.HomeCoach.name){
            HomeCoach(navController)
        }
        composable (route= AppScreens.PerfilEnt.name){
            PerfilEnt(navController)
        }
        composable (route= AppScreens.Calificar.name){
            Calificar(navController)
        }
        composable (route= AppScreens.SignUp.name){
            SignUp(navController)
        }
        composable (route= AppScreens.LogIn.name){
            LogIn(navController)
        }
        composable (route= AppScreens.DetalleSesion.name){
            DetalleSesion(navController)
        }
        composable (route= AppScreens.ChequeoSesion.name){
            ChequeoSesion(navController)
        }


    }

}

