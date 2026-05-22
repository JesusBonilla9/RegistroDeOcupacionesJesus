package edu.ucne.registrodeocupacionesjesus.domain.ocupaciones.usecase

import edu.ucne.registrodeocupacionesjesus.domain.ocupaciones.repository.OcupacionRepository
import javax.inject.Inject

class DeleteOcupacionUseCase @Inject constructor(
    private val repository: OcupacionRepository
) {
    suspend operator fun invoke(id: Int) = repository.delete(id)
}