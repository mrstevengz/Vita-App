package com.example.vita_app.ui.screen.addmeal

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.vita_app.data.remote.model.MealType
import com.example.vita_app.ui.components.AppBackground
import com.example.vita_app.ui.screen.meals.MealsViewModel

@Composable
fun AddMeal(viewModel: MealsViewModel, onMealAdd: () -> Unit) {

    //Variables definidas
    var name by remember { mutableStateOf("") }
    var calories by remember { mutableStateOf("") }
    var section by remember {
        mutableStateOf(MealType.BREAKFAST) //Se llama al enum y se le da una seccion default. Luego se cambiara dependiendo de donde se cliquee
    }
    var submit by remember {mutableStateOf(false)}

    //Pantalla

    AppBackground {
        Column(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp))
        {
            TextField(
                value = name,
                onValueChange = {name = it},
                label = {Text("Name")},
                modifier = Modifier.fillMaxWidth()
            )

            TextField(
                value = calories,
                onValueChange = {calories = it},
                label = {Text("Calories")},
                //Hace que el teclado solo muestre numeros
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            Text("Section")

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                MealType.entries.forEach {
                    type ->
                    FilterChip(
                        selected = section == type,
                        onClick = {section = type},
                        label = {Text(type.name)}
                    )
                }
            }

            Button(
                enabled = !submit && name.isNotBlank(),
                onClick ={
                    submit = true
                    val cal = calories.toDoubleOrNull() ?: 0.0
                    if (name.isNotBlank()) {
                        viewModel.addMeal(name, cal, section)
                        onMealAdd()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Add")
            }

        }
    }
}
