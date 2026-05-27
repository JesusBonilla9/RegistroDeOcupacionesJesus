package edu.ucne.registrodeocupacionesjesus.data.empleados.local
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface EmpleadoDao {
    @Upsert
    suspend fun upsert(entity: EmpleadoEntity): Long

    @Delete
    suspend fun delete(entity: EmpleadoEntity)

    @Query("Select * From empleados ORDER BY empleadoId DESC")
    fun observeAll(): Flow<List<EmpleadoEntity>>

    @Query("Select * From empleados WHERE empleadoId = :id")
    suspend fun getById(id: Int): EmpleadoEntity?

    @Query ("DELETE FROM empleados WHERE empleadoId = :id")
    suspend fun deleteById(id: Int)

    @Query("Select exists(Select 1 From empleados WHERE empleadoId = :id)")
    suspend fun exists(id: Int): Boolean

}