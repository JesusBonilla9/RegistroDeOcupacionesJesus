package edu.ucne.registrodeocupacionesjesus.domain.horasExtra.usecase
import edu.ucne.registrodeocupacionesjesus.domain.horasExtra.model.HoraExtra
import edu.ucne.registrodeocupacionesjesus.domain.horasExtra.repository.HoraExtraRepository
import javax.inject.Inject

class UpsertHoraExtraUseCase @Inject constructor(
    private val repository: HoraExtraRepository
) {
    suspend operator fun invoke(horaExtra: HoraExtra): Result<Int> {
        val empleadoResult = validateEmpleadoId(horaExtra.empleadoId)
        if (!empleadoResult.isValid) {
            return Result.failure(IllegalArgumentException(empleadoResult.error))
        }

        val fechaResult = validateFechaHoraExtra(horaExtra.fecha)
        if (!fechaResult.isValid) {
            return Result.failure(IllegalArgumentException(fechaResult.error))
        }

        val horasResult = validateCantidadHoras(horaExtra.cantidadHoras.toString())
        if (!horasResult.isValid) {
            return Result.failure(IllegalArgumentException(horasResult.error))
        }

        val tipoResult = validateTipoHoraExtra(horaExtra.tipoHoraExtra.name, cantidad = horaExtra.cantidadHoras.toString())
        if (!tipoResult.isValid) {
            return Result.failure(IllegalArgumentException(tipoResult.error))
        }

        return runCatching { repository.upsert(horaExtra) }
    }
}