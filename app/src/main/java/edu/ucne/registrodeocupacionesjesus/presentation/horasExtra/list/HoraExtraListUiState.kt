package edu.ucne.registrodeocupacionesjesus.presentation.horasExtra.list

import edu.ucne.registrodeocupacionesjesus.domain.empleados.model.Empleado
import edu.ucne.registrodeocupacionesjesus.domain.horasExtra.model.HoraExtra

data class HoraExtraListUiState(
    val isLoading : Boolean = false,
    val horasExtra: List<HoraExtra> = emptyList(),
    val empleados : List<Empleado> = emptyList(),
    val message: String? = null,
    val navigateToCreate: Boolean = false,
    val navigateToEditId: Int? = null,
    val error: String? = null
)
