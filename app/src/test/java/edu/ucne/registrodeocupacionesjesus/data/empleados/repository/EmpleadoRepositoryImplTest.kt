package edu.ucne.registrodeocupacionesjesus.data.empleados.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import edu.ucne.registrodeocupacionesjesus.data.empleados.local.EmpleadoDao
import edu.ucne.registrodeocupacionesjesus.data.empleados.local.EmpleadoEntity
import edu.ucne.registrodeocupacionesjesus.domain.empleados.model.Empleado
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
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.time.LocalDate

@ExperimentalCoroutinesApi
class EmpleadoRepositoryImplTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var repository: EmpleadoRepositoryImpl
    private lateinit var dao: EmpleadoDao

    @Before
    fun setup() {
        dao = mockk(relaxed = true)
        repository = EmpleadoRepositoryImpl( dao)
    }

    @Test
    fun upsert_guardaElEmpleadoCorrectamente() = runTest {
        val fechaActual = LocalDate.now()
        val empleado = Empleado(
            empleadoId = 0,
            fechaIngreso = fechaActual,
            nombres = "Juan Perez",
            sexo = "Masculino",
            sueldo = 50000.0
        )
        val empleadoSlot = slot<EmpleadoEntity>()
        coEvery { dao.upsert(capture(empleadoSlot)) } just Runs

        val result = repository.upsert(empleado)

        assertEquals(0, result)
        coVerify { dao.upsert(any()) }
        assertEquals(empleado.nombres, empleadoSlot.captured.nombres)
        assertEquals(empleado.sueldo, empleadoSlot.captured.sueldo, 0.0)
        assertEquals(empleado.sexo, empleadoSlot.captured.sexo)
        assertEquals(empleado.fechaIngreso, empleadoSlot.captured.fechaIngreso)
    }

    @Test
    fun upsert_actualizaElEmpleadoCorrectamente() = runTest {
        val empleado = Empleado(
            empleadoId = 1,
            fechaIngreso = LocalDate.now(),
            nombres = "Empleado Actualizado",
            sexo = "Femenino",
            sueldo = 30000.0
        )
        coEvery { dao.upsert(any()) } just Runs

        val result = repository.upsert(empleado)

        assertEquals(1, result)
        coVerify { dao.upsert(any()) }
    }

    @Test
    fun delete_eliminaElEmpleadoCorrectamente() = runTest {
        val empleadoId = 1
        coEvery { dao.deleteById(empleadoId) } just Runs

        repository.delete(empleadoId)

        coVerify { dao.deleteById(empleadoId) }
    }

    @Test
    fun observeEmpleados_retornaFlowDeEmpleados() = runTest {
        val entities = listOf(
            EmpleadoEntity(1, LocalDate.now(), "Maria Lopez", "Femenino", 82000.0),
            EmpleadoEntity(2, LocalDate.now(), "Carlos Ruiz", "Masculino", 70000.0)
        )
        every { dao.observeAll() } returns flowOf(entities)

        val result = repository.observeEmpleados().first()

        assertEquals(2, result.size)
        assertEquals("Maria Lopez", result[0].nombres)
        assertEquals("Carlos Ruiz", result[1].nombres)
    }

    @Test
    fun getEmpleado_retornaEmpleadoPorId() = runTest {
        val entity = EmpleadoEntity(1, LocalDate.now(), "Ana Gomez", "Femenino", 45000.0)
        coEvery { dao.getById(1) } returns entity

        val result = repository.getEmpleado(1)

        assertNotNull(result)
        assertEquals("Ana Gomez", result?.nombres)
        assertEquals(45000.0, result?.sueldo)
        assertEquals("Femenino", result?.sexo)
    }
}

