package com.example.vita_app.ui.screen.addmeal

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.vita_app.data.remote.model.MealType
import com.example.vita_app.ui.components.AppBackground
import com.example.vita_app.ui.components.MacroPreview
import com.example.vita_app.ui.screen.meals.MealsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddMeal(viewModel: MealsViewModel, mealId: Int, onMealAdd: () -> Unit, onBack: () -> Unit) {

    val meal = viewModel.meals.find {it.id == mealId}
    if (meal == null){
        Text("Meal not found")
        return
    }

    var grams by remember{mutableStateOf("")}
    var section by remember {mutableStateOf(MealType.BREAKFAST)}
    var submit by remember {mutableStateOf(false)}

    //Pantalla

    AppBackground {
        //Top Bar transparente arriba
        Column(modifier = Modifier.fillMaxSize()) {
            CenterAlignedTopAppBar(
                title = { Text("Agregar meal") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Navegar hacia atras"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )

            Column(
                modifier = Modifier.fillMaxWidth().padding(16.dp).verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Comida elegida (solo lectura — es una referencia al catálogo)
                Text(meal.name, style = MaterialTheme.typography.titleMedium)
                Text("${meal.calories} cal / 100g", color = Color.Gray)

                // Gramos consumidos
                OutlinedTextField(
                    value = grams,
                    onValueChange = { grams = it },
                    label = { Text("Grams") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )

                // Sección
                Text("Section")
                FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    MealType.entries.forEach { type ->
                        FilterChip(
                            selected = section == type,
                            onClick = { section = type },
                            label = { Text(type.name) }
                        )
                    }
                }

                MacroPreview(meal = meal, grams = grams)

                // Add: crea la entrada en el diario y vuelve atrás
                Button(
                    enabled = !submit && grams.isNotBlank(),
                    onClick = {
                        submit = true
                        viewModel.addEntry(meal.id, grams, section)
                        onMealAdd()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Add")
                }
            }
        }
    }
}
