package edu.ucne.registrodeocupacionesjesus.data.horasExtra.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import edu.ucne.registrodeocupacionesjesus.data.horasExtra.local.HoraExtraDao
import edu.ucne.registrodeocupacionesjesus.data.horasExtra.local.HoraExtraEntity
import edu.ucne.registrodeocupacionesjesus.data.horasExtra.local.TipoHoraExtra
import edu.ucne.registrodeocupacionesjesus.domain.horasExtra.model.HoraExtra
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.time.LocalDate

@ExperimentalCoroutinesApi
class HoraExtraRepositoryImplTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var repository: HoraExtraRepositoryImpl
    private lateinit var dao: HoraExtraDao

    @Before
    fun setup() {
        dao = mockk(relaxed = true)
        repository = HoraExtraRepositoryImpl(dao)
    }

    @Test
    fun upsert_guardaLaHoraExtraCorrectamente() = runTest {
        val horaExtra = HoraExtra(
            horaExtraId = 0,
            empleadoId = 1,
            fecha = LocalDate.now(),
            cantidadHoras = 4,
            tipoHoraExtra = TipoHoraExtra.DIURNA,
            recargo = 1500.0,
            esPuestoDireccion = false
        )
        val horaExtraSlot = slot<HoraExtraEntity>()
        coEvery { dao.upsert(capture(horaExtraSlot)) } returns 1L

        val result = repository.upsert(horaExtra)

        Assert.assertEquals(0, result)
        coVerify { dao.upsert(any()) }
        Assert.assertEquals(horaExtra.empleadoId, horaExtraSlot.captured.empleadoId)
        Assert.assertEquals(horaExtra.cantidadHoras, horaExtraSlot.captured.cantidadHoras)
        Assert.assertEquals(horaExtra.tipoHoraExtra, horaExtraSlot.captured.tipoHoraExtra)
        Assert.assertEquals(horaExtra.recargo, horaExtraSlot.captured.recargo, 0.0)
    }

    @Test
    fun upsert_actualizaLaHoraExtraCorrectamente() = runTest {
        val horaExtra = HoraExtra(
            horaExtraId = 1,
            empleadoId = 2,
            fecha = LocalDate.now(),
            cantidadHoras = 8,
            tipoHoraExtra = TipoHoraExtra.NOCTURNA,
            recargo = 3000.0,
            esPuestoDireccion = false
        )
        coEvery { dao.upsert(any()) } returns 1L

        val result = repository.upsert(horaExtra)

        Assert.assertEquals(1, result)
        coVerify { dao.upsert(any()) }
    }

    @Test
    fun delete_eliminaLaHoraExtraCorrectamente() = runTest {
        val horaExtraId = 1
        coEvery { dao.deleteById(horaExtraId) } just Runs

        repository.delete(horaExtraId)

        coVerify { dao.deleteById(horaExtraId) }
    }

    @Test
    fun observeHorasExtra_retornaFlowDeHorasExtra() = runTest {
        val entities = listOf(
            HoraExtraEntity(1, 1, LocalDate.now(), 2, TipoHoraExtra.DIURNA, 500.0, false),
            HoraExtraEntity(
                2,
                2,
                LocalDate.now(),
                5,
                TipoHoraExtra.DIA_LIBRE_FERIADO,
                2500.0,
                false
            )
        )
        every { dao.observeAll() } returns flowOf(entities)

        val result = repository.observeHorasExtra().first()

        Assert.assertEquals(2, result.size)
        Assert.assertEquals(1, result[0].empleadoId)
        Assert.assertEquals(TipoHoraExtra.DIA_LIBRE_FERIADO, result[1].tipoHoraExtra)
    }

    @Test
    fun getHoraExtra_retornaHoraExtraPorId() = runTest {
        val entity =
            HoraExtraEntity(1, 3, LocalDate.now(), 10, TipoHoraExtra.ALTO_VOLUMEN, 4000.0, false)
        coEvery { dao.getById(1) } returns entity

        val result = repository.getHoraExtra(1)

        Assert.assertNotNull(result)
        Assert.assertEquals(3, result?.empleadoId)
        Assert.assertEquals(10, result?.cantidadHoras)
        Assert.assertEquals(4000.0, result?.recargo)
    }
}