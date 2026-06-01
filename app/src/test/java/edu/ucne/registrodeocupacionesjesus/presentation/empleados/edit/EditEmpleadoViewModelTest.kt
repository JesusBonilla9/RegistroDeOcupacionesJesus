package edu.ucne.registrodeocupacionesjesus.presentation.empleados.edit

import androidx.lifecycle.SavedStateHandle
import edu.ucne.registrodeocupacionesjesus.data.empleados.local.FrecuenciaPago
import edu.ucne.registrodeocupacionesjesus.domain.empleados.model.Empleado
import edu.ucne.registrodeocupacionesjesus.domain.empleados.usecase.DeleteEmpleadoUseCase
import edu.ucne.registrodeocupacionesjesus.domain.empleados.usecase.GetEmpleadoUseCase
import edu.ucne.registrodeocupacionesjesus.domain.empleados.usecase.UpsertEmpleadoUseCase
import edu.ucne.registrodeocupacionesjesus.domain.ocupaciones.usecase.ObserveOcupacionesUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
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
    private lateinit var observeOcupaciones: ObserveOcupacionesUseCase

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        getEmpleado = mockk()
        upsertEmpleado = mockk()
        deleteEmpleado = mockk()
        observeOcupaciones = mockk()

        every { observeOcupaciones() } returns flowOf(emptyList())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun cargar_conIdNuloOCeroEstableceEstadoNuevo() = runTest(dispatcher) {
        val savedStateHandle = SavedStateHandle(mapOf("empleadoId" to 0))
        val viewModel = EditEmpleadoViewModel(getEmpleado, upsertEmpleado, deleteEmpleado, observeOcupaciones, savedStateHandle)

        viewModel.onEvent(EditEmpleadoUiEvent.Load(0))
        runCurrent()

        val currentState = viewModel.state.value
        assertTrue(currentState.isNew)
        assertNull(currentState.empleadoId)
    }

    @Test
    fun cargar_conIdLlenaLosCampos() = runTest(dispatcher) {
        val fecha = LocalDate.of(2023, 5, 10)
        coEvery { getEmpleado(7) } returns Empleado(
            empleadoId = 7,
            ocupacionId = 2,
            nombres = "Juan Perez",
            sexo = "Masculino",
            fechaIngreso = fecha,
            sueldo = 50000.0,
            frecuenciaPago = FrecuenciaPago.MENSUAL
        )

        val savedStateHandle = SavedStateHandle(mapOf("empleadoId" to 7))
        val viewModel = EditEmpleadoViewModel(getEmpleado, upsertEmpleado, deleteEmpleado, observeOcupaciones, savedStateHandle)

        viewModel.onEvent(EditEmpleadoUiEvent.Load(7))
        runCurrent()

        val currentState = viewModel.state.value
        assertFalse(currentState.isNew)
        assertEquals(7, currentState.empleadoId)
        assertEquals(2, currentState.ocupacionId)
        assertEquals("Juan Perez", currentState.nombres)
        assertEquals("Masculino", currentState.sexo)
        assertEquals(fecha, currentState.fechaIngreso)
        assertEquals("50000.0", currentState.sueldo)
        assertEquals(FrecuenciaPago.MENSUAL, currentState.frecuenciaPago)
    }

    @Test
    fun guardar_conEntradasInvalidasEstableceErroresYNoGuarda() = runTest(dispatcher) {
        val savedStateHandle = SavedStateHandle(mapOf("empleadoId" to 0))
        val viewModel = EditEmpleadoViewModel(getEmpleado, upsertEmpleado, deleteEmpleado, observeOcupaciones, savedStateHandle)

        viewModel.onEvent(EditEmpleadoUiEvent.NombresChanged(""))
        viewModel.onEvent(EditEmpleadoUiEvent.SueldoChanged("abc"))
        viewModel.onEvent(EditEmpleadoUiEvent.SexoChanged(""))
        viewModel.onEvent(EditEmpleadoUiEvent.OcupacionChanged(0))

        viewModel.onEvent(EditEmpleadoUiEvent.Save)
        runCurrent()

        val currentState = viewModel.state.value
        assertNotNull(currentState.nombresError)
        assertNotNull(currentState.sueldoError)
        assertNotNull(currentState.sexoError)
        assertNotNull(currentState.ocupacionError)
        assertFalse(currentState.saved)
    }

    @Test
    fun guardar_conEntradasValidasLlamaUpsertYEstableceGuardado() = runTest(dispatcher) {
        coEvery { upsertEmpleado(any()) } returns Result.success(123)
        val savedStateHandle = SavedStateHandle(mapOf("empleadoId" to 0))
        val viewModel = EditEmpleadoViewModel(getEmpleado, upsertEmpleado, deleteEmpleado, observeOcupaciones, savedStateHandle)

        viewModel.onEvent(EditEmpleadoUiEvent.NombresChanged("Maria Lopez"))
        viewModel.onEvent(EditEmpleadoUiEvent.SueldoChanged("60000.0"))
        viewModel.onEvent(EditEmpleadoUiEvent.SexoChanged("Femenino"))
        viewModel.onEvent(EditEmpleadoUiEvent.FechaIngresoChanged(LocalDate.now()))
        viewModel.onEvent(EditEmpleadoUiEvent.OcupacionChanged(1))
        viewModel.onEvent(EditEmpleadoUiEvent.FrecuenciaPagoChanged(FrecuenciaPago.SEMANAL))

        viewModel.onEvent(EditEmpleadoUiEvent.Save)
        runCurrent()

        val currentState = viewModel.state.value
        assertFalse(currentState.isSaving)
        assertTrue(currentState.saved)
        assertEquals(123, currentState.empleadoId)
    }

    @Test
    fun eliminar_cuandoTieneIdLlamaCasoDeUsoYMarcaEliminado() = runTest(dispatcher) {
        coEvery { deleteEmpleado(9) } returns Unit
        coEvery { getEmpleado(9) } returns Empleado(
            empleadoId = 9,
            ocupacionId = 1,
            nombres = "Usuario",
            sexo = "Masculino",
            fechaIngreso = LocalDate.now(),
            sueldo = 100.0
        )

        val savedStateHandle = SavedStateHandle(mapOf("empleadoId" to 9))
        val viewModel = EditEmpleadoViewModel(getEmpleado, upsertEmpleado, deleteEmpleado, observeOcupaciones, savedStateHandle)

        viewModel.onEvent(EditEmpleadoUiEvent.Load(9))
        runCurrent()

        viewModel.onEvent(EditEmpleadoUiEvent.Delete)
        runCurrent()

        coVerify { deleteEmpleado(9) }
        val currentState = viewModel.state.value
        assertFalse(currentState.isDeleting)
        assertTrue(currentState.deleted)
    }
}