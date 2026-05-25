package edu.ucne.registrodeocupacionesjesus.presentation.horasExtra.edit

import edu.ucne.registrodeocupacionesjesus.data.horasExtra.local.TipoHoraExtra
import edu.ucne.registrodeocupacionesjesus.domain.empleados.model.Empleado
import edu.ucne.registrodeocupacionesjesus.domain.ocupaciones.model.Ocupacion
import java.time.LocalDate

data class EditHoraExtraUiState(
    val horaExtraId: Int? = null,
    val empleadoId: Int = 0,
    val fecha: LocalDate = LocalDate.now(),
    val cantidadHoras: String = "",
    val tipoHoraExtra: TipoHoraExtra = TipoHoraExtra.DIURNA,
    val recargo: Double = 0.0,
    val esPuestoDireccion: Boolean = false,
    val empleados: List<Empleado> = emptyList(),
    val ocupaciones: List<Ocupacion> = emptyList(),
    val empleadoError: String? = null,
    val fechaError: String? = null,
    val cantidadHorasError: String? = null,
    val isSaving: Boolean = false,
    val isDeleting: Boolean = false,
    val isNew: Boolean = true,
    val saved: Boolean = false,
    val deleted: Boolean = false
)