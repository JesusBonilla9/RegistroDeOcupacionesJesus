package edu.ucne.registrodeocupacionesjesus.domain.usecase

import edu.ucne.registrodeocupacionesjesus.domain.model.Ocupacion
import edu.ucne.registrodeocupacionesjesus.domain.repository.OcupacionRepository
import kotlinx.coroutines.flow.Flow

class ObserveOcupacionesUseCase(
    private val repository: OcupacionRepository
) {
    operator fun invoke(): Flow<List<Ocupacion>> = repository.observeOcupaciones()
}