package edu.ucne.registrodeocupacionesjesus.data.horasExtra.local

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import edu.ucne.registrodeocupacionesjesus.data.empleados.local.EmpleadoEntity
import java.time.LocalDate

@Entity(
    tableName = "horasExtra",
    foreignKeys = [
        ForeignKey(
            entity = EmpleadoEntity::class,
            parentColumns = ["empleadoId"],
            childColumns = ["empleadoId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("empleadoId")]
)
data class HoraExtraEntity(
    @PrimaryKey(autoGenerate = true)
    val horaExtraId: Int = 0,
    val empleadoId: Int = 0,
    val fecha: LocalDate = LocalDate.now(),
    val cantidadHoras : Int = 0,
    val tipoHoraExtra: TipoHoraExtra = TipoHoraExtra.DIURNA,
    val recargo: Double = 0.0,
    val esPuestoDireccion: Boolean = false
)