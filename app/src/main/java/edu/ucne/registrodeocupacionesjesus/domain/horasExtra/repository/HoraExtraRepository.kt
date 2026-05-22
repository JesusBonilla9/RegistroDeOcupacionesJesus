package edu.ucne.registrodeocupacionesjesus.domain.horasExtra.repository

import edu.ucne.registrodeocupacionesjesus.domain.horasExtra.model.HoraExtra
import kotlinx.coroutines.flow.Flow

interface HoraExtraRepository {
    fun observeHorasExtra(): Flow<List<HoraExtra>>
    suspend fun getHoraExtra(id: Int): HoraExtra?
    suspend fun upsert(horaExtra: HoraExtra): Int
    suspend fun delete(id: Int)
    suspend fun exists(id: Int): Boolean
}