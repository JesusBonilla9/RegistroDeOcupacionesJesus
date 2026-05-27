package edu.ucne.registrodeocupacionesjesus.domain.horasExtra.usecase

import edu.ucne.registrodeocupacionesjesus.domain.horasExtra.model.HoraExtra
import edu.ucne.registrodeocupacionesjesus.domain.horasExtra.repository.HoraExtraRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveHorasExtraUseCase @Inject constructor(
    private val repository: HoraExtraRepository
) {
    operator fun invoke(): Flow<List<HoraExtra>> = repository.observeHorasExtra()
}