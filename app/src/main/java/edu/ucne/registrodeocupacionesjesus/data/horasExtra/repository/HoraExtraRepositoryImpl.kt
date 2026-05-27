package edu.ucne.registrodeocupacionesjesus.data.horasExtra.repository

import edu.ucne.registrodeocupacionesjesus.data.horasExtra.local.HoraExtraDao
import edu.ucne.registrodeocupacionesjesus.data.horasExtra.mapper.toDomain
import edu.ucne.registrodeocupacionesjesus.data.horasExtra.mapper.toEntity
import edu.ucne.registrodeocupacionesjesus.domain.horasExtra.model.HoraExtra
import edu.ucne.registrodeocupacionesjesus.domain.horasExtra.repository.HoraExtraRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class HoraExtraRepositoryImpl @Inject constructor(
    private val localDataSource : HoraExtraDao
): HoraExtraRepository {
    override fun observeHorasExtra(): Flow<List<HoraExtra>> {
        return localDataSource.observeAll().map { entities ->
            entities.map { it.toDomain() }
        }
    }
    override suspend fun getHoraExtra(id: Int): HoraExtra? {
        return localDataSource.getById(id)?.toDomain()
    }

    override suspend fun upsert(horaExtra: HoraExtra): Int {
        localDataSource.upsert(horaExtra.toEntity())
        return horaExtra.horaExtraId ?: 0
    }

    override suspend fun delete(id: Int) {
        localDataSource.deleteById(id)
    }

    override suspend fun exists(id: Int): Boolean{
        return localDataSource.exists(id)
    }
}