package edu.ucne.registrodeocupacionesjesus.domain.empleados.usecase
import edu.ucne.registrodeocupacionesjesus.domain.empleados.model.Empleado
import edu.ucne.registrodeocupacionesjesus.domain.empleados.repository.EmpleadoRepository
import javax.inject.Inject

class UpsertEmpleadoUseCase @Inject constructor(
    private val repository: EmpleadoRepository
) {
    suspend operator fun invoke(empleado: Empleado): Result<Int> {
        val nombresResult = validateNombres(empleado.nombres)
        if (!nombresResult.isValid) {
            return Result.failure(IllegalArgumentException(nombresResult.error))
        }
        val sueldoResult = validateSueldo(empleado.sueldo.toString())
        if (!sueldoResult.isValid) {
            return Result.failure(IllegalArgumentException(sueldoResult.error))
        }
        val fechaResult = validateFecha(empleado.fechaIngreso)
        if (!fechaResult.isValid) {
            return Result.failure(IllegalArgumentException(fechaResult.error))
        }
        val sexoResult = validateSexo(empleado.sexo)
        if (!sexoResult.isValid) {
            return Result.failure(IllegalArgumentException(sexoResult.error))
        }
        return runCatching { repository.upsert(empleado) }
    }
}