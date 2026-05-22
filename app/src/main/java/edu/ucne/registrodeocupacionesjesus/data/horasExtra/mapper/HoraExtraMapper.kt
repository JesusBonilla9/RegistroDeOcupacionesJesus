package edu.ucne.registrodeocupacionesjesus.data.horasExtra.mapper

import edu.ucne.registrodeocupacionesjesus.data.horasExtra.local.HoraExtraEntity
import edu.ucne.registrodeocupacionesjesus.domain.horasExtra.model.HoraExtra

fun HoraExtraEntity.toDomain() = HoraExtra(
    horaExtraId = horaExtraId,
    empleadoId = empleadoId,
    fecha = fecha,
    cantidadHoras = cantidadHoras,
    tipoHoraExtra = tipoHoraExtra,
    recargo = recargo,
    esPuestoDireccion = esPuestoDireccion
)

fun HoraExtra.toEntity() = HoraExtraEntity(
    horaExtraId = horaExtraId,
    empleadoId = empleadoId,
    fecha = fecha,
    cantidadHoras = cantidadHoras,
    tipoHoraExtra = tipoHoraExtra,
    recargo = recargo,
    esPuestoDireccion = esPuestoDireccion
)