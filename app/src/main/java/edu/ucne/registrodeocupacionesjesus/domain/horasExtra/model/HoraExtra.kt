package edu.ucne.registrodeocupacionesjesus.domain.horasExtra.model

import edu.ucne.registrodeocupacionesjesus.data.horasExtra.local.TipoHoraExtra
import java.time.LocalDate

data class HoraExtra(
    val horaExtraId: Int = 0,
    val empleadoId: Int = 0,
    val fecha: LocalDate = LocalDate.now(),
    val cantidadHoras : Int = 0,
    val tipoHoraExtra: TipoHoraExtra = TipoHoraExtra.DIURNA,
    val recargo: Double = 0.0,
    val esPuestoDireccion: Boolean = false
)