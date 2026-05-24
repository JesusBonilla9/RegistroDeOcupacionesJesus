package edu.ucne.registrodeocupacionesjesus.presentation.ocupaciones.edit

sealed interface EditOcupacionUiEvent{
    data class Load(val id: Int?): EditOcupacionUiEvent
    data class DescriptionChanged(val value: String): EditOcupacionUiEvent
    data class SueldoChanged(val value: String): EditOcupacionUiEvent
    data class EsPuestoDireccionChanged(val value: Boolean): EditOcupacionUiEvent
    data object Save: EditOcupacionUiEvent
    data object Delete: EditOcupacionUiEvent
    data object ClearError: EditOcupacionUiEvent
}