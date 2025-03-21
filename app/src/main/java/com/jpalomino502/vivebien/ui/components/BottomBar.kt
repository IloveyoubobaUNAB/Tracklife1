package com.jpalomino502.vivebien.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.jpalomino502.vivebien.navigation.ScreenHome

data class BottomNavItem(
    val route: String,
    val icon: ImageVector,
    val label: String
)

val bottomNavItems = listOf(
    BottomNavItem(
        route = ScreenHome.Home.route,
        icon = Icons.Default.Home,
        label = "Inicio"
    ),
    BottomNavItem(
        route = ScreenHome.Salud.route,
        icon = Icons.Default.Favorite,
        label = "Salud"
    ),
    BottomNavItem(
        route = ScreenHome.Actividad.route,
        icon = Icons.Default.PlayArrow,
        label = "Actividad"
    ),
    BottomNavItem(
        route = ScreenHome.Citas.route,
        icon = Icons.Default.DateRange,
        label = "Citas"
    ),
    BottomNavItem(
        route = ScreenHome.Perfil.route,
        icon = Icons.Default.Person,
        label = "Perfil"
    ),
)

@Composable
fun BottomBar(navController: NavController) {
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry.value?.destination?.route

    NavigationBar {
        bottomNavItems.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Icon(imageVector = item.icon, contentDescription = item.label)
                },
                label = { Text(item.label) }
            )
        }
    }
}
