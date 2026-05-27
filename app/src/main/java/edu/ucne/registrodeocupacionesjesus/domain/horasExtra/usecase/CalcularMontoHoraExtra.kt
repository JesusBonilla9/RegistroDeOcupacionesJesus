package edu.ucne.registrodeocupacionesjesus.domain.horasExtra.usecase

import edu.ucne.registrodeocupacionesjesus.data.empleados.local.FrecuenciaPago
import edu.ucne.registrodeocupacionesjesus.data.horasExtra.local.TipoHoraExtra

fun calcularMontoHoraExtra(
    sueldo: Double,
    frecuenciaPago: FrecuenciaPago,
    tipoHoraExtra: TipoHoraExtra,
    cantidadHoras: Int,
    esPuestoDireccion: Boolean
): Double {
    if (esPuestoDireccion) {
        return 0.0
    }

    val salarioDiario = sueldo / frecuenciaPago.divisor
    val valorHoraOrdinaria = salarioDiario / 8.0
    val montoTotal = valorHoraOrdinaria * tipoHoraExtra.factor * cantidadHoras

    return Math.round(montoTotal * 100) / 100.0
}