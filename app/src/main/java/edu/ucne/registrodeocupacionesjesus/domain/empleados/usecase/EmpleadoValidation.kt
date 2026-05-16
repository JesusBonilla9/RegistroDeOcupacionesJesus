package edu.ucne.registrodeocupacionesjesus.domain.empleados.usecase

import java.time.LocalDate

data class EmpleadoValidation(
    val isValid: Boolean,
    val error: String? = null
)

fun validateNombres(nombres: String): EmpleadoValidation {
    return when {
        nombres.isBlank() -> EmpleadoValidation(false, "El nombre no puede estar vacio")
        nombres.trim().length < 3 -> EmpleadoValidation(false, "El nombre debe tener al menos 3 letras")
        else -> EmpleadoValidation(true)
    }
}

fun validateSueldo(sueldo: String): EmpleadoValidation {
    return when{
        sueldo.isBlank() -> EmpleadoValidation(false, "El sueldo no puede estar vacio")
        sueldo.toDoubleOrNull() == null -> EmpleadoValidation(false, "Debe ingresar un sueldo valido")
        sueldo.toDouble() <= 0.0 -> EmpleadoValidation(false, "El sueldo debe ser mayor que 0")
        else -> EmpleadoValidation(true)
    }
}

fun validateFecha(fecha: LocalDate): EmpleadoValidation {
    val hoy = LocalDate.now()
    return when {
        fecha.isAfter(hoy) ->
            EmpleadoValidation(false, "La fecha de ingreso no puede ser futura")
        else -> EmpleadoValidation(true)
    }
}

fun validateSexo(sexo: String): EmpleadoValidation {
    return if (sexo.isBlank()) {
        EmpleadoValidation(false, "Debe seleccionar el sexo del empleado")
    } else {
        EmpleadoValidation(true)
    }
}


