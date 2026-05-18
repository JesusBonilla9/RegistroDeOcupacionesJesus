package edu.ucne.registrodeocupacionesjesus.domain.ocupaciones
import edu.ucne.registrodeocupacionesjesus.domain.ocupaciones.model.Ocupacion
import edu.ucne.registrodeocupacionesjesus.domain.ocupaciones.repository.OcupacionRepository
import edu.ucne.registrodeocupacionesjesus.domain.ocupaciones.usecase.ObserveOcupacionesUseCase
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

@ExperimentalCoroutinesApi
class ObserveOcupacionesUseCaseTest {

    private lateinit var useCase: ObserveOcupacionesUseCase
    private lateinit var repository: OcupacionRepository

    @Before
    fun setup() {
        repository = mockk()
        useCase = ObserveOcupacionesUseCase(repository)
    }

    @Test
    fun invoke_llamaAlRepositorioYRetornaUnFlujoConLaListaDeOcupaciones() = runTest {
        val listaEsperada = listOf(
            Ocupacion(ocupacionId = 1, descripcion = "Desarrollador", sueldo = 50000.0),
            Ocupacion(ocupacionId = 2, descripcion = "Medico", sueldo = 80000.0)
        )
        coEvery { repository.observeOcupaciones() } returns flowOf(listaEsperada)

        val result = useCase().first()

        assertEquals(listaEsperada, result)
        coVerify(exactly = 1) { repository.observeOcupaciones() }
    }

    @Test
    fun invoke_retornaUnFlujoConUnaListaVaciaCuandoNoHayRegistros() = runTest {
        val listaVacia = emptyList<Ocupacion>()
        coEvery { repository.observeOcupaciones() } returns flowOf(listaVacia)

        val result = useCase().first()

        assertEquals(listaVacia, result)
        coVerify(exactly = 1) { repository.observeOcupaciones() }
    }
}