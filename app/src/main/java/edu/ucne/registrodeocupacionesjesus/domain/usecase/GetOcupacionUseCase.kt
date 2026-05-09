package edu.ucne.registrodeocupacionesjesus.domain.usecase

import edu.ucne.registrodeocupacionesjesus.domain.repository.OcupacionRepository

class GetOcupacionUseCase(
    private val repository: OcupacionRepository
) {
    suspend operator fun invoke(id: Int) = repository.getOcupacion(id)
}