package edu.ucne.registrodeocupacionesjesus.domain.ocupaciones.usecase

import edu.ucne.registrodeocupacionesjesus.domain.ocupaciones.model.Ocupacion
import edu.ucne.registrodeocupacionesjesus.domain.ocupaciones.repository.OcupacionRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class UpsertOcupacionUseCase @Inject constructor(
    private val repository: OcupacionRepository
) {
    suspend operator fun invoke(ocupacion: Ocupacion): Result<Int> {
       val listaActual = repository.observeOcupaciones().first().map{it.descripcion}
       val descriptionResult = validateDescription(ocupacion.descripcion, listaActual)
       if(!descriptionResult.isValid) {
           return Result.failure(IllegalArgumentException(descriptionResult.error))
       }
       val sueldoResult = validateSueldo(ocupacion.sueldo.toString())
       if(!sueldoResult.isValid) {
           return Result.failure(IllegalArgumentException(sueldoResult.error))
       }
       return runCatching { repository.upsert(ocupacion) }
    }
}