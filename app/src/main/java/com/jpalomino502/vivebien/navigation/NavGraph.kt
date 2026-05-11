package com.jpalomino502.vivebien.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.jpalomino502.vivebien.ui.screens.LoginScreen
import com.jpalomino502.vivebien.ui.screens.MainScreen
import com.jpalomino502.vivebien.ui.screens.RegisterScreen

@Composable
fun NavGraph(navController: NavHostController = rememberNavController()) {
    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    ) {
        composable(route = Screen.Login.route) {
            LoginScreen(navController = navController)
        }
        composable(route = Screen.Register.route) {
            RegisterScreen(navController = navController)
        }
        composable(route = Screen.Main.route) {
            MainScreen(rootNavController = navController)
        }
    }
}
