package edu.ucne.registrodeocupacionesjesus.data.ocupaciones.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import edu.ucne.registrodeocupacionesjesus.data.ocupaciones.local.OcupacionDao
import edu.ucne.registrodeocupacionesjesus.data.ocupaciones.local.OcupacionEntity
import edu.ucne.registrodeocupacionesjesus.domain.ocupaciones.model.Ocupacion
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import junit.framework.TestCase.assertNotNull

@ExperimentalCoroutinesApi
class OcupacionRepositoryImplTest{
    @get: Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var repository: OcupacionRepositoryImpl
    private lateinit var dao: OcupacionDao

    @Before
    fun setup(){
        dao = mockk(relaxed = true)
        repository = OcupacionRepositoryImpl(dao)
    }

    @Test
    fun upsert_guardaLaOcupacionCorrectamente() = runTest {
        val ocupacion = Ocupacion(
            ocupacionId = 0,
            descripcion = "Ingeniero en Sistemas",
            sueldo = 50000.0
        )
        val ocupacionSlot = slot<OcupacionEntity>()
        coEvery { dao.upsert(capture(ocupacionSlot)) } just Runs

        val result = repository.upsert(ocupacion)

        assertEquals(0, result)
        coVerify { dao.upsert(any()) }
        assertEquals(ocupacion.descripcion, ocupacionSlot.captured.descripcion)
        assertEquals(ocupacion.sueldo, ocupacionSlot.captured.sueldo)
    }

    @Test
    fun upsert_actualizaLaOcupacionCorrectamente() = runTest {
        val ocupacion = Ocupacion(ocupacionId = 1, descripcion = "Ocupacion actualizada", sueldo = 30000.0)
        coEvery { dao.upsert(any()) } just Runs

        val result = repository.upsert(ocupacion)

        assertEquals(1, result)
        coVerify { dao.upsert(any()) }
    }
    @Test
    fun delete_eliminaLaOcupacionCorrectamente() = runTest{
        val ocupacionId = 1
        coEvery { dao.deleteById(ocupacionId) } just Runs

        repository.delete(ocupacionId)
        coVerify { dao.deleteById(ocupacionId) }
    }
    @Test
    fun observeOcupaciones_retornaFlowDeOcupaciones() = runTest{
        val entities = listOf(
            OcupacionEntity(1, "Doctor", 82000.0),
            OcupacionEntity(2, "Arquitecto", 70000.0)
        )
        every { dao.observeAll() } returns flowOf(entities)
        val result = repository.observeOcupaciones().first()

        assertEquals(2,result.size)
        assertEquals("Doctor", result[0].descripcion)
        assertEquals("Arquitecto", result[1].descripcion)
    }
    @Test
    fun getOcupacion_retornaOcupacionPorId() = runTest {
        val entity = OcupacionEntity(1, "Enfermera", 10000.0)
        coEvery { dao.getById(1) } returns entity

        val result = repository.getOcupacion(1)

        assertNotNull(result)
        assertEquals("Enfermera", result?.descripcion)
        assertEquals(10000.0, result?.sueldo)
    }
}