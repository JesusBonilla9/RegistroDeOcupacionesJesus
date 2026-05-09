package edu.ucne.registrodeocupacionesjesus.domain.usecase

import edu.ucne.registrodeocupacionesjesus.domain.model.Ocupacion
import edu.ucne.registrodeocupacionesjesus.domain.repository.OcupacionRepository
import kotlinx.coroutines.flow.first

class UpsertOcupacionUseCase(
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