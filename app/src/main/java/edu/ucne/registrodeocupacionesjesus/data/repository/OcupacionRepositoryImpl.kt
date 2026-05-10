package edu.ucne.registrodeocupacionesjesus.data.repository

import edu.ucne.registrodeocupacionesjesus.data.local.OcupacionDao
import edu.ucne.registrodeocupacionesjesus.data.mapper.toDomain
import edu.ucne.registrodeocupacionesjesus.data.mapper.toEntity
import edu.ucne.registrodeocupacionesjesus.domain.model.Ocupacion
import edu.ucne.registrodeocupacionesjesus.domain.repository.OcupacionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class OcupacionRepositoryImpl @Inject constructor(
    private val localDataSource: OcupacionDao
) :  OcupacionRepository{
    override fun observeOcupaciones(): Flow<List<Ocupacion>> {
        return localDataSource.observeAll().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun getOcupacion(id: Int): Ocupacion? {
        return localDataSource.getById(id)?.toDomain()
    }

    override suspend fun upsert(ocupacion: Ocupacion): Int {
        localDataSource.upsert(ocupacion.toEntity())
        return ocupacion.ocupacionId ?: 0
    }

    override suspend fun delete(id: Int) {
        localDataSource.deleteById(id)
    }

    override suspend fun exists(id: Int): Boolean{
        return localDataSource.exists(id)
    }
}