package edu.ucne.registrodeocupacionesjesus.data.empleados.mapper

import edu.ucne.registrodeocupacionesjesus.data.empleados.local.EmpleadoEntity
import edu.ucne.registrodeocupacionesjesus.domain.empleados.model.Empleado

fun EmpleadoEntity.toDomain() = Empleado(
    empleadoId = empleadoId,
    ocupacionId = ocupacionId,
    fechaIngreso = fechaIngreso,
    nombres = nombres,
    sexo = sexo,
    sueldo = sueldo,
    frecuenciaPago = frecuenciaPago
)

fun Empleado.toEntity() = EmpleadoEntity(
    empleadoId = empleadoId,
    ocupacionId = ocupacionId,
    fechaIngreso = fechaIngreso,
    nombres = nombres,
    sexo = sexo,
    sueldo = sueldo,
    frecuenciaPago = frecuenciaPago
)