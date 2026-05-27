package edu.ucne.registrodeocupacionesjesus.presentation.horasExtra.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.registrodeocupacionesjesus.domain.empleados.usecase.ObserveEmpleadosUseCase
import edu.ucne.registrodeocupacionesjesus.domain.horasExtra.usecase.DeleteHoraExtraUseCase
import edu.ucne.registrodeocupacionesjesus.domain.horasExtra.usecase.ObserveHorasExtraUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HoraExtraListViewModel @Inject constructor(
    private val observeHorasExtraUseCase: ObserveHorasExtraUseCase,
    private val observeEmpleadosUseCase: ObserveEmpleadosUseCase,
    private val deleteHoraExtraUseCase: DeleteHoraExtraUseCase
): ViewModel() {
    private val _state = MutableStateFlow(HoraExtraListUiState(isLoading = true))
    val state: StateFlow<HoraExtraListUiState> = _state.asStateFlow()

    init {
        loadEmpleados()
        loadHorasExtra()
    }

    fun onEvent(event: HoraExtraListUiEvent){
        when(event){
            HoraExtraListUiEvent.Load -> {
                loadEmpleados()
                loadHorasExtra()
            }

            HoraExtraListUiEvent.Refresh ->{
                loadEmpleados()
                loadHorasExtra()
            }
            is HoraExtraListUiEvent.Delete -> onDelete(event.id)
            is HoraExtraListUiEvent.ShowMessage -> _state.update { it.copy(message = event.message) }
            HoraExtraListUiEvent.ClearMessage -> _state.update { it.copy(message = null) }
            HoraExtraListUiEvent.CreateNew -> _state.update { it.copy(navigateToCreate = true) }
            is HoraExtraListUiEvent.Edit -> _state.update { it.copy(navigateToEditId = event.id) }
        }
    }
    fun loadEmpleados(){
        viewModelScope.launch {
            observeEmpleadosUseCase().collectLatest { empleados ->
                _state.update { it.copy(empleados = empleados) }
            }
        }
    }
    fun loadHorasExtra(){
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            observeHorasExtraUseCase().collectLatest { horasExtra ->
                _state.update { it.copy(isLoading = false, horasExtra = horasExtra, message = null) }
            }
        }
    }
    private fun onDelete(id: Int) {
        viewModelScope.launch {
            deleteHoraExtraUseCase(id)
            onEvent(HoraExtraListUiEvent.ShowMessage("Eliminado"))
        }
    }
}