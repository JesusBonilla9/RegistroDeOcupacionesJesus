package edu.ucne.registrodeocupacionesjesus.domain.ocupaciones.usecase

import edu.ucne.registrodeocupacionesjesus.domain.ocupaciones.model.Ocupacion
import edu.ucne.registrodeocupacionesjesus.domain.ocupaciones.repository.OcupacionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveOcupacionesUseCase @Inject constructor(
    private val repository: OcupacionRepository
) {
    operator fun invoke(): Flow<List<Ocupacion>> = repository.observeOcupaciones()
}