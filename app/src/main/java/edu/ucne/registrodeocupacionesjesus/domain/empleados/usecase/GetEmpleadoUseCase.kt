package edu.ucne.registrodeocupacionesjesus.domain.empleados.usecase

import edu.ucne.registrodeocupacionesjesus.domain.empleados.repository.EmpleadoRepository
import javax.inject.Inject

class GetEmpleadoUseCase @Inject constructor(
    private val repository: EmpleadoRepository
) {
    suspend operator fun invoke(id: Int) = repository.getEmpleado(id)
}