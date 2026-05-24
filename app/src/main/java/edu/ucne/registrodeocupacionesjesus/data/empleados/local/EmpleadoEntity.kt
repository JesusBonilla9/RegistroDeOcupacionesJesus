package edu.ucne.registrodeocupacionesjesus.data.empleados.local

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import edu.ucne.registrodeocupacionesjesus.data.ocupaciones.local.OcupacionEntity
import java.time.LocalDate

@Entity(
    tableName = "empleados",
    foreignKeys = [
        ForeignKey(
            entity = OcupacionEntity::class,
            parentColumns = ["ocupacionId"],
            childColumns = ["ocupacionId"],
            onDelete = ForeignKey.RESTRICT
        )
    ],
    indices = [Index("ocupacionId")]
)
data class EmpleadoEntity(
    @PrimaryKey(autoGenerate = true)
    val empleadoId : Int = 0,
    val ocupacionId: Int = 0,
    val fechaIngreso : LocalDate = LocalDate.now(),
    val nombres : String = "",
    val sexo : String = "",
    val sueldo : Double = 0.0,
    val frecuenciaPago: FrecuenciaPago = FrecuenciaPago.SEMANAL
)

