package edu.ucne.registrodeocupacionesjesus.domain.horasExtra

import edu.ucne.registrodeocupacionesjesus.data.horasExtra.local.TipoHoraExtra
import edu.ucne.registrodeocupacionesjesus.domain.horasExtra.model.HoraExtra
import edu.ucne.registrodeocupacionesjesus.domain.horasExtra.repository.HoraExtraRepository
import edu.ucne.registrodeocupacionesjesus.domain.horasExtra.usecase.GetHoraExtraUseCase
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
class GetHoraExtraUseCaseTest {

    private lateinit var useCase: GetHoraExtraUseCase
    private lateinit var repository: HoraExtraRepository

    @Before
    fun setup() {
        repository = mockk()
        useCase = GetHoraExtraUseCase(repository)
    }

    @Test
    fun invoke_llamaAlRepositorioYRetornaLaHoraExtraCorrespondienteAlId() = runTest {
        val horaExtraId = 1
        val horaExtraEsperada = HoraExtra(
            horaExtraId = horaExtraId,
            empleadoId = 2,
            fecha = LocalDate.now(),
            cantidadHoras = 3,
            tipoHoraExtra = TipoHoraExtra.DIURNA,
            recargo = 1200.0,
            esPuestoDireccion = false
        )

        coEvery { repository.getHoraExtra(horaExtraId) } returns horaExtraEsperada

        val result = useCase(horaExtraId)

        assertEquals(horaExtraEsperada, result)
        coVerify(exactly = 1) { repository.getHoraExtra(horaExtraId) }
    }

    @Test
    fun invoke_retornaNullSiElIdNoExisteEnElRepositorio() = runTest {
        val idInexistente = 99
        coEvery { repository.getHoraExtra(idInexistente) } returns null

        val result = useCase(idInexistente)

        assertNull(result)
        coVerify(exactly = 1) { repository.getHoraExtra(idInexistente) }
    }
}