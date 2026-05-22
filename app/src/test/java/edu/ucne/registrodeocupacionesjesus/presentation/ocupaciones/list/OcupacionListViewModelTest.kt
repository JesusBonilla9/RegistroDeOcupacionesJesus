package edu.ucne.registrodeocupacionesjesus.presentation.ocupaciones.list

import edu.ucne.registrodeocupacionesjesus.domain.ocupaciones.model.Ocupacion
import edu.ucne.registrodeocupacionesjesus.domain.ocupaciones.usecase.DeleteOcupacionUseCase
import edu.ucne.registrodeocupacionesjesus.domain.ocupaciones.usecase.ObserveOcupacionesUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class OcupacionListViewModelTest {

    private val dispatcher = StandardTestDispatcher()
    private lateinit var observeOcupaciones: ObserveOcupacionesUseCase
    private lateinit var deleteOcupacion: DeleteOcupacionUseCase

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        observeOcupaciones = mockk()
        deleteOcupacion = mockk()
    }

    @Test
    fun eliminar_llamaAlCasoDeUsoYMuestraMensaje() = runTest(dispatcher) {
        val shared = MutableSharedFlow<List<Ocupacion>>(replay = 1)
        shared.emit(emptyList())
        every { observeOcupaciones() } returns shared
        coEvery { deleteOcupacion(5) } returns Unit

        val vm = OcupacionListViewModel(observeOcupaciones, deleteOcupacion)
        runCurrent()

        vm.onEvent(OcupacionListUiEvent.Delete(5))
        runCurrent()

        coVerify { deleteOcupacion(5) }
        assertEquals("Eliminado", vm.state.value.message)
    }

    @Test
    fun banderasDeNavegacion_cambianComoSeEspera() = runTest(dispatcher) {
        val shared = MutableSharedFlow<List<Ocupacion>>(replay = 1)
        shared.emit(emptyList())
        every { observeOcupaciones() } returns shared
        val vm = OcupacionListViewModel(observeOcupaciones, deleteOcupacion)
        runCurrent()

        vm.onEvent(OcupacionListUiEvent.CreateNew)
        assertTrue(vm.state.value.navigateToCreate)

        vm.onEvent(OcupacionListUiEvent.Edit(10))
        assertEquals(10, vm.state.value.navigateToEditId)
    }
}