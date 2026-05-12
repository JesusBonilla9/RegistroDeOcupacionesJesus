package edu.ucne.registrodeocupacionesjesus.presentation.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import edu.ucne.registrodeocupacionesjesus.domain.model.Ocupacion
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.runtime.getValue
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.foundation.lazy.items

@Composable
fun OcupacionListScreen(
    viewModel: OcupacionListViewModel = hiltViewModel(),
    onAddOcupacion: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
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

    LaunchedEffect (state.message) {
        state.message?.let { message ->
            snackbarHostState.showSnackbar(message)
            onEvent(OcupacionListUiEvent.ClearMessage)
        }
    }

    Scaffold (
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddOcupacion,
                modifier = Modifier.testTag("fab_add")
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Agregar Ocupacion"
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
                        text = "No hay ocupaciones",
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
                        ) { ocupacion->
                            OcupacionItem(
                                ocupacion = ocupacion,
                                onDelete = {
                                    onEvent(OcupacionListUiEvent.Delete(ocupacion.ocupacionId))
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun OcupacionItem(
    ocupacion: Ocupacion,
    onDelete: () -> Unit
) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("ocupacion_item_${ocupacion.ocupacionId}")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = ocupacion.descripcion,
                    style = MaterialTheme.typography.bodyLarge
                )

                Text(
                    text = "RD$${ocupacion.sueldo}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            IconButton(
                onClick = onDelete,
                modifier = Modifier.testTag("btn_delete_${ocupacion.ocupacionId}")
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Eliminar ocupacion"
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun OcupacionListBodyPreview() {
    MaterialTheme {
        val state = OcupacionListUiState(
            isLoading = false,
            ocupaciones = listOf(
                Ocupacion(ocupacionId = 1, descripcion = "Ingeniero en sistemas", sueldo = 40000.0),
                Ocupacion(ocupacionId = 2, descripcion = "Doctor", sueldo = 60000.0)
            )
        )
        OcupacionListBody(state, {}, {})
    }
}
