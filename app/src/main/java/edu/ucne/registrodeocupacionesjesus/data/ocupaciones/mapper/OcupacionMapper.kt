package edu.ucne.registrodeocupacionesjesus.data.ocupaciones.mapper

import edu.ucne.registrodeocupacionesjesus.data.ocupaciones.local.OcupacionEntity
import edu.ucne.registrodeocupacionesjesus.domain.ocupaciones.model.Ocupacion

fun OcupacionEntity.toDomain() = Ocupacion(
    ocupacionId = ocupacionId,
    descripcion = descripcion,
    sueldo = sueldo
)

fun Ocupacion.toEntity() = OcupacionEntity(
    ocupacionId = ocupacionId,
    descripcion = descripcion,
    sueldo = sueldo
)