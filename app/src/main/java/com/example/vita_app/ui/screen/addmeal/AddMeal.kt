package com.example.vita_app.ui.screen.addmeal

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
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
import com.example.vita_app.ui.components.AppBackground

@Composable
fun AddMeal(section: String) {
    var mealName by remember { mutableStateOf("")}
    var calories by remember {mutableStateOf("")}

    Scaffold { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            AppBackground {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(10.dp)
                ) {
                    TextField(
                        value = mealName,
                        onValueChange = { mealName = it },
                        label = { Text("Name") }
                    )

                    TextField(
                        value = calories,
                        onValueChange = { calories = it },
                        label = { Text("Calories") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )

                    Button(onClick = { handleSubmit(mealName, calories.toDoubleOrNull() ?: 0.0) }) {
                        Text("Add")
                    }
                }
            }
        }
    }
}

fun handleSubmit(mealName: String, calories: Double) {
}