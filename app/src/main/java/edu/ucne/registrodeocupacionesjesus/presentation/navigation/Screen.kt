package edu.ucne.registrodeocupacionesjesus.presentation.navigation

import kotlinx.serialization.Serializable

sealed class Screen {
    @Serializable
    data object OcupacionList : Screen()

    @Serializable
    data class OcupacionEdit(val ocupacionId : Int = 0) : Screen()

    @Serializable
    data object EmpleadoList : Screen()

    @Serializable
    data class EmpleadoEdit(val ocupacionId : Int = 0) : Screen()
}


