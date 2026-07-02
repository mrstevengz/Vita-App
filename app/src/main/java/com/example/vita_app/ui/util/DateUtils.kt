package com.example.vita_app.ui.util

import java.time.LocalDate //Fecha sin hora
import java.time.ZoneId //Zona horaria
import java.time.Instant //Instante exacto de tiempo (en UTC)


//Booleano que compara si la fecha del backend cae en el dia date (segun la hora del telefono)
fun isOnDate(isoDate: String, date: LocalDate): Boolean {
    val entryLocalDate = Instant.parse(isoDate) //Texto de Instant en UTC
        .atZone(ZoneId.systemDefault())                // reexpresado en la zona del teléfono
        .toLocalDate()                                 // deja solo la fecha local
    return entryLocalDate == date //Coincide el dia, retorna booleano
}