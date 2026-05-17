package edu.ucne.registrodeocupacionesjesus.domain.empleados

import edu.ucne.registrodeocupacionesjesus.domain.empleados.model.Empleado
import edu.ucne.registrodeocupacionesjesus.domain.empleados.repository.EmpleadoRepository
import edu.ucne.registrodeocupacionesjesus.domain.empleados.usecase.GetEmpleadoUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import java.time.LocalDate

@ExperimentalCoroutinesApi
class GetEmpleadoUseCaseTest {

    private lateinit var useCase: GetEmpleadoUseCase
    private lateinit var repository: EmpleadoRepository

    @Before
    fun setup() {
        repository = mockk()
        useCase = GetEmpleadoUseCase(repository)
    }

    @Test
    fun invoke_llamaAlRepositorioYRetornaElEmpleadoCorrespondienteAlId() = runTest {

        val empleadoId = 1
        val empleadoEsperado = Empleado(
            empleadoId = empleadoId,
            nombres = "Maria Santos",
            sueldo = 65000.0,
            fechaIngreso = LocalDate.now(),
            sexo = "Femenino"
        )

        coEvery { repository.getEmpleado(empleadoId) } returns empleadoEsperado

        val result = useCase(empleadoId)

        assertEquals(empleadoEsperado, result)
        coVerify(exactly = 1) { repository.getEmpleado(empleadoId) }
    }

    @Test
    fun invoke_retornaNullSiElIdNoExisteEnElRepositorio() = runTest {

        val idInexistente = 99
        coEvery { repository.getEmpleado(idInexistente) } returns null

        val result = useCase(idInexistente)

        assertNull(result)
        coVerify(exactly = 1) { repository.getEmpleado(idInexistente) }
    }
}