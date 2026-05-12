package edu.ucne.registrodeocupacionesjesus.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import edu.ucne.registrodeocupacionesjesus.presentation.edit.EditOcupacionScreen
import edu.ucne.registrodeocupacionesjesus.presentation.list.OcupacionListScreen

@Composable
fun OcupacionNavHost(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.OcupacionList
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
    }
}