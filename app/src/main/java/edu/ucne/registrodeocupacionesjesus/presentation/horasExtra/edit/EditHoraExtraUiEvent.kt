package edu.ucne.registrodeocupacionesjesus.presentation.horasExtra.edit

import edu.ucne.registrodeocupacionesjesus.data.horasExtra.local.TipoHoraExtra
import java.time.LocalDate

sealed interface EditHoraExtraUiEvent {
    data class Load(val id: Int) : EditHoraExtraUiEvent
    data class EmpleadoChanged(val id: Int) : EditHoraExtraUiEvent
    data class FechaChanged(val date: LocalDate) : EditHoraExtraUiEvent
    data class CantidadHorasChanged(val cantidad: String) : EditHoraExtraUiEvent
    data class TipoHoraExtraChanged(val tipo: TipoHoraExtra) : EditHoraExtraUiEvent
    data object Save : EditHoraExtraUiEvent
    data object Delete : EditHoraExtraUiEvent
}