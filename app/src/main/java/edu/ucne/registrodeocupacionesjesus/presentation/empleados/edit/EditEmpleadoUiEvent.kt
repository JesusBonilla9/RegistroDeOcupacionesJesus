package edu.ucne.registrodeocupacionesjesus.presentation.empleados.edit
import java.time.LocalDate

sealed interface EditEmpleadoUiEvent {
    data class Load(val id: Int?) : EditEmpleadoUiEvent
    data class NombresChanged(val value: String) : EditEmpleadoUiEvent
    data class SexoChanged(val value: String) : EditEmpleadoUiEvent
    data class FechaIngresoChanged(val value: LocalDate) : EditEmpleadoUiEvent
    data class SueldoChanged(val value: String) : EditEmpleadoUiEvent
    data object Save : EditEmpleadoUiEvent
    data object Delete : EditEmpleadoUiEvent
}