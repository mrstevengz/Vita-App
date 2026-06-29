package com.example.vita_app.ui.components

// Proposito: Componente que calcula una vista previa de macros segun la cantidad de gramos ingresada.

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.vita_app.data.remote.model.MealResponse
import kotlin.text.toInt

@Composable
// Calcula y muestra macros proporcionales a los gramos ingresados.
fun MacroPreview(
    meal: MealResponse,
    grams: String,
    modifier: Modifier = Modifier
) {
    // Convierte gramos a proporcion sobre 100g, porque la API guarda macros por 100g.
    val factor = (grams.toDoubleOrNull() ?: 0.0)/ 100.0
    val calsToAdd = (meal.calories.toDoubleOrNull() ?: 0.0) * factor
    val proteinToAdd = (meal.protein.toDoubleOrNull() ?: 0.0) * factor
    val carbsToAdd   = (meal.carbs.toDoubleOrNull()    ?: 0.0) * factor
    val fatToAdd     = (meal.fat.toDoubleOrNull()      ?: 0.0) * factor

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text("Se agregará:", fontWeight = FontWeight.Bold)
            Text("Calorías: ${calsToAdd.toInt()} cal")
            Text("Proteína: ${proteinToAdd.toInt()} g")
            Text("Carbohidratos: ${carbsToAdd.toInt()} g")
            Text("Grasa: ${fatToAdd.toInt()} g")
        }
    }
}