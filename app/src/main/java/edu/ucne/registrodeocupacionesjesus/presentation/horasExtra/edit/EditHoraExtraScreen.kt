package edu.ucne.registrodeocupacionesjesus.presentation.horasExtra.edit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import edu.ucne.registrodeocupacionesjesus.data.horasExtra.local.TipoHoraExtra
import java.time.Instant
import java.time.ZoneId

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditHoraExtraScreen(
    viewModel: EditHoraExtraViewModel = hiltViewModel(),
    onBack: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()

    var expandedEmpleado by remember { mutableStateOf(false) }
    var expandedTipo by remember { mutableStateOf(false) }

    LaunchedEffect(state.saved, state.deleted) {
        if (state.saved || state.deleted) {
            onBack()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (state.isNew) "Registrar Hora Extra" else "Editar Hora Extra") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Atras")
                    }
                },
                actions = {
                    if (!state.isNew) {
                        IconButton(onClick = { viewModel.onEvent(EditHoraExtraUiEvent.Delete) }) {
                            Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = MaterialTheme.colorScheme.error)
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
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            ExposedDropdownMenuBox(
                expanded = expandedEmpleado,
                onExpandedChange = { expandedEmpleado = it }
            ) {
                val empleadoSeleccionado = state.empleados.find { it.empleadoId == state.empleadoId }?.nombres ?: ""
                OutlinedTextField(
                    value = empleadoSeleccionado,
                    onValueChange = { },
                    readOnly = true,
                    label = { Text("Empleado") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedEmpleado) },
                    modifier = Modifier.fillMaxWidth().menuAnchor(),
                    isError = state.empleadoError != null,
                    supportingText = state.empleadoError?.let { { Text(it) } }
                )
                ExposedDropdownMenu(
                    expanded = expandedEmpleado,
                    onDismissRequest = { expandedEmpleado = false }
                ) {
                    if (state.empleados.isEmpty()) {
                        DropdownMenuItem(text = { Text("No hay empleados creados") }, onClick = { expandedEmpleado = false })
                    } else {
                        state.empleados.forEach { empleado ->
                            DropdownMenuItem(
                                text = { Text(empleado.nombres) },
                                onClick = {
                                    viewModel.onEvent(EditHoraExtraUiEvent.EmpleadoChanged(empleado.empleadoId))
                                    expandedEmpleado = false
                                }
                            )
                        }
                    }
                }
            }

            OutlinedTextField(
                value = state.fecha.toString(),
                onValueChange = { },
                label = { Text("Fecha") },
                modifier = Modifier.fillMaxWidth(),
                readOnly = true,
                trailingIcon = {
                    IconButton(onClick = { showDatePicker = true }) {
                        Icon(Icons.Default.DateRange, contentDescription = "Seleccionar fecha")
                    }
                },
                isError = state.fechaError != null,
                supportingText = state.fechaError?.let { { Text(it) } }
            )

            if (showDatePicker) {
                DatePickerDialog(
                    onDismissRequest = { showDatePicker = false },
                    confirmButton = {
                        TextButton(onClick = {
                            datePickerState.selectedDateMillis?.let { millis ->
                                val date = Instant.ofEpochMilli(millis).atZone(ZoneId.of("UTC")).toLocalDate()
                                viewModel.onEvent(EditHoraExtraUiEvent.FechaChanged(date))
                            }
                            showDatePicker = false
                        }) { Text("Aceptar") }
                    },
                    dismissButton = { TextButton(onClick = { showDatePicker = false }) { Text("Cancelar") } }
                ) {
                    DatePicker(state = datePickerState)
                }
            }

            OutlinedTextField(
                value = state.cantidadHoras,
                onValueChange = { viewModel.onEvent(EditHoraExtraUiEvent.CantidadHorasChanged(it)) },
                label = { Text("Cantidad de Horas extra") },
                modifier = Modifier.fillMaxWidth(),
                isError = state.cantidadHorasError != null,
                supportingText = state.cantidadHorasError?.let { { Text(it) } },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            ExposedDropdownMenuBox(
                expanded = expandedTipo,
                onExpandedChange = { expandedTipo = it }
            ) {
                OutlinedTextField(
                    value = state.tipoHoraExtra.descripcion,
                    onValueChange = { },
                    readOnly = true,
                    label = { Text("Tipo de Hora Extra") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedTipo) },
                    modifier = Modifier.fillMaxWidth().menuAnchor(),
                    isError = state.tipoHoraExtraError != null,
                    supportingText = state.tipoHoraExtraError?.let { { Text(it) } }
                )
                ExposedDropdownMenu(
                    expanded = expandedTipo,
                    onDismissRequest = { expandedTipo = false }
                ) {
                    TipoHoraExtra.entries.forEach { tipo ->
                        DropdownMenuItem(
                            text = { Text(tipo.descripcion) },
                            onClick = {
                                viewModel.onEvent(EditHoraExtraUiEvent.TipoHoraExtraChanged(tipo))
                                expandedTipo = false
                            }
                        )
                    }
                }
            }

            ElevatedCard(modifier = Modifier.fillMaxWidth().padding(top = 8.dp)) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "Total a pagar (Calculado)", style = MaterialTheme.typography.labelMedium)
                    if (state.esPuestoDireccion) {
                        Text(
                            text = "RD$ 0.00 (Es puesto de Dirección)",
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.error,
                            fontWeight = FontWeight.Bold
                        )
                    } else {
                        Text(
                            text = "RD$ ${state.recargo}",
                            style = MaterialTheme.typography.headlineMedium,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Button(
                onClick = { viewModel.onEvent(EditHoraExtraUiEvent.Save) },
                modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                enabled = !state.isSaving
            ) {
                if (state.isSaving) CircularProgressIndicator(modifier = Modifier.size(24.dp), color = MaterialTheme.colorScheme.onPrimary)
                else Text("Guardar Registro")
            }
        }
    }
}