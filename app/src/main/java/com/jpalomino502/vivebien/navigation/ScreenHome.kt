package com.jpalomino502.vivebien.navigation

sealed class ScreenHome(val route: String) {
    object Home : ScreenHome("home")
    object Salud : ScreenHome("salud")
    object Actividad : ScreenHome("actividad")
    object Citas : ScreenHome("citas")
    object Perfil : ScreenHome("perfil")
}
