package edu.ucne.registrodeocupacionesjesus.presentation.horasExtra.edit

import androidx.lifecycle.SavedStateHandle
import edu.ucne.registrodeocupacionesjesus.data.empleados.local.FrecuenciaPago
import edu.ucne.registrodeocupacionesjesus.data.horasExtra.local.TipoHoraExtra
import edu.ucne.registrodeocupacionesjesus.domain.empleados.model.Empleado
import edu.ucne.registrodeocupacionesjesus.domain.empleados.usecase.ObserveEmpleadosUseCase
import edu.ucne.registrodeocupacionesjesus.domain.horasExtra.model.HoraExtra
import edu.ucne.registrodeocupacionesjesus.domain.horasExtra.usecase.DeleteHoraExtraUseCase
import edu.ucne.registrodeocupacionesjesus.domain.horasExtra.usecase.GetHoraExtraUseCase
import edu.ucne.registrodeocupacionesjesus.domain.horasExtra.usecase.UpsertHoraExtraUseCase
import edu.ucne.registrodeocupacionesjesus.domain.ocupaciones.model.Ocupacion
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
import org.robolectric.RobolectricTestRunner
import org.junit.runner.RunWith
import java.time.LocalDate

@RunWith(RobolectricTestRunner::class)
@OptIn(ExperimentalCoroutinesApi::class)
class EditHoraExtraViewModelTest {

    private val dispatcher = StandardTestDispatcher()
    private lateinit var getHoraExtra: GetHoraExtraUseCase
    private lateinit var upsertHoraExtra: UpsertHoraExtraUseCase
    private lateinit var deleteHoraExtra: DeleteHoraExtraUseCase
    private lateinit var observeEmpleados: ObserveEmpleadosUseCase
    private lateinit var observeOcupaciones: ObserveOcupacionesUseCase

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        getHoraExtra = mockk()
        upsertHoraExtra = mockk()
        deleteHoraExtra = mockk()
        observeEmpleados = mockk()
        observeOcupaciones = mockk()

        val mockEmpleado = Empleado(1, 1, LocalDate.now(), "Juan", "Masculino", 10000.0, FrecuenciaPago.SEMANAL)
        val mockOcupacion = Ocupacion(1, "Desarrollador", 10000.0, false)

        every { observeEmpleados() } returns flowOf(listOf(mockEmpleado))
        every { observeOcupaciones() } returns flowOf(listOf(mockOcupacion))
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun cargar_conIdNuloOCeroEstableceEstadoNuevo() = runTest(dispatcher) {
        val savedStateHandle = SavedStateHandle(mapOf("horaExtraId" to 0))
        val viewModel = EditHoraExtraViewModel(
            getHoraExtra, upsertHoraExtra, deleteHoraExtra, observeEmpleados, observeOcupaciones, savedStateHandle
        )

        viewModel.onEvent(EditHoraExtraUiEvent.Load(0))
        runCurrent()

        val currentState = viewModel.state.value
        assertTrue(currentState.isNew)
        assertNull(currentState.horaExtraId)
    }

    @Test
    fun cargar_conIdLlenaLosCampos() = runTest(dispatcher) {
        val fecha = LocalDate.of(2023, 5, 10)
        coEvery { getHoraExtra(7) } returns HoraExtra(
            horaExtraId = 7,
            empleadoId = 1,
            fecha = fecha,
            cantidadHoras = 10,
            tipoHoraExtra = TipoHoraExtra.NOCTURNA,
            recargo = 1500.0,
            esPuestoDireccion = false
        )

        val savedStateHandle = SavedStateHandle(mapOf("horaExtraId" to 7))
        val viewModel = EditHoraExtraViewModel(
            getHoraExtra, upsertHoraExtra, deleteHoraExtra, observeEmpleados, observeOcupaciones, savedStateHandle
        )

        viewModel.onEvent(EditHoraExtraUiEvent.Load(7))
        runCurrent()

        val currentState = viewModel.state.value
        assertFalse(currentState.isNew)
        assertEquals(7, currentState.horaExtraId)
        assertEquals(1, currentState.empleadoId)
        assertEquals(fecha, currentState.fecha)
        assertEquals("10", currentState.cantidadHoras)
        assertEquals(TipoHoraExtra.NOCTURNA, currentState.tipoHoraExtra)
    }

    @Test
    fun guardar_conEntradasInvalidasEstableceErroresYNoGuarda() = runTest(dispatcher) {
        val savedStateHandle = SavedStateHandle(mapOf("horaExtraId" to 0))
        val viewModel = EditHoraExtraViewModel(
            getHoraExtra, upsertHoraExtra, deleteHoraExtra, observeEmpleados, observeOcupaciones, savedStateHandle
        )

        viewModel.onEvent(EditHoraExtraUiEvent.EmpleadoChanged(0))
        viewModel.onEvent(EditHoraExtraUiEvent.CantidadHorasChanged("-5"))

        viewModel.onEvent(EditHoraExtraUiEvent.Save)
        runCurrent()

        val currentState = viewModel.state.value
        assertNotNull(currentState.empleadoError)
        assertNotNull(currentState.cantidadHorasError)
        assertFalse(currentState.saved)
    }

    @Test
    fun guardar_conEntradasValidasLlamaUpsertYEstableceGuardado() = runTest(dispatcher) {
        coEvery { upsertHoraExtra(any()) } returns Result.success(123)
        val savedStateHandle = SavedStateHandle(mapOf("horaExtraId" to 0))
        val viewModel = EditHoraExtraViewModel(
            getHoraExtra, upsertHoraExtra, deleteHoraExtra, observeEmpleados, observeOcupaciones, savedStateHandle
        )

        viewModel.onEvent(EditHoraExtraUiEvent.EmpleadoChanged(1))
        viewModel.onEvent(EditHoraExtraUiEvent.CantidadHorasChanged("5"))
        viewModel.onEvent(EditHoraExtraUiEvent.TipoHoraExtraChanged(TipoHoraExtra.DIURNA))
        viewModel.onEvent(EditHoraExtraUiEvent.FechaChanged(LocalDate.now()))

        viewModel.onEvent(EditHoraExtraUiEvent.Save)
        runCurrent()

        val currentState = viewModel.state.value
        assertFalse(currentState.isSaving)
        assertTrue(currentState.saved)
        assertEquals(123, currentState.horaExtraId) // El Id se actualiza al éxito si se configuró en el Result
    }

    @Test
    fun eliminar_cuandoTieneIdLlamaCasoDeUsoYMarcaEliminado() = runTest(dispatcher) {
        coEvery { deleteHoraExtra(9) } returns Unit
        coEvery { getHoraExtra(9) } returns HoraExtra(
            horaExtraId = 9,
            empleadoId = 1,
            fecha = LocalDate.now(),
            cantidadHoras = 2,
            tipoHoraExtra = TipoHoraExtra.DIURNA
        )

        val savedStateHandle = SavedStateHandle(mapOf("horaExtraId" to 9))
        val viewModel = EditHoraExtraViewModel(
            getHoraExtra, upsertHoraExtra, deleteHoraExtra, observeEmpleados, observeOcupaciones, savedStateHandle
        )

        viewModel.onEvent(EditHoraExtraUiEvent.Load(9))
        runCurrent()

        viewModel.onEvent(EditHoraExtraUiEvent.Delete)
        runCurrent()

        coVerify { deleteHoraExtra(9) }
        val currentState = viewModel.state.value
        assertFalse(currentState.isDeleting)
        assertTrue(currentState.deleted)
    }
}