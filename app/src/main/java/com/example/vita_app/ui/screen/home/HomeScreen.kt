package com.example.vita_app.ui.screen.home

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.vita_app.data.GoalStore
import com.example.vita_app.data.remote.model.DiaryEntryResponse
import com.example.vita_app.ui.components.AppBackground
import com.example.vita_app.ui.components.BottomBar
import com.example.vita_app.ui.components.HomeTopBar
import com.example.vita_app.ui.screen.meals.MealsViewModel
import com.example.vita_app.ui.screen.workouts.WorkoutViewModel
import com.example.vita_app.ui.theme.PineBlue
import com.example.vita_app.ui.util.formatted
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.time.ExperimentalTime

@Composable
fun HomeScreen(
    onCalorieCardClick: () -> Unit,
    onWorkoutCardClick: () -> Unit,
    mealsViewModel: MealsViewModel,
    workoutViewModel: WorkoutViewModel,
    onLogout: () -> Unit
) {
    LaunchedEffect(Unit) {
        mealsViewModel.loadEntries()
        workoutViewModel.loadEntries()
    }

    //Recoge las variables Context del viewmodel y las manda a llamar para cargarlas en UI
    val context = LocalContext.current
    val goalStore = remember { GoalStore(context.applicationContext) }
    val goal by goalStore.goalFlow.collectAsState(initial = 2000)
    val scope = rememberCoroutineScope()
    var showGoalDialog by remember { mutableStateOf(false) }

    //Variables derivadas del viewmodel
    val foodCalories = mealsViewModel.foodCalories
    val exerciseCalories = workoutViewModel.exerciseCalories
    val exerciseTime = workoutViewModel.exerciseTime
    val remaining = goal - foodCalories + exerciseCalories
    val macros = mealsViewModel.macros
    val todayMeals = mealsViewModel.entriesOn(LocalDate.now())

    //Hace que los minutos se muestren en horas (para el card de ejercicio)
    fun formatMinutes(min: Int) = "${min / 60}:${"%02d".format(min % 60)}"

    val ringTrack = MaterialTheme.colorScheme.surfaceVariant
    val ringProgress = if (remaining < 0) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
    //Progreso de 0 a 1 para el anillo
    val progress = (foodCalories.toFloat() / goal.coerceAtLeast(1)).coerceIn(0f, 1f)
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(durationMillis = 900, easing = FastOutSlowInEasing),
        label = "ringProgress"
    )

    val animatedRemaining by animateIntAsState(
        targetValue = remaining,
        animationSpec = tween(durationMillis = 700, easing = FastOutSlowInEasing),
        label = "remainingCount"
    )
    val todayLabel = remember { //Se calcula una vez la fecha
        LocalDate.now().format(DateTimeFormatter.ofPattern("EEEE, d MMM", Locale("es")))
    }

    AppBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 100.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 20.dp, bottom = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(todayLabel, style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text("Bienvenido", style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)
                }
                IconButton(onClick = onLogout) {
                    Icon(Icons.AutoMirrored.Filled.Logout, "Cerrar sesión",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }

            // ---- Hero de calorías ----
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(2.dp),
                onClick = onCalorieCardClick
            ) {
                Row(modifier = Modifier.padding(20.dp), verticalAlignment = Alignment.CenterVertically) {
                    Box(contentAlignment = Alignment.Center, modifier = Modifier.size(128.dp)) {
                        Canvas(Modifier.fillMaxSize()) {
                            drawArc(ringTrack, 0f, 360f, false, style = Stroke(30f, cap = StrokeCap.Round))
                            drawArc(ringProgress, -90f, animatedProgress * 360f, false, style = Stroke(30f, cap = StrokeCap.Round))
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(animatedRemaining.formatted(), style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold,
                                color = if(remaining < 0)
                                    MaterialTheme.colorScheme.error
                                        else
                                    MaterialTheme.colorScheme.onSurface)
                            Text("kcal restantes", style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                    Spacer(Modifier.width(20.dp))
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        StatItem("Meta", goal.formatted()) { showGoalDialog = true }
                        StatItem("Comida", foodCalories.formatted())
                        StatItem("Ejercicio", exerciseCalories.formatted())
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            // ---- Macros (tarjeta de ancho completo) ----
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(Modifier.padding(20.dp)) {
                    Text("Macros de hoy", style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface)
                    Spacer(Modifier.height(14.dp))
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        MacroStat("Proteína", macros.protein)
                        MacroStat("Carbohidratos", macros.carbs)
                        MacroStat("Grasa", macros.fat)
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            // ---- Ejercicio ----
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(2.dp),
                onClick = onWorkoutCardClick
            ) {
                Column(Modifier.padding(20.dp)) {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Ejercicio", style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface)
                        Icon(Icons.Default.Add, "Agregar ejercicio", tint = MaterialTheme.colorScheme.primary)
                    }
                    Spacer(Modifier.height(8.dp))
                    Text("${exerciseCalories.formatted()} cal quemadas", fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface)
                    Text("${formatMinutes(exerciseTime)} h", style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }

            Spacer(Modifier.height(24.dp))

            // ---- Comidas de hoy ----
            Text("Hoy", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(bottom = 8.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                if (todayMeals.isEmpty()) {
                    Text("Aún no has registrado comidas hoy.",
                        modifier = Modifier.padding(20.dp),
                        color = MaterialTheme.colorScheme.onSurfaceVariant)
                } else {
                    Column {
                        todayMeals.forEach { entry -> TodayMealRow(entry) }
                    }
                }
            }
        }

        if (showGoalDialog) {
            var input by remember { mutableStateOf(goal.toString()) }
            AlertDialog(
                onDismissRequest = { showGoalDialog = false },
                title = { Text("Editar objetivo") },
                text = {
                    OutlinedTextField(
                        value = input,
                        onValueChange = { input = it },
                        label = { Text("Calorías por día") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                },
                confirmButton = {
                    TextButton(onClick = {
                        //String -> Int?, solo si es mayor a 0. Si sobrevive guarda, si algo falla no hace nada
                        input.toIntOrNull()?.takeIf { it > 0 }?.let { scope.launch { goalStore.setGoal(it) } }
                        showGoalDialog = false
                    }) { Text("Guardar") }
                },
                dismissButton = { TextButton(onClick = { showGoalDialog = false }) { Text("Cancelar") } }
            )
        }
    }
}

@Composable
private fun StatItem(label: String, value: String, onClick: (() -> Unit)? = null) {
    Column(modifier = if (onClick != null) Modifier.clickable { onClick() } else Modifier) {
        Text(label, style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(value, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface)
    }
}

@Composable
private fun MacroStat(label: String, grams: Int) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text("${grams}g", style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
        Text(label, style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
private fun TodayMealRow(entry: DiaryEntryResponse) {
    val per100 = entry.meal.calories.toDoubleOrNull() ?: 0.0
    val grams = entry.grams.toDoubleOrNull() ?: 0.0
    val cal = (per100 * grams / 100.0).toInt()
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier.size(36.dp).clip(RoundedCornerShape(10.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Restaurant, null, tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(18.dp))
            }
            Spacer(Modifier.width(10.dp))
            Column {
                Text(entry.meal.name, style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onSurface)
                Text("${grams.toInt()} g", style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
        Text("${cal.formatted()} cal", style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}