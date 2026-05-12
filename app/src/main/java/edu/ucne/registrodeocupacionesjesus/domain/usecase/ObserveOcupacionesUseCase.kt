package edu.ucne.registrodeocupacionesjesus.domain.usecase

import edu.ucne.registrodeocupacionesjesus.domain.model.Ocupacion
import edu.ucne.registrodeocupacionesjesus.domain.repository.OcupacionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveOcupacionesUseCase @Inject constructor(
    private val repository: OcupacionRepository
) {
    operator fun invoke(): Flow<List<Ocupacion>> = repository.observeOcupaciones()
}