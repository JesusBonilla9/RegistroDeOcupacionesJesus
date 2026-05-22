package edu.ucne.registrodeocupacionesjesus.domain.empleados

import edu.ucne.registrodeocupacionesjesus.domain.empleados.model.Empleado
import edu.ucne.registrodeocupacionesjesus.domain.empleados.repository.EmpleadoRepository
import edu.ucne.registrodeocupacionesjesus.domain.empleados.usecase.ObserveEmpleadosUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.time.LocalDate

@ExperimentalCoroutinesApi
class ObserveEmpleadosUseCaseTest {

    private lateinit var useCase: ObserveEmpleadosUseCase
    private lateinit var repository: EmpleadoRepository

    @Before
    fun setup() {
        repository = mockk()
        useCase = ObserveEmpleadosUseCase(repository)
    }

    @Test
    fun invoke_llamaAlRepositorioYRetornaUnFlujoConLaListaDeEmpleados() = runTest {
        val listaEsperada = listOf(
            Empleado(
                empleadoId = 1,
                nombres = "Juan Perez",
                sexo = "Masculino",
                fechaIngreso = LocalDate.of(2023, 5, 10),
                sueldo = 50000.0
            ),
            Empleado(
                empleadoId = 2,
                nombres = "Maria Lopez",
                sexo = "Femenino",
                fechaIngreso = LocalDate.of(2024, 1, 15),
                sueldo = 80000.0
            )
        )
        coEvery { repository.observeEmpleados() } returns flowOf(listaEsperada)

        val result = useCase().first()

        assertEquals(listaEsperada, result)
        coVerify(exactly = 1) { repository.observeEmpleados() }
    }

    @Test
    fun invoke_retornaUnFlujoConUnaListaVaciaCuandoNoHayRegistros() = runTest {
        val listaVacia = emptyList<Empleado>()
        coEvery { repository.observeEmpleados() } returns flowOf(listaVacia)

        val result = useCase().first()

        assertEquals(listaVacia, result)
        coVerify(exactly = 1) { repository.observeEmpleados() }
    }
}