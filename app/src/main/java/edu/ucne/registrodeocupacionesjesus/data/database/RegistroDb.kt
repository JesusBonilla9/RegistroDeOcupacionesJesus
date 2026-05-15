package edu.ucne.registrodeocupacionesjesus.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import edu.ucne.registrodeocupacionesjesus.data.empleados.local.EmpleadoDao
import edu.ucne.registrodeocupacionesjesus.data.empleados.local.EmpleadoEntity
import edu.ucne.registrodeocupacionesjesus.data.ocupaciones.local.OcupacionDao
import edu.ucne.registrodeocupacionesjesus.data.ocupaciones.local.OcupacionEntity

@Database(
    entities = [OcupacionEntity :: class, EmpleadoEntity :: class],
    version = 2
)
abstract class RegistroDb : RoomDatabase() {
    abstract fun OcupacionDao(): OcupacionDao
    abstract fun EmpleadoDao(): EmpleadoDao
}