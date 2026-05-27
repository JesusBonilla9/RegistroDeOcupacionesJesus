package edu.ucne.registrodeocupacionesjesus.presentation.empleados.edit

import edu.ucne.registrodeocupacionesjesus.data.empleados.local.FrecuenciaPago
import edu.ucne.registrodeocupacionesjesus.domain.ocupaciones.model.Ocupacion
import java.time.LocalDate

data class EditEmpleadoUiState(
    val empleadoId: Int? = null,
    val nombres: String = "",
    val sexo: String = "",
    val fechaIngreso: LocalDate = LocalDate.now(),
    val sueldo: String = "",
    val ocupacionId: Int = 0,
    val frecuenciaPago: FrecuenciaPago = FrecuenciaPago.SEMANAL,
    val ocupaciones: List<Ocupacion> = emptyList(),
    val nombresError: String? = null,
    val sexoError: String? = null,
    val fechaIngresoError: String? = null,
    val sueldoError: String? = null,
    val ocupacionError: String? = null,
    val frecuenciaPagoError: String? = null,
    val isSaving: Boolean = false,
    val isDeleting: Boolean = false,
    val isNew: Boolean = true,
    val saved: Boolean = false,
    val deleted: Boolean = false
)