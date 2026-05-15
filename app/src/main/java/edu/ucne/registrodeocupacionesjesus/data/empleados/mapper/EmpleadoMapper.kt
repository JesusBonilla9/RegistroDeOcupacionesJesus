package edu.ucne.registrodeocupacionesjesus.data.empleados.mapper

import edu.ucne.registrodeocupacionesjesus.data.empleados.local.EmpleadoEntity
import edu.ucne.registrodeocupacionesjesus.domain.empleados.model.Empleado

fun EmpleadoEntity.toDomain() = Empleado(
    empleadoId = empleadoId,
    fechaIngreso = fechaIngreso,
    nombres = nombres,
    sexo = sexo,
    sueldo = sueldo
)

fun Empleado.toEntity() = EmpleadoEntity(
    empleadoId = empleadoId,
    fechaIngreso = fechaIngreso,
    nombres = nombres,
    sexo = sexo,
    sueldo = sueldo
)