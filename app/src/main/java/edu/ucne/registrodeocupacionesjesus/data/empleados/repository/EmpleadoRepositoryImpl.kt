package edu.ucne.registrodeocupacionesjesus.data.empleados.repository


import edu.ucne.registrodeocupacionesjesus.data.empleados.local.EmpleadoDao
import edu.ucne.registrodeocupacionesjesus.data.empleados.mapper.toDomain
import edu.ucne.registrodeocupacionesjesus.data.empleados.mapper.toEntity
import edu.ucne.registrodeocupacionesjesus.domain.empleados.model.Empleado
import edu.ucne.registrodeocupacionesjesus.domain.empleados.repository.EmpleadoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class EmpleadoRepositoryImpl @Inject constructor(
    private val localDataSource: EmpleadoDao
) :  EmpleadoRepository{
    override fun observeEmpleados(): Flow<List<Empleado>> {
        return localDataSource.observeAll().map { entities ->
            entities.map { it.toDomain() }
        }
    }
    override suspend fun getEmpleado(id: Int): Empleado? {
        return localDataSource.getById(id)?.toDomain()
    }

    override suspend fun upsert(empleado: Empleado): Int {
        localDataSource.upsert(empleado.toEntity())
        return empleado.empleadoId ?: 0
    }

    override suspend fun delete(id: Int) {
        localDataSource.deleteById(id)
    }

    override suspend fun exists(id: Int): Boolean{
        return localDataSource.exists(id)
    }
}