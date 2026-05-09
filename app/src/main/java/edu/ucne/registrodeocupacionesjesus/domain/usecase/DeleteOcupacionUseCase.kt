package edu.ucne.registrodeocupacionesjesus.domain.usecase

import edu.ucne.registrodeocupacionesjesus.domain.repository.OcupacionRepository

class DeleteOcupacionUseCase (
    private val repository: OcupacionRepository
) {
    suspend operator fun invoke(id: Int) = repository.delete(id)
}