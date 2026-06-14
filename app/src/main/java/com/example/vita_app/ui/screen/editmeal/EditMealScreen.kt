package com.example.vita_app.ui.screen.editmeal

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.VerticalDivider
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
import com.example.vita_app.data.remote.model.MealType
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

    //Busca el meal especifico en la lista de meals del VM, basado en la id que se le pasa en diaryscreen
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

    //Variable sencilla para almacenar y mandar a llamar el update, se le tienen que pasar todos los datos
    val saveMeal = {
        val updated = meal.copy(
            name = name,
            calories = calories.toDoubleOrNull() ?: meal.calories,
            fat = fat.toDoubleOrNull() ?: meal.fat,
            protein = protein.toDoubleOrNull() ?: meal.protein,
            directions = directions,
            section = section,
        )
        //Llamada a la funcion, le pasa el id y la variable con el meal
        vm.updateMeal(mealId, updated)
        //Llama a la funcion onCompleted que se le pasa
        onCompleted()
    }

    AppBackground {
        //Top Bar transparente arriba
        Column(modifier = Modifier.fillMaxSize()) {
            CenterAlignedTopAppBar(
                title = {Text("Editar meal")},
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Navegar hacia atras")
                    }
                },
                actions = {
                    IconButton(onClick = saveMeal) {
                        Icon(imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Confirmar meal")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )

            Column(
                modifier = Modifier.fillMaxWidth().verticalScroll(rememberScrollState()).padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedTextField(                                   // 3
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedTextField(
                        value = calories, onValueChange = { calories = it },
                        label = { Text("Cal") }, singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = fat, onValueChange = { fat = it },
                        label = { Text("Fat") }, singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = protein, onValueChange = { protein = it },
                        label = { Text("Protein") }, singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f)
                    )
                }

                Text("Section", style = MaterialTheme.typography.labelLarge)
                //Flowrow es un row que wrappea en caso de se salga de la pantalla
                FlowRow(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    MealType.entries.forEach { type ->
                        //Filterchip
                        FilterChip(
                            selected = section == type,
                            onClick = {section = type},
                            label = {Text(type.name)}
                        )
                    }
                }
            }
        }
    }
    }