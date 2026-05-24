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
        else -> HoraExtraValidation(true)
    }
}

fun validateTipoHoraExtra(tipo: String): HoraExtraValidation {
    return when {
        tipo.isBlank() -> HoraExtraValidation(false, "Debe seleccionar un tipo de hora extra")
        else -> HoraExtraValidation(true)
    }
}

fun validateRecargo(recargo: String): HoraExtraValidation {
    return when {
        recargo.isBlank() -> HoraExtraValidation(false, "El recargo no puede estar vacío")
        recargo.toDoubleOrNull() == null -> HoraExtraValidation(false, "Debe ingresar un recargo válido")
        recargo.toDouble() < 0.0 -> HoraExtraValidation(false, "El recargo no puede ser negativo")
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