package edu.ucne.registrodeocupacionesjesus.presentation.horasExtra.edit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.registrodeocupacionesjesus.data.empleados.local.FrecuenciaPago
import edu.ucne.registrodeocupacionesjesus.domain.empleados.usecase.ObserveEmpleadosUseCase
import edu.ucne.registrodeocupacionesjesus.domain.horasExtra.model.HoraExtra
import edu.ucne.registrodeocupacionesjesus.domain.horasExtra.usecase.DeleteHoraExtraUseCase
import edu.ucne.registrodeocupacionesjesus.domain.horasExtra.usecase.GetHoraExtraUseCase
import edu.ucne.registrodeocupacionesjesus.domain.horasExtra.usecase.UpsertHoraExtraUseCase
import edu.ucne.registrodeocupacionesjesus.domain.horasExtra.usecase.calcularMontoHoraExtra
import edu.ucne.registrodeocupacionesjesus.domain.horasExtra.usecase.validateCantidadHoras
import edu.ucne.registrodeocupacionesjesus.domain.horasExtra.usecase.validateEmpleadoId
import edu.ucne.registrodeocupacionesjesus.domain.horasExtra.usecase.validateFechaHoraExtra
import edu.ucne.registrodeocupacionesjesus.domain.horasExtra.usecase.validateTipoHoraExtra
import edu.ucne.registrodeocupacionesjesus.domain.ocupaciones.usecase.ObserveOcupacionesUseCase
import edu.ucne.registrodeocupacionesjesus.presentation.navigation.Screen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class EditHoraExtraViewModel @Inject constructor(
    private val getHoraExtraUseCase: GetHoraExtraUseCase,
    private val upsertHoraExtraUseCase: UpsertHoraExtraUseCase,
    private val deleteHoraExtraUseCase: DeleteHoraExtraUseCase,
    private val observeEmpleadosUseCase: ObserveEmpleadosUseCase,
    private val observeOcupacionesUseCase: ObserveOcupacionesUseCase,
    savedStateHandle: SavedStateHandle
): ViewModel() {
    private val routeArgs = savedStateHandle.toRoute<Screen.HoraExtraEdit>()
    private val horaExtraId: Int = routeArgs.horaExtraId

    private val _state = MutableStateFlow(EditHoraExtraUiState())
    val state : StateFlow<EditHoraExtraUiState> = _state.asStateFlow()

    init {
        loadEmpleados()
        loadOcupaciones()
        loadHoraExtra(horaExtraId)
    }
    private fun loadEmpleados(){
        viewModelScope.launch {
            observeEmpleadosUseCase().collect { listaEmpleados ->
                _state.update { it.copy(empleados = listaEmpleados) }
                recalcularTotal()
            }
        }
    }
    private fun loadOcupaciones(){
        viewModelScope.launch {
            observeOcupacionesUseCase().collect { listaOcupaciones ->
                _state.update { it.copy(ocupaciones = listaOcupaciones) }
                recalcularTotal()
            }
        }
    }
    fun onEvent(event: EditHoraExtraUiEvent){
        when(event){
            is EditHoraExtraUiEvent.Load -> loadHoraExtra(event.id)
            is EditHoraExtraUiEvent.EmpleadoChanged -> {
                _state.update { it.copy(empleadoId = event.id, empleadoError = null) }
                recalcularTotal()
            }
            is EditHoraExtraUiEvent.CantidadHorasChanged -> {
                _state.update { it.copy(cantidadHoras = event.cantidad, cantidadHorasError = null, tipoHoraExtraError = null ) }
                recalcularTotal()
            }
            is EditHoraExtraUiEvent.TipoHoraExtraChanged -> {
                _state.update { it.copy(tipoHoraExtra = event.tipo, tipoHoraExtraError = null, cantidadHorasError = null) }
                recalcularTotal()
            }
            is EditHoraExtraUiEvent.FechaChanged ->{
                _state.update { it.copy(fecha = event.date, fechaError = null) }
            }

            EditHoraExtraUiEvent.Save -> onSave()
            EditHoraExtraUiEvent.Delete -> onDelete()
        }
    }
    private fun recalcularTotal(){
        val currentState = _state.value
        val empleado = currentState.empleados.find { it.empleadoId == currentState.empleadoId }
        val ocupacion = currentState.ocupaciones.find { it.ocupacionId == empleado?.ocupacionId }

        val sueldo = empleado?.sueldo ?: 0.0
        val frecuencia = empleado?.frecuenciaPago ?: FrecuenciaPago.SEMANAL
        val esDireccion = ocupacion?.esPuestoDireccion ?: false
        val horas = currentState.cantidadHoras.toIntOrNull() ?: 0

        val nuevoRecargo = calcularMontoHoraExtra(
            sueldo = sueldo,
            frecuenciaPago = frecuencia,
            tipoHoraExtra = currentState.tipoHoraExtra,
            cantidadHoras = horas,
            esPuestoDireccion = esDireccion
        )
        _state.update { it.copy(recargo = nuevoRecargo, esPuestoDireccion = esDireccion) }
    }
    private fun loadHoraExtra(id: Int?){
        if(id == null || id == 0){
            _state.update { it.copy(isNew = true, horaExtraId = null) }
            return
        }
        viewModelScope.launch {
            val horaExtra = getHoraExtraUseCase(id)
            if(horaExtra != null){
                _state.update {
                    it.copy(
                        isNew = false,
                        horaExtraId = horaExtra.horaExtraId,
                        empleadoId = horaExtra.empleadoId,
                        fecha = horaExtra.fecha,
                        cantidadHoras = horaExtra.cantidadHoras.toString(),
                        tipoHoraExtra = horaExtra.tipoHoraExtra,
                        recargo = horaExtra.recargo,
                        esPuestoDireccion = horaExtra.esPuestoDireccion
                    )
                }
                recalcularTotal()
            }
        }
    }
    private fun onSave(){
        viewModelScope.launch {
            val empleadoResult = validateEmpleadoId(state.value.empleadoId)
            val fechaResult = validateFechaHoraExtra(state.value.fecha)
            val horasResult = validateCantidadHoras(state.value.cantidadHoras)
            val tipoResult = validateTipoHoraExtra(
                tipo = state.value.tipoHoraExtra.name,
                cantidad = state.value.cantidadHoras
            )

            if(!empleadoResult.isValid || !fechaResult.isValid || !horasResult.isValid || !tipoResult.isValid){
                _state.update {
                    it.copy(
                        empleadoError = empleadoResult.error,
                        fechaError = fechaResult.error,
                        cantidadHorasError = horasResult.error,
                        tipoHoraExtraError = tipoResult.error
                    )
                }
                return@launch
            }
            _state.update { it.copy(isSaving = true) }

            val horaExtra = HoraExtra(
                horaExtraId = state.value.horaExtraId ?: 0,
                empleadoId = state.value.empleadoId,
                fecha = state.value.fecha,
                cantidadHoras = state.value.cantidadHoras.toInt(),
                tipoHoraExtra = state.value.tipoHoraExtra,
                recargo = state.value.recargo,
                esPuestoDireccion = state.value.esPuestoDireccion
            )
            upsertHoraExtraUseCase(horaExtra).onSuccess {
                _state.update { it.copy(isSaving = false, saved = true) }
            }.onFailure {
                _state.update { it.copy(isSaving = false) }
            }
        }
    }
    private fun onDelete(){
        val id = state.value.horaExtraId ?: return
        viewModelScope.launch {
            _state.update { it.copy(isDeleting = true) }
            deleteHoraExtraUseCase(id)
            _state.update { it.copy(isDeleting = false, deleted = true) }
        }
    }
}