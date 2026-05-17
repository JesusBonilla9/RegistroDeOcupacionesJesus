package edu.ucne.registrodeocupacionesjesus.presentation.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import edu.ucne.registrodeocupacionesjesus.domain.empleados.model.Empleado
import edu.ucne.registrodeocupacionesjesus.presentation.empleados.edit.EditEmpleadoScreen
import edu.ucne.registrodeocupacionesjesus.presentation.empleados.list.EmpleadoListScreen
import edu.ucne.registrodeocupacionesjesus.presentation.ocupaciones.edit.EditOcupacionScreen
import edu.ucne.registrodeocupacionesjesus.presentation.ocupaciones.list.OcupacionListScreen

@Composable
fun MainNavigationHost(
    navController: NavHostController = rememberNavController(),
    innerPadding: PaddingValues
) {
    NavHost(
        navController = navController,
        startDestination = Screen.OcupacionList,
        modifier = Modifier.padding(innerPadding)
    ) {
        composable<Screen.OcupacionList> {
            OcupacionListScreen(
                onAddOcupacion = {
                    navController.navigate(Screen.OcupacionEdit(0))
                },
                onNavigateToEdit = { id ->
                    navController.navigate(Screen.OcupacionEdit(id))
                }
            )
        }

        composable<Screen.OcupacionEdit> {
            EditOcupacionScreen(
                onBack = {
                    navController.navigate(Screen.OcupacionList) {
                        popUpTo(Screen.OcupacionList) {
                            inclusive = true
                        }
                    }
                }
            )
        }
        composable<Screen.EmpleadoList> {
            EmpleadoListScreen(
                onAddEmpleado = {
                    navController.navigate(Screen.EmpleadoEdit(0))
                },
                onNavigateToEdit = { id ->
                    navController.navigate(Screen.EmpleadoEdit(id))
                }
            )
        }

        composable<Screen.EmpleadoEdit> {
            EditEmpleadoScreen(
                onBack = {
                    navController.navigate(Screen.EmpleadoList) {
                        popUpTo(Screen.EmpleadoList) {
                            inclusive = true
                        }
                    }
                }
            )
        }
    }
}