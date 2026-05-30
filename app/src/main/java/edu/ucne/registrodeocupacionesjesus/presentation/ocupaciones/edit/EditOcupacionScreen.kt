package edu.ucne.registrodeocupacionesjesus.presentation.ocupaciones.edit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditOcupacionScreen(
    viewModel: EditOcupacionViewModel = hiltViewModel(),
    onBack: () -> Unit,
    ocupacionId: Int
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(ocupacionId) {
        viewModel.loadOcupacion(ocupacionId)
    }

    LaunchedEffect(state.saved, state.deleted) {
        if (state.saved || state.deleted) {
            onBack()
        }
    }
    LaunchedEffect(state.errorMessage) {
        state.errorMessage?.let { mensaje ->
            Toast.makeText(context, mensaje, Toast.LENGTH_LONG).show()
            viewModel.onEvent(EditOcupacionUiEvent.ClearError)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if(state.isNew) "Nueva Ocupación" else "Editar Ocupación") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Atras")
                    }
                },
                actions = {
                    if (!state.isNew) {
                        IconButton(
                            onClick = { viewModel.onEvent(EditOcupacionUiEvent.Delete) },
                            modifier = Modifier.testTag("btn_delete")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Eliminar Ocupación",
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = state.descripcion,
                onValueChange = { viewModel.onEvent(EditOcupacionUiEvent.DescriptionChanged(it)) },
                label = { Text("Descripción") },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("input_description"),
                isError = state.descripcionError != null,
                supportingText = state.descripcionError?.let { { Text(it) } },
                singleLine = false,
                minLines = 3,
                maxLines = 5
            )

            OutlinedTextField(
                value = state.sueldo,
                onValueChange = { viewModel.onEvent(EditOcupacionUiEvent.SueldoChanged(it)) },
                label = { Text("Sueldo (en pesos dominicanos)") },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("input_sueldo"),
                isError = state.sueldoError != null,
                supportingText = state.sueldoError?.let { { Text(it) } },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                singleLine = true
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "¿Es un puesto de dirección?",
                    style = MaterialTheme.typography.bodyLarge
                )
                Switch(
                    checked = state.esPuestoDireccion,
                    onCheckedChange = { viewModel.onEvent(EditOcupacionUiEvent.EsPuestoDireccionChanged(it)) },
                    modifier = Modifier.testTag("switch_direccion")
                )
            }

            Button(
                onClick = { viewModel.onEvent(EditOcupacionUiEvent.Save) },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("btn_save"),
                enabled = !state.isSaving
            ) {
                if (state.isSaving) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text("Guardar")
                }
            }
        }
    }
}