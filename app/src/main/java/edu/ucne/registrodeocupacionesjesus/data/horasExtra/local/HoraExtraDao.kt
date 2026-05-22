package edu.ucne.registrodeocupacionesjesus.data.horasExtra.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import edu.ucne.registrodeocupacionesjesus.data.ocupaciones.local.OcupacionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HoraExtraDao {
    @Upsert
    suspend fun upsert(entity: HoraExtraEntity)

    @Delete
    suspend fun delete(entity: HoraExtraEntity)

    @Query("Select * From horasExtra ORDER BY horaExtraId DESC")
    fun observeAll(): Flow<List<HoraExtraEntity>>

    @Query("Select * From horasExtra WHERE horaExtraId = :id")
    suspend fun getById(id: Int): HoraExtraEntity?

    @Query ("DELETE FROM horasExtra WHERE horaExtraId = :id")
    suspend fun deleteById(id: Int)

    @Query("Select exists(Select 1 From horasExtra WHERE horaExtraId = :id)")
    suspend fun exists(id: Int): Boolean
}