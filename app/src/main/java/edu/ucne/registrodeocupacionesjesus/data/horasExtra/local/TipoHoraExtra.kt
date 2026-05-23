package edu.ucne.registrodeocupacionesjesus.data.horasExtra.local

enum class TipoHoraExtra(
    val descripcion : String,
    val porcentajeRecargo : Double
) {
    DIURNA("DIURNA", 1.35),
    NOCTURNA("NOCTURNA", 1.5),
    DIA_LIBRE_FERIADO("DIA LIBRE O FERIADO", 2.0),
    ALTO_VOLUMEN("ALTO VOLUMEN", 2.0)
}