package edu.ucne.registrodeocupacionesjesus.data.ocupaciones.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
@Dao
interface OcupacionDao {
    @Upsert
    suspend fun upsert(entity: OcupacionEntity): Long

    @Delete
    suspend fun delete(entity: OcupacionEntity)

    @Query("Select * From ocupaciones ORDER BY ocupacionId DESC")
    fun observeAll(): Flow<List<OcupacionEntity>>

    @Query("Select * From ocupaciones WHERE ocupacionId = :id")
    suspend fun getById(id: Int): OcupacionEntity?

    @Query ("DELETE FROM ocupaciones WHERE ocupacionId = :id")
    suspend fun deleteById(id: Int)

    @Query("Select exists(Select 1 From ocupaciones WHERE ocupacionId = :id)")
    suspend fun exists(id: Int): Boolean
}