package edu.ucne.registrodeocupacionesjesus.presentation.ocupaciones.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import edu.ucne.registrodeocupacionesjesus.domain.ocupaciones.model.Ocupacion

@Composable
fun OcupacionListScreen(
    viewModel: OcupacionListViewModel = hiltViewModel(),
    onAddOcupacion: () -> Unit,
    onNavigateToEdit: (Int) -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(state.navigateToCreate) {
        if (state.navigateToCreate) {
            onAddOcupacion()
        }
    }

    LaunchedEffect(state.navigateToEditId) {
        state.navigateToEditId?.let { id ->
            onNavigateToEdit(id)
        }
    }

    OcupacionListBody(state, viewModel::onEvent, onAddOcupacion)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OcupacionListBody(
    state: OcupacionListUiState,
    onEvent: (OcupacionListUiEvent) -> Unit,
    onAddOcupacion: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(state.message) {
        state.message?.let { message ->
            snackbarHostState.showSnackbar(message)
            onEvent(OcupacionListUiEvent.ClearMessage)
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddOcupacion,
                modifier = Modifier.testTag("fab_add")
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Agregar ocupación"
                )
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .testTag("loading")
                )
            } else {
                if (state.ocupaciones.isEmpty()) {
                    Text(
                        text = "No hay ocupaciones registradas",
                        modifier = Modifier
                            .align(Alignment.Center)
                            .testTag("empty_message"),
                        style = MaterialTheme.typography.bodyLarge
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(
                            items = state.ocupaciones,
                            key = { it.ocupacionId }
                        ) { ocupacion ->
                            OcupacionItem(
                                ocupacion = ocupacion,
                                onClick = {
                                    onEvent(OcupacionListUiEvent.Edit(ocupacion.ocupacionId))
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OcupacionItem(
    ocupacion: Ocupacion,
    onClick: () -> Unit
) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("ocupacion_item_${ocupacion.ocupacionId}"),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = ocupacion.descripcion,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.primary
            )

            Text(
                text = "Sueldo: RD$${ocupacion.sueldo}",
                style = MaterialTheme.typography.bodyMedium
            )

            if (ocupacion.esPuestoDireccion) {
                Text(
                    text = "Puesto de Dirección (No aplica horas extra)",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}