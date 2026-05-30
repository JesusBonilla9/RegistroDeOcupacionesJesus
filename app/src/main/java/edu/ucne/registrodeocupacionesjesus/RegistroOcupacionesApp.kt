package edu.ucne.registrodeocupacionesjesus

import android.app.Application
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.HiltAndroidApp
import edu.ucne.registrodeocupacionesjesus.presentation.navigation.MainNavigationHost
import edu.ucne.registrodeocupacionesjesus.presentation.navigation.Screen


@HiltAndroidApp
class RegistroOcupacionesApp: Application() {
}
@Composable
fun RegistroOcupacionesAppUI() {
    val navAssistant = rememberNavController()
    val currentDestination = navAssistant.currentBackStackEntryAsState().value?.destination

    NavigationSuiteScaffold(
        navigationSuiteItems = {
            item(
                selected = currentDestination?.hierarchy?.any { it.hasRoute<Screen.OcupacionList>() } == true,
                onClick = { navAssistant.navigate(Screen.OcupacionList) },
                icon = { Icon(Icons.Default.Work, contentDescription = "Ocupaciones") },
                label = { Text("Ocupaciones") }
            )
            item(
                selected = currentDestination?.hierarchy?.any { it.hasRoute<Screen.EmpleadoList>() } == true,
                onClick = { navAssistant.navigate(Screen.EmpleadoList) },
                icon = { Icon(Icons.Default.Person, contentDescription = "Empleados") },
                label = { Text("Empleados") }
            )
            item(
                selected = currentDestination?.hierarchy?.any { it.hasRoute<Screen.HoraExtraList>() } == true,
                onClick = { navAssistant.navigate(Screen.HoraExtraList) },
                icon = { Icon(Icons.Default.AccessTime, contentDescription = "Hora Extra") },
                label = { Text("Hora Extra") }
            )
        }
    ) {
        MainNavigationHost(navController = navAssistant)
    }
}