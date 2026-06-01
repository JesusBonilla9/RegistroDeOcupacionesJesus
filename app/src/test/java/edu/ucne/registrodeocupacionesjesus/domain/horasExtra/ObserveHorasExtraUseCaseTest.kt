package edu.ucne.registrodeocupacionesjesus.domain.horasExtra

import edu.ucne.registrodeocupacionesjesus.data.horasExtra.local.TipoHoraExtra
import edu.ucne.registrodeocupacionesjesus.domain.horasExtra.model.HoraExtra
import edu.ucne.registrodeocupacionesjesus.domain.horasExtra.repository.HoraExtraRepository
import edu.ucne.registrodeocupacionesjesus.domain.horasExtra.usecase.ObserveHorasExtraUseCase
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
class ObserveHorasExtraUseCaseTest {

    private lateinit var useCase: ObserveHorasExtraUseCase
    private lateinit var repository: HoraExtraRepository

    @Before
    fun setup() {
        repository = mockk()
        useCase = ObserveHorasExtraUseCase(repository)
    }

    @Test
    fun invoke_llamaAlRepositorioYRetornaUnFlujoConLaListaDeHorasExtra() = runTest {
        val listaEsperada = listOf(
            HoraExtra(
                horaExtraId = 1,
                empleadoId = 1,
                fecha = LocalDate.of(2024, 5, 10),
                cantidadHoras = 4,
                tipoHoraExtra = TipoHoraExtra.NOCTURNA,
                recargo = 2000.0,
                esPuestoDireccion = false
            ),
            HoraExtra(
                horaExtraId = 2,
                empleadoId = 2,
                fecha = LocalDate.of(2024, 6, 15),
                cantidadHoras = 8,
                tipoHoraExtra = TipoHoraExtra.DIA_LIBRE_FERIADO,
                recargo = 4000.0,
                esPuestoDireccion = false
            )
        )
        coEvery { repository.observeHorasExtra() } returns flowOf(listaEsperada)

        val result = useCase().first()

        assertEquals(listaEsperada, result)
        coVerify(exactly = 1) { repository.observeHorasExtra() }
    }

    @Test
    fun invoke_retornaUnFlujoConUnaListaVaciaCuandoNoHayRegistros() = runTest {
        val listaVacia = emptyList<HoraExtra>()
        coEvery { repository.observeHorasExtra() } returns flowOf(listaVacia)

        val result = useCase().first()

        assertEquals(listaVacia, result)
        coVerify(exactly = 1) { repository.observeHorasExtra() }
    }
}