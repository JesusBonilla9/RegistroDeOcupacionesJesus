package edu.ucne.registrodeocupacionesjesus.domain.ocupaciones
import edu.ucne.registrodeocupacionesjesus.domain.ocupaciones.model.Ocupacion
import edu.ucne.registrodeocupacionesjesus.domain.ocupaciones.repository.OcupacionRepository
import edu.ucne.registrodeocupacionesjesus.domain.ocupaciones.usecase.GetOcupacionUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class GetOcupacionUseCaseTest {

    private lateinit var useCase: GetOcupacionUseCase
    private lateinit var repository: OcupacionRepository

    @Before
    fun setup() {
        repository = mockk()
        useCase = GetOcupacionUseCase(repository)
    }

    @Test
    fun invoke_llamaAlRepositorioYRetornaLaOcupacionCorrespondienteAlId() = runTest {
        val ocupacionId = 1
        val ocupacionEsperada = Ocupacion(ocupacionId = ocupacionId, descripcion = "Médico", sueldo = 80000.0)

        coEvery { repository.getOcupacion(ocupacionId) } returns ocupacionEsperada

        val result = useCase(ocupacionId)

        assertEquals(ocupacionEsperada, result)
        coVerify(exactly = 1) { repository.getOcupacion(ocupacionId) }
    }

    @Test
    fun invoke_retornaNullSiElIdNoExisteEnElRepositorio() = runTest {
        val idInexistente = 99

        coEvery { repository.getOcupacion(idInexistente) } returns null

        val result = useCase(idInexistente)

        assertNull(result)
        coVerify(exactly = 1) { repository.getOcupacion(idInexistente) }
    }
}