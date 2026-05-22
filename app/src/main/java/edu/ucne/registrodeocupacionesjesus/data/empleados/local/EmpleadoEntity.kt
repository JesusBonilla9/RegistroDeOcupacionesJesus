package edu.ucne.registrodeocupacionesjesus.data.empleados.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "empleados")
data class EmpleadoEntity(
    @PrimaryKey(autoGenerate = true)
    val ocupacionId: Int = 0,
    val empleadoId : Int = 0,
    val fechaIngreso : LocalDate = LocalDate.now(),
    val nombres : String = "",
    val sexo : String = "",
    val sueldo : Double = 0.0,
    val frecuenciaPago: FrecuenciaPago
)

