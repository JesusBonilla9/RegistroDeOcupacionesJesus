package edu.ucne.registrodeocupacionesjesus.domain.ocupaciones.usecase


data class OcupacionValidation(
    val isValid: Boolean,
    val error: String? = null
)

fun validateDescription(descripcion: String, ocupacionesExistentes: List<String>): OcupacionValidation {
    return when{
        descripcion.isBlank() -> OcupacionValidation(false, "La descripcion no puede estar vacia")
        descripcion.length < 3 -> OcupacionValidation(false, "La descripcion debe tener al menos 3 caracteres")
        ocupacionesExistentes.any{it.equals(descripcion.trim(), ignoreCase = true)} -> OcupacionValidation(false, "Ya existe una ocupacion con esa descripcion")
        else -> OcupacionValidation(true)
    }
}

fun validateSueldo(sueldo: String): OcupacionValidation {
    return when{
        sueldo.isBlank() -> OcupacionValidation(false, "El sueldo no puede estar vacio")
        sueldo.toDoubleOrNull() == null -> OcupacionValidation(false, "Debe ingresar un sueldo valido")
        sueldo.toDouble() <= 0.0 -> OcupacionValidation(false, "El sueldo debe ser mayor que 0")
        else -> OcupacionValidation(true)
    }
}

