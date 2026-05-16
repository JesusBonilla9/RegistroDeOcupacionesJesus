package edu.ucne.registrodeocupacionesjesus.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import edu.ucne.registrodeocupacionesjesus.data.empleados.local.Converters
import edu.ucne.registrodeocupacionesjesus.data.empleados.local.EmpleadoDao
import edu.ucne.registrodeocupacionesjesus.data.empleados.local.EmpleadoEntity
import edu.ucne.registrodeocupacionesjesus.data.ocupaciones.local.OcupacionDao
import edu.ucne.registrodeocupacionesjesus.data.ocupaciones.local.OcupacionEntity

@Database(
    entities = [OcupacionEntity :: class, EmpleadoEntity :: class],
    version = 2
)
@TypeConverters(Converters::class)
abstract class RegistroDb : RoomDatabase() {
    abstract fun OcupacionDao(): OcupacionDao
    abstract fun EmpleadoDao(): EmpleadoDao
}