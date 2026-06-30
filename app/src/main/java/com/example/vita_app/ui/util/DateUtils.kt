package com.example.vita_app.ui.util

import java.time.LocalDate
import java.time.ZoneId
import java.time.Instant

fun isOnDate(isoDate: String, date: LocalDate): Boolean {
    val entryLocalDate = Instant.parse(isoDate)
        .atZone(ZoneId.systemDefault())                // reexpresado en la zona del teléfono
        .toLocalDate()                                 // nos quedamos solo con la fecha
    return entryLocalDate == date
}