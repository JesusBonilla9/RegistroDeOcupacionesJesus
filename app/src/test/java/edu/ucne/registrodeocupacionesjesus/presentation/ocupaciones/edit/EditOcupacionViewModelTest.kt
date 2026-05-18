package edu.ucne.registrodeocupacionesjesus.presentation.ocupaciones.edit

import androidx.lifecycle.SavedStateHandle
import edu.ucne.registrodeocupacionesjesus.domain.ocupaciones.model.Ocupacion
import edu.ucne.registrodeocupacionesjesus.domain.ocupaciones.usecase.DeleteOcupacionUseCase
import edu.ucne.registrodeocupacionesjesus.domain.ocupaciones.usecase.GetOcupacionUseCase
import edu.ucne.registrodeocupacionesjesus.domain.ocupaciones.usecase.ObserveOcupacionesUseCase
import edu.ucne.registrodeocupacionesjesus.domain.ocupaciones.usecase.UpsertOcupacionUseCase
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

@OptIn(ExperimentalCoroutinesApi::class)
class EditOcupacionViewModelTest {

    private val dispatcher = StandardTestDispatcher()
    private lateinit var getOcupacion: GetOcupacionUseCase
    private lateinit var upsertOcupacion: UpsertOcupacionUseCase
    private lateinit var deleteOcupacion: DeleteOcupacionUseCase
    private lateinit var observeOcupaciones: ObserveOcupacionesUseCase

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        getOcupacion = mockk()
        upsertOcupacion = mockk()
        deleteOcupacion = mockk()
        observeOcupaciones = mockk()

        every { observeOcupaciones() } returns flowOf(emptyList())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun cargar_conIdNuloOCeroEstableceEstadoNuevo() = runTest(dispatcher) {
        val savedStateHandle = SavedStateHandle(mapOf("ocupacionId" to 0))
        val vm = EditOcupacionViewModel(getOcupacion, upsertOcupacion, deleteOcupacion, observeOcupaciones, savedStateHandle)

        vm.onEvent(EditOcupacionUiEvent.Load(0))
        runCurrent()

        val s = vm.state.value
        assertTrue(s.isNew)
        assertNull(s.ocupacionId)
    }

    @Test
    fun cargar_conIdLlenaLosCampos() = runTest(dispatcher) {
        coEvery { getOcupacion(7) } returns Ocupacion(
            ocupacionId = 7,
            descripcion = "Desarrollador de Software",
            sueldo = 75000.0
        )

        val savedStateHandle = SavedStateHandle(mapOf("ocupacionId" to 7))
        val vm = EditOcupacionViewModel(getOcupacion, upsertOcupacion, deleteOcupacion, observeOcupaciones, savedStateHandle)

        vm.onEvent(EditOcupacionUiEvent.Load(7))
        runCurrent()

        val s = vm.state.value
        assertFalse(s.isNew)
        assertEquals(7, s.ocupacionId)
        assertEquals("Desarrollador de Software", s.descripcion)
        assertEquals("75000.0", s.sueldo)
    }

    @Test
    fun guardar_conEntradasInvalidasEstableceErroresYNoGuarda() = runTest(dispatcher) {
        val savedStateHandle = SavedStateHandle(mapOf("ocupacionId" to 0))
        val vm = EditOcupacionViewModel(getOcupacion, upsertOcupacion, deleteOcupacion, observeOcupaciones, savedStateHandle)

        vm.onEvent(EditOcupacionUiEvent.DescriptionChanged(""))
        vm.onEvent(EditOcupacionUiEvent.SueldoChanged("abc"))

        vm.onEvent(EditOcupacionUiEvent.Save)
        runCurrent()

        val s = vm.state.value
        assertNotNull(s.descripcionError)
        assertNotNull(s.sueldoError)
        assertFalse(s.saved)
    }

    @Test
    fun guardar_conEntradasValidasLlamaUpsertYEstableceGuardado() = runTest(dispatcher) {
        coEvery { upsertOcupacion(any()) } returns Result.success(123)
        val savedStateHandle = SavedStateHandle(mapOf("ocupacionId" to 0))
        val vm = EditOcupacionViewModel(getOcupacion, upsertOcupacion, deleteOcupacion, observeOcupaciones, savedStateHandle)

        vm.onEvent(EditOcupacionUiEvent.DescriptionChanged("Ingeniero de Datos"))
        vm.onEvent(EditOcupacionUiEvent.SueldoChanged("80000.0"))

        vm.onEvent(EditOcupacionUiEvent.Save)
        runCurrent()

        val s = vm.state.value
        assertFalse(s.isSaving)
        assertTrue(s.saved)
        assertEquals(123, s.ocupacionId)
    }

    @Test
    fun eliminar_cuandoTieneIdLlamaCasoDeUsoYMarcaEliminado() = runTest(dispatcher) {
        coEvery { deleteOcupacion(9) } returns Unit
        coEvery { getOcupacion(9) } returns Ocupacion(
            ocupacionId = 9,
            descripcion = "Arquitecto",
            sueldo = 90000.0
        )

        val savedStateHandle = SavedStateHandle(mapOf("ocupacionId" to 9))
        val vm = EditOcupacionViewModel(getOcupacion, upsertOcupacion, deleteOcupacion, observeOcupaciones, savedStateHandle)

        vm.onEvent(EditOcupacionUiEvent.Load(9))
        runCurrent()

        vm.onEvent(EditOcupacionUiEvent.Delete)
        runCurrent()

        coVerify { deleteOcupacion(9) }
        val s = vm.state.value
        assertFalse(s.isDeleting)
        assertTrue(s.deleted)
    }
}
