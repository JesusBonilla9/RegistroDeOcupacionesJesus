package edu.ucne.registrodeocupacionesjesus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import edu.ucne.registrodeocupacionesjesus.presentation.navigation.MainNavigationHost
import edu.ucne.registrodeocupacionesjesus.presentation.navigation.Screen
import edu.ucne.registrodeocupacionesjesus.ui.theme.RegistroDeOcupacionesJesusTheme


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RegistroDeOcupacionesJesusTheme {
               RegistroOcupacionesAppUI()
            }
        }
    }
}

data class TopLevelRoute<T : Any>(
    val nombre: String,
    val ruta: T,
    val icono: androidx.compose.ui.graphics.vector.ImageVector
)

