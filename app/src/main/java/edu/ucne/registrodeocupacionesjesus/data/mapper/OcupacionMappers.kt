package edu.ucne.registrodeocupacionesjesus.data.mapper

import edu.ucne.registrodeocupacionesjesus.data.local.OcupacionEntity
import edu.ucne.registrodeocupacionesjesus.domain.model.Ocupacion

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