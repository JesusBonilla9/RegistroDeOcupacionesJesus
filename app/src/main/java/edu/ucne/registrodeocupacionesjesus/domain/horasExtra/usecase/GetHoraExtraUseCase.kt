package edu.ucne.registrodeocupacionesjesus.domain.horasExtra.usecase

import edu.ucne.registrodeocupacionesjesus.domain.horasExtra.repository.HoraExtraRepository
import javax.inject.Inject

class GetHoraExtraUseCase @Inject constructor(
    private val repository: HoraExtraRepository
) {
    suspend operator fun invoke(id: Int) = repository.getHoraExtra(id)
}