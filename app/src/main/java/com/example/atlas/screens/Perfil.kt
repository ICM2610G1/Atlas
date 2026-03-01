package com.example.atlas.screens

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.atlas.R
import com.example.atlas.elements.ScaffoldDesign

@Composable
fun Perfil (controller : NavController){

    ScaffoldDesign("Home",R.color.pink,controller)

}