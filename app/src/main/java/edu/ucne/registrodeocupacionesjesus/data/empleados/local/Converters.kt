package edu.ucne.registrodeocupacionesjesus.data.empleados.local

import androidx.room.TypeConverter
import edu.ucne.registrodeocupacionesjesus.data.horasExtra.local.TipoHoraExtra

import java.time.LocalDate

class Converters {
    @TypeConverter
    fun fromString(value: String?): LocalDate?{
        return value?.let { LocalDate.parse(it) }
    }

    @TypeConverter
    fun dateToString(date: LocalDate?): String?{
        return date?.toString()
    }
    @TypeConverter
    fun fromFrecuenciaPago(value: FrecuenciaPago): String{
        return value.name
    }
    @TypeConverter
    fun toFrecuenciaPago(value: String): FrecuenciaPago {
        return FrecuenciaPago.valueOf(value)
    }
    @TypeConverter
    fun fromTipoHoraExtra(value: TipoHoraExtra): String {
        return value.name
    }
    @TypeConverter
    fun toTipoHoraExtra(value: String): TipoHoraExtra {
        return TipoHoraExtra.valueOf(value)
    }
}