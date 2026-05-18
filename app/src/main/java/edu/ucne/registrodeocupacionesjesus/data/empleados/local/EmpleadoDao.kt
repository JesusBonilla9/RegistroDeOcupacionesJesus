package edu.ucne.registrodeocupacionesjesus.data.empleados.local
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface EmpleadoDao {
    @Upsert
    suspend fun upsert(entity: EmpleadoEntity)

    @Delete
    suspend fun delete(entity: EmpleadoEntity)

    @Query("Select * From Empleados ORDER BY empleadoId DESC")
    fun observeAll(): Flow<List<EmpleadoEntity>>

    @Query("Select * From Empleados WHERE empleadoId = :id")
    suspend fun getById(id: Int): EmpleadoEntity?

    @Query ("DELETE FROM Empleados WHERE empleadoId = :id")
    suspend fun deleteById(id: Int)

    @Query("Select exists(Select 1 From Empleados WHERE empleadoId = :id)")
    suspend fun exists(id: Int): Boolean

}