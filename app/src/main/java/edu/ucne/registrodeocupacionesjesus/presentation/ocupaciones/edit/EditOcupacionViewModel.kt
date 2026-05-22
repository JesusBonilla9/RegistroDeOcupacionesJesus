package edu.ucne.registrodeocupacionesjesus.presentation.ocupaciones.edit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.registrodeocupacionesjesus.domain.ocupaciones.model.Ocupacion
import edu.ucne.registrodeocupacionesjesus.domain.ocupaciones.usecase.DeleteOcupacionUseCase
import edu.ucne.registrodeocupacionesjesus.domain.ocupaciones.usecase.GetOcupacionUseCase
import edu.ucne.registrodeocupacionesjesus.domain.ocupaciones.usecase.ObserveOcupacionesUseCase
import edu.ucne.registrodeocupacionesjesus.domain.ocupaciones.usecase.UpsertOcupacionUseCase
import edu.ucne.registrodeocupacionesjesus.domain.ocupaciones.usecase.validateDescription
import edu.ucne.registrodeocupacionesjesus.domain.ocupaciones.usecase.validateSueldo
import edu.ucne.registrodeocupacionesjesus.presentation.navigation.Screen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditOcupacionViewModel @Inject constructor(
    private val getOcupacionUseCase: GetOcupacionUseCase,
    private val upsertOcupacionUseCase: UpsertOcupacionUseCase,
    private val deleteOcupacionUseCase: DeleteOcupacionUseCase,
    private val observeOcupacionesUseCase: ObserveOcupacionesUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val routeArgs = savedStateHandle.toRoute<Screen.OcupacionEdit>()
    private val ocupacionId: Int = routeArgs.ocupacionId

    private val _state = MutableStateFlow(EditOcupacionUiState())
    val state: StateFlow<EditOcupacionUiState> = _state.asStateFlow()

    init {
        loadOcupacion(ocupacionId)
    }

    fun onEvent(event: EditOcupacionUiEvent) {
        when (event) {
            is EditOcupacionUiEvent.Load -> loadOcupacion(event.id)
            is EditOcupacionUiEvent.DescriptionChanged -> _state.update {
                it.copy(descripcion = event.value, descripcionError = null)
            }
            is EditOcupacionUiEvent.SueldoChanged -> _state.update {
                it.copy(sueldo = event.value, sueldoError = null)
            }
            EditOcupacionUiEvent.Save -> onSave()
            EditOcupacionUiEvent.Delete -> onDelete()
        }
    }

    private fun loadOcupacion(id: Int?) {
        if (id == null || id == 0) {
            _state.update { it.copy(isNew = true, ocupacionId = null) }
            return
        }

        viewModelScope.launch {
            val ocupacion = getOcupacionUseCase(id)
            if (ocupacion != null) {
                _state.update {
                    it.copy(
                        isNew = false,
                        ocupacionId = ocupacion.ocupacionId,
                        descripcion = ocupacion.descripcion,
                        sueldo = ocupacion.sueldo.toString()
                    )
                }
            } else {
                _state.update { it.copy(isNew = true, ocupacionId = null) }
            }
        }
    }

    private fun onSave() {
        viewModelScope.launch {
            val descripcion = state.value.descripcion
            val sueldo = state.value.sueldo

            val descripcionesExistentes = observeOcupacionesUseCase().first()
                .filter { it.ocupacionId != state.value.ocupacionId }
                .map { it.descripcion }

            val descripcionValidation = validateDescription(descripcion, descripcionesExistentes)
            val sueldoValidation = validateSueldo(sueldo)

            if (!descripcionValidation.isValid || !sueldoValidation.isValid) {
                _state.update {
                    it.copy(
                        descripcionError = descripcionValidation.error,
                        sueldoError = sueldoValidation.error
                    )
                }
            } else {
                _state.update { it.copy(isSaving = true) }

                val ocupacion = Ocupacion(
                    ocupacionId = state.value.ocupacionId ?: 0,
                    descripcion = descripcion,
                    sueldo = sueldo.toDoubleOrNull() ?: 0.0
                )

                val result = upsertOcupacionUseCase(ocupacion)
                result.onSuccess { newId ->
                    _state.update {
                        it.copy(
                            isSaving = false,
                            saved = true,
                            ocupacionId = newId,
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
        val id = state.value.ocupacionId ?: return
        viewModelScope.launch {
            _state.update { it.copy(isDeleting = true) }
            deleteOcupacionUseCase(id)
            _state.update { it.copy(isDeleting = false, deleted = true) }
        }
    }
}