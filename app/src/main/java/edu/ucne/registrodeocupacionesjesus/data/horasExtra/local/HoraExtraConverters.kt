package edu.ucne.registrodeocupacionesjesus.data.horasExtra.local

import androidx.room.TypeConverter
import edu.ucne.registrodeocupacionesjesus.data.empleados.local.FrecuenciaPago
import java.time.LocalDate

class HoraExtraConverters {
    @TypeConverter
    fun fromString(value: String?): LocalDate?{
        return value?.let { LocalDate.parse(it) }
    }

    @TypeConverter
    fun dateToString(date: LocalDate?): String?{
        return date?.toString()
    }
    @TypeConverter
    fun fromTipoHoraExtra(value: TipoHoraExtra): String {
        return value.descripcion
    }
    @TypeConverter
    fun toTipoHoraExtra(value: String): TipoHoraExtra {
        return TipoHoraExtra.valueOf(value)
    }
}