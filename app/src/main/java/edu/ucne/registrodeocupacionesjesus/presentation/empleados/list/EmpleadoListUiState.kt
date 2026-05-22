package edu.ucne.registrodeocupacionesjesus.presentation.empleados.list

import edu.ucne.registrodeocupacionesjesus.domain.empleados.model.Empleado

data class EmpleadoListUiState(
    val isLoading: Boolean = false,
    val empleados: List<Empleado> = emptyList(),
    val message: String? = null,
    val navigateToCreate: Boolean = false,
    val navigateToEditId: Int? = null,
    val error: String? = null
)