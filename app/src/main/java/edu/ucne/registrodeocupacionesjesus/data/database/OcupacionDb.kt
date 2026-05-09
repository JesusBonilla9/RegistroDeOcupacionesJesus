package edu.ucne.registrodeocupacionesjesus.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import edu.ucne.registrodeocupacionesjesus.data.local.OcupacionDao
import edu.ucne.registrodeocupacionesjesus.data.local.OcupacionEntity

@Database(
    entities = [OcupacionEntity :: class],
    version = 1
)
abstract class OcupacionDb : RoomDatabase() {
    abstract fun OcupacionDao(): OcupacionDao
}