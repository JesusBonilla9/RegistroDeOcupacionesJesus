package edu.ucne.registrodeocupacionesjesus.presentation.horasExtra.list

import edu.ucne.registrodeocupacionesjesus.domain.empleados.usecase.ObserveEmpleadosUseCase
import edu.ucne.registrodeocupacionesjesus.domain.horasExtra.model.HoraExtra
import edu.ucne.registrodeocupacionesjesus.domain.horasExtra.usecase.DeleteHoraExtraUseCase
import edu.ucne.registrodeocupacionesjesus.domain.horasExtra.usecase.ObserveHorasExtraUseCase
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
class HoraExtraListViewModelTest {

    private val dispatcher = StandardTestDispatcher()
    private lateinit var observeHorasExtra: ObserveHorasExtraUseCase
    private lateinit var observeEmpleados: ObserveEmpleadosUseCase
    private lateinit var deleteHoraExtra: DeleteHoraExtraUseCase

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        observeHorasExtra = mockk()
        observeEmpleados = mockk()
        deleteHoraExtra = mockk()

        every { observeEmpleados() } returns flowOf(emptyList())
    }

    @Test
    fun eliminar_llamaAlCasoDeUsoYMuestraMensaje() = runTest(dispatcher) {
        val shared = MutableSharedFlow<List<HoraExtra>>(replay = 1)
        shared.emit(emptyList())
        every { observeHorasExtra() } returns shared
        coEvery { deleteHoraExtra(5) } returns Unit

        val viewModel = HoraExtraListViewModel(observeHorasExtra, observeEmpleados, deleteHoraExtra)
        runCurrent()

        viewModel.onEvent(HoraExtraListUiEvent.Delete(5))
        runCurrent()

        coVerify { deleteHoraExtra(5) }
        assertEquals("Eliminado", viewModel.state.value.message)
    }

    @Test
    fun banderasDeNavegacion_cambianComoSeEspera() = runTest(dispatcher) {
        val shared = MutableSharedFlow<List<HoraExtra>>(replay = 1)
        shared.emit(emptyList())
        every { observeHorasExtra() } returns shared

        val viewModel = HoraExtraListViewModel(observeHorasExtra, observeEmpleados, deleteHoraExtra)
        runCurrent()

        viewModel.onEvent(HoraExtraListUiEvent.CreateNew)
        assertTrue(viewModel.state.value.navigateToCreate)

        viewModel.onEvent(HoraExtraListUiEvent.Edit(10))
        assertEquals(10, viewModel.state.value.navigateToEditId)
    }
}