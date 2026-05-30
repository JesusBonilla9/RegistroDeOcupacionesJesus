package edu.ucne.registrodeocupacionesjesus.presentation.ocupaciones.adaptative


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
import edu.ucne.registrodeocupacionesjesus.presentation.ocupaciones.edit.EditOcupacionScreen
import edu.ucne.registrodeocupacionesjesus.presentation.ocupaciones.list.OcupacionListScreen
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun OcupacionAdaptiveScreen(){
    val navigator = rememberListDetailPaneScaffoldNavigator<Any>()
    val scope = rememberCoroutineScope()
    var selectedOcupacionId by remember { mutableStateOf(0)}

    ListDetailPaneScaffold(
        directive = navigator.scaffoldDirective,
        value = navigator.scaffoldValue,
        listPane = {
            OcupacionListScreen(
                onAddOcupacion = {
                    selectedOcupacionId = 0
                    scope.launch {
                        navigator.navigateTo(ListDetailPaneScaffoldRole.Detail)
                    }
                },
                onNavigateToEdit = { id ->
                    selectedOcupacionId = id
                    scope.launch {
                        navigator.navigateTo(ListDetailPaneScaffoldRole.Detail)
                    }
                }
            )
        },
        detailPane = {
            EditOcupacionScreen(
                ocupacionId = selectedOcupacionId,
                onBack = {
                    scope.launch {
                        navigator.navigateBack()
                    }
                }
            )
        }
    )
}