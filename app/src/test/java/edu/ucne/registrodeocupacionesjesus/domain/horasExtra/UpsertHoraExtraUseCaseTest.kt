package edu.ucne.registrodeocupacionesjesus.domain.horasExtra

import edu.ucne.registrodeocupacionesjesus.data.horasExtra.local.TipoHoraExtra
import edu.ucne.registrodeocupacionesjesus.domain.horasExtra.model.HoraExtra
import edu.ucne.registrodeocupacionesjesus.domain.horasExtra.repository.HoraExtraRepository
import edu.ucne.registrodeocupacionesjesus.domain.horasExtra.usecase.UpsertHoraExtraUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.time.LocalDate

@ExperimentalCoroutinesApi
class UpsertHoraExtraUseCaseTest {

    private lateinit var useCase: UpsertHoraExtraUseCase
    private lateinit var repository: HoraExtraRepository

    @Before
    fun setup() {
        repository = mockk()
        useCase = UpsertHoraExtraUseCase(repository)
    }

    @Test
    fun invoke_guardaHoraExtraConDatosValidos() = runTest {
        val horaExtra = HoraExtra(
            horaExtraId = 0,
            empleadoId = 1,
            fecha = LocalDate.now(),
            cantidadHoras = 5,
            tipoHoraExtra = TipoHoraExtra.DIURNA,
            recargo = 1500.0,
            esPuestoDireccion = false
        )
        coEvery { repository.upsert(horaExtra) } returns 1

        val result = useCase(horaExtra)

        assertTrue(result.isSuccess)
        assertEquals(1, result.getOrNull())
        coVerify(exactly = 1) { repository.upsert(horaExtra) }
    }

    @Test
    fun invoke_fallaConEmpleadoInvalido() = runTest {
        val horaExtra = HoraExtra(
            horaExtraId = 0,
            empleadoId = 0,
            fecha = LocalDate.now(),
            cantidadHoras = 5,
            tipoHoraExtra = TipoHoraExtra.DIURNA
        )
        val result = useCase(horaExtra)

        assertTrue(result.isFailure)
        assertEquals("Debe seleccionar un empleadoId", result.exceptionOrNull()?.message)
        coVerify(exactly = 0) { repository.upsert(any()) }
    }

    @Test
    fun invoke_fallaConFechaFutura() = runTest {
        val horaExtra = HoraExtra(
            horaExtraId = 0,
            empleadoId = 1,
            fecha = LocalDate.now().plusDays(1),
            cantidadHoras = 5,
            tipoHoraExtra = TipoHoraExtra.DIURNA
        )
        val result = useCase(horaExtra)

        assertTrue(result.isFailure)
        assertEquals("La fecha no puede ser futura", result.exceptionOrNull()?.message)
        coVerify(exactly = 0) { repository.upsert(any()) }
    }

    @Test
    fun invoke_fallaConCantidadDeHorasVaciaOInvalida() = runTest {
        val horaExtra = HoraExtra(
            horaExtraId = 0,
            empleadoId = 1,
            fecha = LocalDate.now(),
            cantidadHoras = 0,
            tipoHoraExtra = TipoHoraExtra.DIURNA
        )
        val result = useCase(horaExtra)

        assertTrue(result.isFailure)
        assertEquals("La cantidad de horas debe ser mayor que 0", result.exceptionOrNull()?.message)
        coVerify(exactly = 0) { repository.upsert(any()) }
    }

    @Test
    fun invoke_fallaConMasDe80Horas() = runTest {
        val horaExtra = HoraExtra(
            horaExtraId = 0,
            empleadoId = 1,
            fecha = LocalDate.now(),
            cantidadHoras = 85,
            tipoHoraExtra = TipoHoraExtra.ALTO_VOLUMEN
        )
        val result = useCase(horaExtra)

        assertTrue(result.isFailure)
        assertEquals("No puedes registrar mas de 80 horas en una semana", result.exceptionOrNull()?.message)
        coVerify(exactly = 0) { repository.upsert(any()) }
    }

    @Test
    fun invoke_fallaSiMasDe24HorasYNoEsAltoVolumen() = runTest {
        val horaExtra = HoraExtra(
            horaExtraId = 0,
            empleadoId = 1,
            fecha = LocalDate.now(),
            cantidadHoras = 25,
            tipoHoraExtra = TipoHoraExtra.DIURNA
        )
        val result = useCase(horaExtra)

        assertTrue(result.isFailure)
        assertEquals("Para más de 24 horas, el tipo debe ser 'ALTO VOLUMEN'", result.exceptionOrNull()?.message)
        coVerify(exactly = 0) { repository.upsert(any()) }
    }
}