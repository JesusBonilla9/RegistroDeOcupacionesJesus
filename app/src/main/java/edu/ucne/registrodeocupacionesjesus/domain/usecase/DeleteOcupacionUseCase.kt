package edu.ucne.registrodeocupacionesjesus.domain.usecase

import edu.ucne.registrodeocupacionesjesus.domain.repository.OcupacionRepository
import javax.inject.Inject

class DeleteOcupacionUseCase @Inject constructor(
    private val repository: OcupacionRepository
) {
    suspend operator fun invoke(id: Int) = repository.delete(id)
}