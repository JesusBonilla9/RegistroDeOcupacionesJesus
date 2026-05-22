package edu.ucne.registrodeocupacionesjesus.data.horasExtra.local

import androidx.room.TypeConverter
import edu.ucne.registrodeocupacionesjesus.data.empleados.local.FrecuenciaPago

class HoraExtraConverters {
    @TypeConverter
    fun fromTipoHoraExtra(value: TipoHoraExtra): String {
        return value.descripcion
    }
    @TypeConverter
    fun toTipoHoraExtra(value: String): TipoHoraExtra {
        return TipoHoraExtra.valueOf(value)
    }
}