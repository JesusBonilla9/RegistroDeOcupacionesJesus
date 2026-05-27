package edu.ucne.registrodeocupacionesjesus.domain.horasExtra.usecase

import java.time.LocalDate


data class HoraExtraValidation(
    val isValid: Boolean,
    val error: String? = null
)

fun validateEmpleadoId(empleadoId: Int): HoraExtraValidation {
    return when {
        empleadoId <= 0 -> HoraExtraValidation(false, "Debe seleccionar un empleadoId")
        else -> HoraExtraValidation(true)
    }
}

fun validateCantidadHoras(cantidad: String): HoraExtraValidation {
    return when {
        cantidad.isBlank() -> HoraExtraValidation(false, "La cantidad de horas no puede estar vacía")
        cantidad.toIntOrNull() == null -> HoraExtraValidation(false, "Debe ingresar una cantidad válida")
        cantidad.toInt() <= 0 -> HoraExtraValidation(false, "La cantidad de horas debe ser mayor que 0")
        cantidad.toInt() > 80 -> HoraExtraValidation(false, "No puedes registrar mas de 80 horas en una semana")
        else -> HoraExtraValidation(true)
    }
}

fun validateTipoHoraExtra(tipo: String, cantidad: String): HoraExtraValidation {
    val horas = cantidad.toIntOrNull() ?: 0
    return when {
        tipo.isBlank() -> HoraExtraValidation(false, "Debe seleccionar un tipo de hora extra")
        horas > 24 && (tipo != "ALTO VOLUMEN" && tipo != "ALTO_VOLUMEN") ->
            HoraExtraValidation(false, "Para más de 24 horas, el tipo debe ser 'ALTO VOLUMEN'")
        else -> HoraExtraValidation(true)
    }
}


fun validateFechaHoraExtra(fecha: LocalDate): HoraExtraValidation {
    val hoy = LocalDate.now()
    return when {
        fecha.isAfter(hoy) -> HoraExtraValidation(false, "La fecha no puede ser futura")
        else -> HoraExtraValidation(true)
    }
}