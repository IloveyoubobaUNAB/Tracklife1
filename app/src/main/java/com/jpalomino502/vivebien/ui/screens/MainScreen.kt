package com.jpalomino502.vivebien.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.jpalomino502.vivebien.navigation.ScreenHome
import com.jpalomino502.vivebien.ui.components.BottomBar

@Composable
fun MainScreen(rootNavController: NavHostController = rememberNavController()) {
    val bottomNavController: NavHostController = rememberNavController()

    Scaffold(
        bottomBar = { BottomBar(navController = bottomNavController) }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            NavHost(
                navController = bottomNavController,
                startDestination = ScreenHome.Home.route
            ) {
                composable(ScreenHome.Home.route) { HomeScreen() }
                composable(ScreenHome.Salud.route) { SaludScreen() }
                composable(ScreenHome.Actividad.route) { ActividadScreen() }
                composable(ScreenHome.Citas.route) { CitasScreen() }
                composable(ScreenHome.Perfil.route) { PerfilScreen(navController = rootNavController) }
            }
        }
    }
}
