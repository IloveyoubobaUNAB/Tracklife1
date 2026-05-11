package com.jpalomino502.vivebien.navigation

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Main : Screen("main")
}
