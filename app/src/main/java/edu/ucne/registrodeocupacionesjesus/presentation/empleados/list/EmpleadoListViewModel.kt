package edu.ucne.registrodeocupacionesjesus.presentation.empleados.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.registrodeocupacionesjesus.domain.empleados.usecase.DeleteEmpleadoUseCase
import edu.ucne.registrodeocupacionesjesus.domain.empleados.usecase.ObserveEmpleadosUseCase
import edu.ucne.registrodeocupacionesjesus.domain.ocupaciones.usecase.ObserveOcupacionesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EmpleadoListViewModel @Inject constructor(
    private val observeEmpleadosUseCase: ObserveEmpleadosUseCase,
    private val observeOcupacionesUseCase: ObserveOcupacionesUseCase,
    private val deleteEmpleadoUseCase: DeleteEmpleadoUseCase
): ViewModel() {
    private val _state = MutableStateFlow(EmpleadoListUiState(isLoading = true))
    val state: StateFlow<EmpleadoListUiState> = _state.asStateFlow()

    init {
        loadOcupaciones()
        loadEmpleados()
    }

    fun onEvent(event: EmpleadoListUiEvent) {
        when(event) {
            EmpleadoListUiEvent.Load -> {
                loadOcupaciones()
                loadEmpleados()
            }
            EmpleadoListUiEvent.Refresh -> {
                loadOcupaciones()
                loadEmpleados()
            }
            is EmpleadoListUiEvent.Delete -> onDelete(event.id)
            is EmpleadoListUiEvent.ShowMessage -> _state.update { it.copy(message = event.message) }
            EmpleadoListUiEvent.ClearMessage -> _state.update { it.copy(message = null) }
            EmpleadoListUiEvent.CreateNew -> _state.update { it.copy(navigateToCreate = true) }
            is EmpleadoListUiEvent.Edit -> _state.update { it.copy(navigateToEditId = event.id) }
        }
    }

    fun loadOcupaciones() {
        viewModelScope.launch {
            observeOcupacionesUseCase().collectLatest { ocupaciones ->
                _state.update { it.copy(ocupaciones = ocupaciones) }
            }
        }
    }

    fun loadEmpleados() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            observeEmpleadosUseCase().collectLatest { empleados ->
                _state.update { it.copy(isLoading = false, empleados = empleados, message = null) }
            }
        }
    }

    private fun onDelete(id: Int) {
        viewModelScope.launch {
            deleteEmpleadoUseCase(id)
            onEvent(EmpleadoListUiEvent.ShowMessage("Eliminado"))
        }
    }
}