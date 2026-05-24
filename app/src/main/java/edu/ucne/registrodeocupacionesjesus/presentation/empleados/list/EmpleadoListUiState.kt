package edu.ucne.registrodeocupacionesjesus.presentation.empleados.list

import edu.ucne.registrodeocupacionesjesus.domain.empleados.model.Empleado
import edu.ucne.registrodeocupacionesjesus.domain.ocupaciones.model.Ocupacion

data class EmpleadoListUiState(
    val isLoading: Boolean = false,
    val empleados: List<Empleado> = emptyList(),
    val ocupaciones: List<Ocupacion> = emptyList(),
    val message: String? = null,
    val navigateToCreate: Boolean = false,
    val navigateToEditId: Int? = null,
    val error: String? = null
)