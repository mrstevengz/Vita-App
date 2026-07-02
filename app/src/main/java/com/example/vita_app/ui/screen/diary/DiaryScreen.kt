package com.example.vita_app.ui.screen.diary

import android.annotation.SuppressLint
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.vita_app.data.GoalStore
import com.example.vita_app.data.remote.model.MealType
import com.example.vita_app.ui.components.AppBackground
import com.example.vita_app.ui.components.DateSelectorRow
import com.example.vita_app.ui.components.HomeTopBar
import com.example.vita_app.ui.components.MealSection
import com.example.vita_app.ui.components.WorkoutSection
import com.example.vita_app.ui.screen.meals.MealsViewModel
import com.example.vita_app.ui.screen.workouts.WorkoutViewModel
import com.example.vita_app.ui.theme.CarbonBlack
import java.time.LocalDate


@Composable
fun DiaryScreen(
    viewModel: MealsViewModel, //Se manda a llamar el viewmodel de Meals para obtener los metodos
    workoutsViewModel: WorkoutViewModel,
    onAddMealClick: () -> Unit,
    onMealEditClick: (Int) -> Unit,
    onAddWorkoutClick: () -> Unit,
    onWorkoutEditClick: (Int) -> Unit
) {
    LaunchedEffect(Unit) {
        viewModel.loadEntries()
        workoutsViewModel.loadEntries()
    }

    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    //Uso equivalente a filter en JS, se pasa en el lambda la seccion y meal como (it)
    //Se guarda el resultado (un mapa) en la variable grouped
    val grouped = viewModel.entriesOn(selectedDate).groupBy { it.section }

    val context = LocalContext.current
    val goalStore = remember { GoalStore(context.applicationContext) }
    val goal by goalStore.goalFlow.collectAsState(initial = 2000)

    val foodCalories = viewModel.foodCaloriesOn(selectedDate)
    val exerciseCalories = workoutsViewModel.exerciseCaloriesOn(selectedDate)

    //Remaining cals
    val remaining = goal - foodCalories + exerciseCalories

    AppBackground { // Fondo personalizado de la app
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(top = 10.dp, bottom = 110.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                DateSelectorRow(
                    date = selectedDate,
                    onPrev = {selectedDate = selectedDate.minusDays(1)},
                    onNext = {selectedDate = selectedDate.plusDays(1)},
                    onToday = {selectedDate = LocalDate.now()}
                )
            }

            item {
                val progress = (foodCalories.toFloat() / goal.coerceAtLeast(1)).coerceIn(0f, 1f)
                Card(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Column(Modifier.padding(20.dp)) {
                        Text("Calorías restantes", style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Spacer(Modifier.height(4.dp))
                        Text("$remaining", style = MaterialTheme.typography.headlineLarge,
                            fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                        Spacer(Modifier.height(14.dp))
                        LinearProgressIndicator(
                            progress = progress,
                            modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(4.dp)),
                            color = MaterialTheme.colorScheme.primary,
                            trackColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                        Spacer(Modifier.height(16.dp))
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            CalorieStat("Meta", goal)
                            CalorieStat("Comida", foodCalories)
                            CalorieStat("Ejercicio", exerciseCalories)
                        }
                    }
                }
            }

                //Se itera sobre cada entrie o valor en el enum. Por cada tipo que hay se crea un
                //componente de MealSection donde se le pasa la seccion arriba, los meals del API
                //agrupados por la especifica seccion y una funcion vacia para llamar adentro
            items(
                    items = MealType.entries,
                    key = {type -> type.name}
            )
            { type ->
                val label = when (type) {
                    MealType.BREAKFAST -> "Desayuno"
                    MealType.LUNCH     -> "Almuerzo"
                    MealType.DINNER    -> "Cena"
                    MealType.SNACKS    -> "Snacks"
                }
                MealSection(
                    section = label,
                    entries = grouped[type].orEmpty(),
                    onAddClick = onAddMealClick,
                    onEntryDelete = {entry -> viewModel.deleteEntry(entry.id)},
                    onEntryClick = {entry -> onMealEditClick(entry.id)}
                )
            }
            item{
                WorkoutSection(
                    entries = workoutsViewModel.entriesOn(selectedDate),
                    onAddClick = onAddWorkoutClick,
                    onEntryDelete = {entry -> workoutsViewModel.deleteEntry(entry.id)},
                    onEntryClick = {entry -> onWorkoutEditClick(entry.id)}
                )
            }

        }
    }
}

@Composable
private fun CalorieStat(label: String, value: Int) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text("$value", style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
        Text(label, style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}
