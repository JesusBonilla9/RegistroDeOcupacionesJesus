package edu.ucne.registrodeocupacionesjesus.data.empleados.local

enum class FrecuenciaPago(
    val descripcion: String,
    val divisor : Double
) {
    SEMANAL("SEMANAL", 5.5),
    QUINCENAL(descripcion = "QUINCENAL",11.91),
    MENSUAL(descripcion = "MENSUAL",23.83)
}