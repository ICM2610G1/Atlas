package com.example.atlas.navegation

import com.example.atlas.notificaciones.TrotePendienteNotificacion
import androidx.compose.runtime.LaunchedEffect
import com.example.atlas.notificaciones.ChatPendienteNotificacion
import EjercicioViewModel
import Pantallas.PantallaEjercicios
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.atlas.screens.AppStart
import com.example.atlas.screens.Calificar
import com.example.atlas.screens.Camara
import com.example.atlas.screens.CatalogoActividades
import com.example.atlas.screens.Chats
import com.example.atlas.screens.ChequeoSesion
import com.example.atlas.screens.DetalleSesion
import com.example.atlas.screens.Historial
import com.example.atlas.screens.Home
import com.example.atlas.screens.HomeCoach
import com.example.atlas.screens.LogIn
import com.example.atlas.screens.Perfil
import com.example.atlas.screens.PerfilEnt
import com.example.atlas.screens.Ubicacion
import com.example.atlas.screens.CrearNuevaSesion
import com.example.atlas.screens.DetallesTrote
import com.example.atlas.screens.ElegirTipo
import com.example.atlas.screens.Evolucion
import com.example.atlas.screens.MessageEmail
import com.example.atlas.screens.MiUbicacion
import com.example.atlas.screens.Progreso
import com.example.atlas.screens.RecoverPassword
import com.example.atlas.screens.ResumenSesion
import com.example.atlas.screens.ResumenTrote
import com.example.atlas.screens.SignUpDep
import com.example.atlas.screens.SignUpEnt
import com.example.atlas.screens.TroteActivo
import com.example.atlas.screens.chatP
import com.example.atlas.screens.vistaSesionesEntrenador
import com.example.atlas.viewmodels.CrearSesionViewModel
import com.example.atlas.viewmodels.ModeloMiUbicacion
import com.example.atlas.viewmodels.ResumenSesionViewModel

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
    SignUpDep,
    SignUpEnt,
    Elegirtipo,
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
    MessageEmail,
    troteActivo,
    ResumenTrote,

    agregarEjercicio,
    Camara,
    Progreso,
    Evolucion

}

@Composable
fun Navigation(){
    val navController = rememberNavController()
    val ejercicioViewModel: EjercicioViewModel = viewModel()
    val miUbicacionViewModel: ModeloMiUbicacion = viewModel()
    val sesionViewModel: CrearSesionViewModel = viewModel()
    val resumenViewModel: ResumenSesionViewModel = viewModel()
    LaunchedEffect(Unit) {
        if (ChatPendienteNotificacion.hayChatPendiente()) {
            val idChat = ChatPendienteNotificacion.idChat
            val nombre = ChatPendienteNotificacion.nombre

            navController.navigate("${AppScreens.chatsP.name}/$idChat/$nombre")

            ChatPendienteNotificacion.limpiar()
        }
    }
    LaunchedEffect(Unit) {
        if (TrotePendienteNotificacion.hayTrotePendiente()) {
            val idDeportista = TrotePendienteNotificacion.idDeportista

            navController.navigate("${AppScreens.Ubicacion.name}/$idDeportista")

            TrotePendienteNotificacion.limpiar()
        }
    }
    NavHost(navController, startDestination = AppScreens.Appstart.name){
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
        composable(route = AppScreens.chatsP.name + "/{idChat}/{nombre}") { backStackEntry ->

            val idChat = backStackEntry.arguments?.getString("idChat") ?: ""
            val nombre = backStackEntry.arguments?.getString("nombre") ?: ""
            chatP(
                idChat = idChat,
                contacto = nombre,
                controller = navController
            )
        }
        composable(route= AppScreens.vistaSesionesEnt.name) {
            vistaSesionesEntrenador(navController)
        }
        composable (route= AppScreens.CrearNuevaSesion.name){
            CrearNuevaSesion(navController, sesionViewModel)
        }
        composable (route= AppScreens.Ejercicios.name+"/{ejercicio}"){ backStackEntry ->
            val ejercicio = backStackEntry.arguments?.getString("ejercicio")
            PantallaEjercicios(controller = navController, ejercicio = ejercicio, modelEjercicios = ejercicioViewModel, modelSesion = sesionViewModel )
        }
        composable (route= AppScreens.Ejercicios.name){
            PantallaEjercicios(controller = navController, modelEjercicios = ejercicioViewModel, modelSesion = sesionViewModel )
        }

        composable(route = AppScreens.agregarEjercicio.name) {
            CatalogoActividades(controller = navController, modelE = ejercicioViewModel)
        }

        composable (route= AppScreens.ChequeoSesion.name){
            ChequeoSesion(controller = navController, viewModel = ejercicioViewModel)
        }
        composable (route= AppScreens.ChequeoSesion.name){
            ChequeoSesion(controller = navController, viewModel = ejercicioViewModel)
        }
        composable (route= AppScreens.detallesTrote.name+"/{idSesion}"){backStackEntry ->
            val idSesion = backStackEntry.arguments?.getString("idSesion")
            DetallesTrote(controller = navController, idSesion = idSesion)
        }
        composable (route= AppScreens.Ubicacion.name+"/{idDep}"){backStackEntry->
            val idDeportista= backStackEntry.arguments?.getString("idDep")?:""
            Ubicacion(navController,idDeportista )
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
        composable (route= AppScreens.SignUpDep.name){
            SignUpDep(navController)
        }
        composable (route= AppScreens.SignUpEnt.name){
            SignUpEnt(navController)
        }
        composable (route= AppScreens.Elegirtipo.name){
            ElegirTipo(navController)
        }
        composable (route= AppScreens.LogIn.name){
            LogIn(navController)
        }
        composable (route= AppScreens.DetalleSesion.name+"/{idSesion}"){ backStackEntry ->
            val idSesion = backStackEntry.arguments?.getString("idSesion") ?: ""
            DetalleSesion(navController, idSesion)
        }
        composable (route= AppScreens.RecoverPassword.name){
            RecoverPassword(navController)
        }
        composable(route = AppScreens.Mimapa.name + "/{fin}/{actividad}") { backStackEntry ->
            val lugarFinal = backStackEntry.arguments?.getString("fin") ?: ""
            val actividad  = backStackEntry.arguments?.getString("actividad") ?: ""
            MiUbicacion(controller = navController, final= lugarFinal, actividad= actividad, modelo= miUbicacionViewModel, modeloSesion = sesionViewModel)
        }
        composable(route= AppScreens.ResumenTrote.name){
            ResumenTrote(navController, miUbicacionViewModel)
        }
        composable(route = AppScreens.ResumenSesion.name) {
            ResumenSesion(controller = navController, model = resumenViewModel, modelSesion = sesionViewModel)
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
        composable (route= AppScreens.Evolucion.name){
            Evolucion(navController)
        }

    }

}
