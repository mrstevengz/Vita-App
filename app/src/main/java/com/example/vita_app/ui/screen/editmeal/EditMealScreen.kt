package com.example.vita_app.ui.screen.editmeal

// Proposito: Pantalla para modificar o eliminar una entrada de comida ya registrada.

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
import com.example.vita_app.ui.components.MacroPreview
import com.example.vita_app.ui.screen.meals.MealsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
// Permite modificar o borrar una comida que ya estaba registrada en el diario.
fun EditMealScreen(
    vm: MealsViewModel,
    entryId: Int,
    onCompleted: () -> Unit,
    onBack: () -> Unit
) {

    //Busca el meal especifico en la lista de meals del VM, basado en la id que se le pasa en diaryscreen
    val entry = vm.entries.find { it.id == entryId }

    if(entry == null) {
        Text("Meal not found")
        return
    }

    //Inicializacion de variables ya rellenadas (mutableStateOf)
    var grams by remember {mutableStateOf(entry.grams)}
    var section by remember {mutableStateOf(entry.section)}

    //Variable sencilla para almacenar y mandar a llamar el update, se le tienen que pasar todos los datos
    // Accion de guardado: valida los campos y delega la actualizacion al ViewModel.
    val saveMeal = {
        //Llamada a la funcion, le pasa el id y la variable con el meal
        vm.updateEntry(entryId, grams, section)
        //Llama a la funcion onCompleted que se le pasa
        onCompleted()
    }

    AppBackground {
        //Top Bar transparente arriba
        Column(modifier = Modifier.fillMaxSize()) {
            CenterAlignedTopAppBar(
                title = {Text("Editar entrada")},
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Navegar hacia atras")
                    }
                },
                actions = {
                    IconButton(onClick = saveMeal) {
                        Icon(imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Confirmar")
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
                Text("Food: ${entry.meal.name}", style = MaterialTheme.typography.titleMedium)

                OutlinedTextField(
                    value = grams,
                    onValueChange = { grams = it },
                    label = { Text("Grams") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )

                MacroPreview(meal = entry.meal, grams = grams)

                Text("Section", style = MaterialTheme.typography.labelLarge)
                FlowRow(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    MealType.entries.forEach { type ->
                        FilterChip(
                            selected = section == type,
                            onClick = { section = type },
                            label = { Text(type.name) }
                        )
                    }
                }
            }
        }
    }
    }

