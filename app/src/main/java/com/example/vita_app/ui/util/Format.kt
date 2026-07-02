package com.example.vita_app.ui.util


import java.util.Locale

// 1044 -> "1,044". Forzamos Locale.US para que SIEMPRE use coma como separador
fun Int.formatted(): String = String.format(Locale.US, "%,d", this)