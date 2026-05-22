package edu.ucne.registrodeocupacionesjesus.presentation.empleados.edit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.registrodeocupacionesjesus.domain.empleados.model.Empleado
import edu.ucne.registrodeocupacionesjesus.domain.empleados.usecase.DeleteEmpleadoUseCase
import edu.ucne.registrodeocupacionesjesus.domain.empleados.usecase.GetEmpleadoUseCase
import edu.ucne.registrodeocupacionesjesus.domain.empleados.usecase.UpsertEmpleadoUseCase
import edu.ucne.registrodeocupacionesjesus.domain.empleados.usecase.validateFecha
import edu.ucne.registrodeocupacionesjesus.domain.empleados.usecase.validateNombres
import edu.ucne.registrodeocupacionesjesus.domain.empleados.usecase.validateSexo
import edu.ucne.registrodeocupacionesjesus.domain.empleados.usecase.validateSueldo
import edu.ucne.registrodeocupacionesjesus.presentation.navigation.Screen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditEmpleadoViewModel @Inject constructor(
    private val getEmpleadoUseCase: GetEmpleadoUseCase,
    private val upsertEmpleadoUseCase: UpsertEmpleadoUseCase,
    private val deleteEmpleadoUseCase: DeleteEmpleadoUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val routeArgs = savedStateHandle.toRoute<Screen.EmpleadoEdit>()
    private val empleadoId: Int = routeArgs.empleadoId

    private val _state = MutableStateFlow(EditEmpleadoUiState())
    val state: StateFlow<EditEmpleadoUiState> = _state.asStateFlow()

    init {
        loadEmpleado(empleadoId)
    }

    fun onEvent(event: EditEmpleadoUiEvent) {
        when (event) {
            is EditEmpleadoUiEvent.Load -> loadEmpleado(event.id)
            is EditEmpleadoUiEvent.NombresChanged -> _state.update {
                it.copy(nombres = event.value, nombresError = null)
            }
            is EditEmpleadoUiEvent.SexoChanged -> _state.update {
                it.copy(sexo = event.value, sexoError = null)
            }
            is EditEmpleadoUiEvent.FechaIngresoChanged -> _state.update {
                it.copy(fechaIngreso = event.value, fechaIngresoError = null)
            }
            is EditEmpleadoUiEvent.SueldoChanged -> _state.update {
                it.copy(sueldo = event.value, sueldoError = null)
            }
            EditEmpleadoUiEvent.Save -> onSave()
            EditEmpleadoUiEvent.Delete -> onDelete()
        }
    }

    private fun loadEmpleado(id: Int?) {
        if (id == null || id == 0) {
            _state.update { it.copy(isNew = true, empleadoId = null) }
            return
        }

        viewModelScope.launch {
            val empleado = getEmpleadoUseCase(id)
            if (empleado != null) {
                _state.update {
                    it.copy(
                        isNew = false,
                        empleadoId = empleado.empleadoId,
                        nombres = empleado.nombres,
                        sexo = empleado.sexo,
                        fechaIngreso = empleado.fechaIngreso,
                        sueldo = empleado.sueldo.toString()
                    )
                }
            } else {
                _state.update { it.copy(isNew = true, empleadoId = null) }
            }
        }
    }

    private fun onSave() {
        viewModelScope.launch {
            val nombres = state.value.nombres
            val sueldo = state.value.sueldo
            val fechaIngreso = state.value.fechaIngreso
            val sexo = state.value.sexo

            val nombresValidation = validateNombres(nombres)
            val sueldoValidation = validateSueldo(sueldo)
            val fechaValidation = validateFecha(fechaIngreso)
            val sexoValidation = validateSexo(sexo)

            if (!nombresValidation.isValid || !sueldoValidation.isValid || !fechaValidation.isValid || !sexoValidation.isValid) {
                _state.update {
                    it.copy(
                        nombresError = nombresValidation.error,
                        sueldoError = sueldoValidation.error,
                        fechaIngresoError = fechaValidation.error,
                        sexoError = sexoValidation.error
                    )
                }
            } else {
                _state.update { it.copy(isSaving = true) }

                val empleado = Empleado(
                    empleadoId = state.value.empleadoId ?: 0,
                    nombres = nombres,
                    sexo = sexo,
                    fechaIngreso = fechaIngreso,
                    sueldo = sueldo.toDoubleOrNull() ?: 0.0
                )

                val result = upsertEmpleadoUseCase(empleado)
                result.onSuccess { newId ->
                    _state.update {
                        it.copy(
                            isSaving = false,
                            saved = true,
                            empleadoId = newId,
                            isNew = false
                        )
                    }
                }.onFailure {
                    _state.update { it.copy(isSaving = false) }
                }
            }
        }
    }

    private fun onDelete() {
        val id = state.value.empleadoId ?: return
        viewModelScope.launch {
            _state.update { it.copy(isDeleting = true) }
            deleteEmpleadoUseCase(id)
            _state.update { it.copy(isDeleting = false, deleted = true) }
        }
    }
}
