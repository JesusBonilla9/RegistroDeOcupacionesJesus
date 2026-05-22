package edu.ucne.registrodeocupacionesjesus.data.horasExtra.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "horasExtra")
data class HoraExtraEntity(
    @PrimaryKey(autoGenerate = true)
    val horaExtraId: Int = 0,
    val empleadoId: Int = 0,
    val fecha: LocalDate = LocalDate.now(),
    val cantidadHoras : Int = 0,
    val tipoHoraExtra: TipoHoraExtra = TipoHoraExtra.DIURNO,
    val recargo: Double = 0.0,
    val esPuestoDireccion: Boolean = false
)