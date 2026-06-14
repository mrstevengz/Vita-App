package com.example.vita_app.ui.screen.editmeal

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.vita_app.ui.components.AppBackground
import com.example.vita_app.ui.screen.meals.MealsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditMealScreen(
    vm: MealsViewModel,
    mealId: Int,
    onCompleted: () -> Unit,
    onBack: () -> Unit
) {
    val meal = vm.meals.find { it.id == mealId }

    if(meal == null) {
        Text("Meal not found")
        return
    }

    //Inicializacion de variables ya rellenadas (mutableStateOf)
    var name by remember { mutableStateOf(meal.name) }
    var calories by remember { mutableStateOf(meal.calories.toString()) }
    var fat by remember { mutableStateOf(meal.fat.toString()) }
    var protein by remember { mutableStateOf(meal.protein.toString()) }
    var directions by remember { mutableStateOf(meal.directions) }
    var section by remember { mutableStateOf(meal.section) }

    AppBackground {
        //TOP BAR FOR CONFIRM/BACK


        Column(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            CenterAlignedTopAppBar(
                title = {Text("Editar meal")},
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Navegar hacia atras")
                    }
                },
                actions = {
                    IconButton(onClick = onCompleted) {
                        Icon(imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Confirmar meal")
                    }
                }
            )

            TextField(value = name, onValueChange = {name = it}, label = {Text("Name")}, modifier = Modifier.fillMaxWidth())

            TextField(
                value = calories, onValueChange = {calories = it}, label = {Text("Calories")},
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            TextField(
                value = fat, onValueChange = {fat = it}, label = {Text("Fat")},
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            TextField(
                value = protein, onValueChange = {protein = it},
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                label = {Text("Protein")}, modifier = Modifier.fillMaxWidth()
            )

            Button(
                enabled = name.isNotBlank(),
                onClick = {
                    val updated = meal.copy(
                        name = name,
                        calories = calories.toDoubleOrNull() ?: meal.calories,
                        fat = fat.toDoubleOrNull() ?: meal.fat,
                        protein = protein.toDoubleOrNull() ?: meal.protein,
                        directions = directions
                    )
                    vm.updateMeal(mealId, updated)
                    onCompleted()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save")
            }
        }
    }
}