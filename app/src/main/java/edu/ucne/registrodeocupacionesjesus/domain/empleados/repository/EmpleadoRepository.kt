package edu.ucne.registrodeocupacionesjesus.domain.empleados.repository

import edu.ucne.registrodeocupacionesjesus.domain.empleados.model.Empleado
import kotlinx.coroutines.flow.Flow

interface EmpleadoRepository {
    fun observeEmpleados(): Flow<List<Empleado>>
    suspend fun getEmpleado(id: Int): Empleado?
    suspend fun upsert(empleado: Empleado): Int
    suspend fun delete(id: Int)
    suspend fun exists(id: Int): Boolean
}
