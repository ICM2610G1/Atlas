package com.example.atlas.navegation
import Pantallas.EjercicioViewModel
import Pantallas.PantallaEjercicios
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.atlas.screens.AppStart
import com.example.atlas.screens.Calificar
import com.example.atlas.screens.Camara
import com.example.atlas.screens.CatalogoActividades
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
import com.example.atlas.screens.CrearNuevaSesion
import com.example.atlas.screens.DetallesTrote
import com.example.atlas.screens.MessageEmail
import com.example.atlas.screens.MiUbicacion
import com.example.atlas.screens.Progreso
import com.example.atlas.screens.RecoverPassword
import com.example.atlas.screens.ResumenSesion
import com.example.atlas.screens.ResumenTrote
import com.example.atlas.screens.TroteActivo
import com.example.atlas.screens.chatP
import com.example.atlas.screens.vistaSesionesEntrenador
import com.example.atlas.viewmodels.ModeloMiUbicacion

enum class AppScreens{
    Appstart,
    Home,
    HomeCoach,
    Perfil,
    Chats,
    Ubicacion,

    Mimapa,
    Historial,
    PerfilEnt,
    Calificar,
    SignUp,
    LogIn,
    CrearNuevaSesion,
    detallesTrote,
    DetalleSesion,
    ChequeoSesion,
    chatsP,
    Ejercicios,
    vistaSesionesEnt,
    ResumenSesion,
    RecoverPassword,
    // AppFinal,
    MessageEmail,
    troteActivo,
    ResumenTrote,

    agregarEjercicio,
    Camara,
    Progreso

}

@Composable
fun Navigation(){
    val navController = rememberNavController()
    val miUbicacionViewModel: ModeloMiUbicacion = viewModel()
    val EjercicioviewModel: EjercicioViewModel = viewModel()
    NavHost(navController, startDestination = AppScreens.Home.name){
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
        composable (route= AppScreens.CrearNuevaSesion.name){
            CrearNuevaSesion(navController)
        }
        composable (route= AppScreens.Ejercicios.name+"/{ejercicio}"){
                backStackEntry ->
            val ejercicio = backStackEntry.arguments?.getString("ejercicio")

            PantallaEjercicios(controller = navController, ejercicio = ejercicio, viewModel = EjercicioviewModel)
        }
        composable (route= AppScreens.Ejercicios.name){


            PantallaEjercicios(controller = navController, viewModel = EjercicioviewModel)
        }
        composable (route= AppScreens.detallesTrote.name){
            DetallesTrote(navController)
        }
        composable (route= AppScreens.Ubicacion.name){
            Ubicacion(navController)
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

        composable (route= AppScreens.agregarEjercicio.name){
            CatalogoActividades(navController)
        }
        composable (route= AppScreens.LogIn.name){
            LogIn(navController)
        }
        composable (route= AppScreens.DetalleSesion.name){
            DetalleSesion(navController)
        }
        composable (route= AppScreens.ChequeoSesion.name){

            ChequeoSesion(navController,EjercicioviewModel)
        }
        composable (route= AppScreens.RecoverPassword.name){
            RecoverPassword(navController)
        }
        composable (route= AppScreens.Mimapa.name + "/{inicio}/{fin}/{actividad}"){backStackEntry->
            val lugarInicio = backStackEntry.arguments?.getString("inicio") ?: ""
            val lugarFinal = backStackEntry.arguments?.getString("fin") ?: ""
            val actividad = backStackEntry.arguments?.getString("actividad") ?: ""

            MiUbicacion(navController, lugarInicio, lugarFinal, actividad, miUbicacionViewModel)
        }
        composable(route= AppScreens.ResumenTrote.name){

            ResumenTrote(navController, miUbicacionViewModel)
        }
        composable(route = AppScreens.ResumenSesion.name){
            ResumenSesion(navController)
        }
        composable (route= AppScreens.MessageEmail.name){
            MessageEmail(navController)
        }
        composable (route= AppScreens.troteActivo.name){
            TroteActivo(navController)
        }
        composable (route = AppScreens.Progreso.name ){
            Progreso(navController)

        }
        composable (route = AppScreens.Camara.name){
            Camara (navController)
        }

    }

}




