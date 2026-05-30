package edu.ucne.registrodeocupacionesjesus.presentation.empleados.adaptative


import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import edu.ucne.registrodeocupacionesjesus.presentation.empleados.edit.EditEmpleadoScreen
import edu.ucne.registrodeocupacionesjesus.presentation.empleados.list.EmpleadoListScreen
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun EmpleadoAdaptiveScreen(){
    val navigator = rememberListDetailPaneScaffoldNavigator<Any>()
    val scope = rememberCoroutineScope()
    var selectedEmpleadoId by remember { mutableStateOf(0)}

    ListDetailPaneScaffold(
        directive = navigator.scaffoldDirective,
        value = navigator.scaffoldValue,
        listPane = {
            EmpleadoListScreen(
                onAddEmpleado = {
                    selectedEmpleadoId = 0
                    scope.launch {
                        navigator.navigateTo(ListDetailPaneScaffoldRole.Detail)
                    }
                },
                onNavigateToEdit = { id ->
                    selectedEmpleadoId = id
                    scope.launch {
                        navigator.navigateTo(ListDetailPaneScaffoldRole.Detail)
                    }
                }
            )
        },
        detailPane = {
            EditEmpleadoScreen(
                empleadoId = selectedEmpleadoId,
                onBack = {
                    scope.launch {
                        navigator.navigateBack()
                    }
                }
            )
        }
    )
}