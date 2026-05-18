package edu.ucne.registrodeocupacionesjesus.presentation.empleados.edit

import androidx.lifecycle.SavedStateHandle
import edu.ucne.registrodeocupacionesjesus.domain.empleados.model.Empleado
import edu.ucne.registrodeocupacionesjesus.domain.empleados.usecase.DeleteEmpleadoUseCase
import edu.ucne.registrodeocupacionesjesus.domain.empleados.usecase.GetEmpleadoUseCase
import edu.ucne.registrodeocupacionesjesus.domain.empleados.usecase.UpsertEmpleadoUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.time.LocalDate
import org.robolectric.RobolectricTestRunner
import org.junit.runner.RunWith

@RunWith(RobolectricTestRunner::class)
@OptIn(ExperimentalCoroutinesApi::class)
class EditEmpleadoViewModelTest {

    private val dispatcher = StandardTestDispatcher()
    private lateinit var getEmpleado: GetEmpleadoUseCase
    private lateinit var upsertEmpleado: UpsertEmpleadoUseCase
    private lateinit var deleteEmpleado: DeleteEmpleadoUseCase

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        getEmpleado = mockk()
        upsertEmpleado = mockk()
        deleteEmpleado = mockk()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun cargar_conIdNuloOCeroEstableceEstadoNuevo() = runTest(dispatcher) {

        val savedStateHandle = SavedStateHandle(mapOf("empleadoId" to 0))
        val vm = EditEmpleadoViewModel(getEmpleado, upsertEmpleado, deleteEmpleado, savedStateHandle)

        vm.onEvent(EditEmpleadoUiEvent.Load(0))
        runCurrent()

        val s = vm.state.value
        assertTrue(s.isNew)
        assertNull(s.empleadoId)
    }

    @Test
    fun cargar_conIdLlenaLosCampos() = runTest(dispatcher) {
        val fecha = LocalDate.of(2023, 5, 10)
        coEvery { getEmpleado(7) } returns Empleado(
            empleadoId = 7,
            nombres = "Juan Perez",
            sexo = "Masculino",
            fechaIngreso = fecha,
            sueldo = 50000.0
        )

        val savedStateHandle = SavedStateHandle(mapOf("empleadoId" to 7))
        val vm = EditEmpleadoViewModel(getEmpleado, upsertEmpleado, deleteEmpleado, savedStateHandle)

        vm.onEvent(EditEmpleadoUiEvent.Load(7))
        runCurrent()

        val s = vm.state.value
        assertFalse(s.isNew)
        assertEquals(7, s.empleadoId)
        assertEquals("Juan Perez", s.nombres)
        assertEquals("Masculino", s.sexo)
        assertEquals(fecha, s.fechaIngreso)
        assertEquals("50000.0", s.sueldo)
    }

    @Test
    fun guardar_conEntradasInvalidasEstableceErroresYNoGuarda() = runTest(dispatcher) {
        val savedStateHandle = SavedStateHandle(mapOf("empleadoId" to 0))
        val vm = EditEmpleadoViewModel(getEmpleado, upsertEmpleado, deleteEmpleado, savedStateHandle)

        vm.onEvent(EditEmpleadoUiEvent.NombresChanged(""))
        vm.onEvent(EditEmpleadoUiEvent.SueldoChanged("abc"))
        vm.onEvent(EditEmpleadoUiEvent.SexoChanged(""))

        vm.onEvent(EditEmpleadoUiEvent.Save)
        runCurrent()

        val s = vm.state.value
        assertNotNull(s.nombresError)
        assertNotNull(s.sueldoError)
        assertNotNull(s.sexoError)
        assertFalse(s.saved)
    }

    @Test
    fun guardar_conEntradasValidasLlamaUpsertYEstableceGuardado() = runTest(dispatcher) {
        coEvery { upsertEmpleado(any()) } returns Result.success(123)
        val savedStateHandle = SavedStateHandle(mapOf("empleadoId" to 0))
        val vm = EditEmpleadoViewModel(getEmpleado, upsertEmpleado, deleteEmpleado, savedStateHandle)

        vm.onEvent(EditEmpleadoUiEvent.NombresChanged("Maria Lopez"))
        vm.onEvent(EditEmpleadoUiEvent.SueldoChanged("60000.0"))
        vm.onEvent(EditEmpleadoUiEvent.SexoChanged("Femenino"))
        vm.onEvent(EditEmpleadoUiEvent.FechaIngresoChanged(LocalDate.now()))

        vm.onEvent(EditEmpleadoUiEvent.Save)
        runCurrent()

        val s = vm.state.value
        assertFalse(s.isSaving)
        assertTrue(s.saved)
        assertEquals(123, s.empleadoId)
    }

    @Test
    fun eliminar_cuandoTieneIdLlamaCasoDeUsoYMarcaEliminado() = runTest(dispatcher) {
        coEvery { deleteEmpleado(9) } returns Unit
        coEvery { getEmpleado(9) } returns Empleado(
            empleadoId = 9,
            nombres = "Usuario",
            sexo = "Masculino",
            fechaIngreso = LocalDate.now(),
            sueldo = 100.0
        )

        val savedStateHandle = SavedStateHandle(mapOf("empleadoId" to 9))
        val vm = EditEmpleadoViewModel(getEmpleado, upsertEmpleado, deleteEmpleado, savedStateHandle)

        vm.onEvent(EditEmpleadoUiEvent.Load(9))
        runCurrent()

        vm.onEvent(EditEmpleadoUiEvent.Delete)
        runCurrent()

        coVerify { deleteEmpleado(9) }
        val s = vm.state.value
        assertFalse(s.isDeleting)
        assertTrue(s.deleted)
    }
}