package edu.ucne.registrodeocupacionesjesus.presentation.empleados.list

import edu.ucne.registrodeocupacionesjesus.domain.empleados.model.Empleado
import edu.ucne.registrodeocupacionesjesus.domain.empleados.usecase.DeleteEmpleadoUseCase
import edu.ucne.registrodeocupacionesjesus.domain.empleados.usecase.ObserveEmpleadosUseCase
import edu.ucne.registrodeocupacionesjesus.domain.ocupaciones.usecase.ObserveOcupacionesUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class EmpleadoListViewModelTest {

    private val dispatcher = StandardTestDispatcher()
    private lateinit var observeEmpleados: ObserveEmpleadosUseCase
    private lateinit var deleteEmpleado: DeleteEmpleadoUseCase
    private lateinit var observeOcupaciones: ObserveOcupacionesUseCase

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        observeEmpleados = mockk()
        deleteEmpleado = mockk()
        observeOcupaciones = mockk()

        every { observeOcupaciones() } returns flowOf(emptyList())
    }

    @Test
    fun eliminar_llamaAlCasoDeUsoYMuestraMensaje() = runTest(dispatcher) {
        val shared = MutableSharedFlow<List<Empleado>>(replay = 1)
        shared.emit(emptyList())
        every { observeEmpleados() } returns shared
        coEvery { deleteEmpleado(5) } returns Unit

        val viewModel = EmpleadoListViewModel(observeEmpleados, observeOcupaciones, deleteEmpleado)
        runCurrent()

        viewModel.onEvent(EmpleadoListUiEvent.Delete(5))
        runCurrent()

        coVerify { deleteEmpleado(5) }
        assertEquals("Eliminado", viewModel.state.value.message)
    }

    @Test
    fun banderasDeNavegacion_cambianComoSeEspera() = runTest(dispatcher) {
        val shared = MutableSharedFlow<List<Empleado>>(replay = 1)
        shared.emit(emptyList())
        every { observeEmpleados() } returns shared
        val viewModel = EmpleadoListViewModel(observeEmpleados, observeOcupaciones, deleteEmpleado)
        runCurrent()

        viewModel.onEvent(EmpleadoListUiEvent.CreateNew)
        assertTrue(viewModel.state.value.navigateToCreate)

        viewModel.onEvent(EmpleadoListUiEvent.Edit(10))
        assertEquals(10, viewModel.state.value.navigateToEditId)
    }
}