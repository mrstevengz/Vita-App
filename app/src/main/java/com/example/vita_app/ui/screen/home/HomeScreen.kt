package com.example.vita_app.ui.screen.home

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.vita_app.ui.components.AppBackground
import com.example.vita_app.ui.components.BottomBar
import com.example.vita_app.ui.components.HomeTopBar
import com.example.vita_app.ui.screen.meals.MealsViewModel
import com.example.vita_app.ui.screen.workouts.WorkoutViewModel
import com.example.vita_app.ui.theme.PineBlue
import kotlin.time.ExperimentalTime

@Composable
fun HomeScreen(
    onCalorieCardClick: () -> Unit,
    onWorkoutCardClick: () -> Unit,
    mealsViewModel: MealsViewModel,
    workoutViewModel: WorkoutViewModel
) {

    //Variables para llenar contenido

    LaunchedEffect(Unit) {
        mealsViewModel.loadEntries()
        workoutViewModel.loadEntries()
    }
    val goal = 2000
    val foodCalories = mealsViewModel.foodCalories
    val exerciseCalories = workoutViewModel.exerciseCalories
    val exerciseTime = workoutViewModel.exerciseTime

    //Remaining cals
    val remaining = goal - foodCalories + exerciseCalories

    fun formatMinutes(exerciseTime: Int): String {
        val hours = exerciseTime / 60
        val minutes = exerciseTime % 60

        return "$hours:${"%02d".format(minutes)}"
    }
    //Contenido principal de la APP
    AppBackground {

        Column(
            modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
                ) {

                    HomeTopBar("Yo") //

                    Spacer(modifier = Modifier.height(20.dp))

                    Column(
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                    ) {

                        // CARD PRINCIPAL (Calorías)
                        Text("Today", fontWeight = FontWeight.Bold, fontSize = 24.sp,
                            modifier = Modifier.padding(bottom = 8.dp))

                        Card(
                            shape = RoundedCornerShape(28.dp),
                            modifier = Modifier.fillMaxWidth().height(250.dp),
                            elevation = CardDefaults.cardElevation(4.dp),
                            onClick = onCalorieCardClick
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {

                                Text("Calories", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                                Text("Remaining = Goal - Food", fontSize = 12.sp, color = Color.Gray)

                                Spacer(modifier = Modifier.height(16.dp))

                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {

                                    // Circulo de progreso (hard-coded)
                                    Box(
                                        contentAlignment = Alignment.Center,
                                        modifier = Modifier.size(120.dp)
                                    ) {

                                        Canvas(modifier = Modifier.fillMaxSize()) {
                                            drawArc(
                                                color = Color.LightGray,
                                                startAngle = 0f,
                                                sweepAngle = 360f,
                                                useCenter = false,
                                                style = Stroke(width = 20f)
                                            )

                                            drawArc(
                                                color = PineBlue,
                                                startAngle = -90f,
                                                sweepAngle = 220f,
                                                useCenter = false,
                                                style = Stroke(width = 20f)
                                            )
                                        }

                                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                            Text("$remaining", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                                            Text("Remaining", fontSize = 12.sp)
                                        }
                                    }

                                    Spacer(modifier = Modifier.width(16.dp))

                                    Column {

                                        Text("Base Goal", color = Color.Gray, fontSize = 12.sp)
                                        Text("$goal", fontWeight = FontWeight.Bold)

                                        Spacer(modifier = Modifier.height(8.dp))

                                        Text("Food", color = PineBlue, fontSize = 12.sp)
                                        Text("$foodCalories", fontWeight = FontWeight.Bold)

                                        Spacer(modifier = Modifier.height(8.dp))

                                        Text("Exercise", color = Color.Gray, fontSize = 12.sp)
                                        Text("$exerciseCalories", fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        // INDICADOR
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            repeat(2) {
                                Box(
                                    modifier = Modifier
                                        .size(14.dp)
                                        .padding(2.dp)
                                        .clip(CircleShape)
                                        .background(if (it == 0) PineBlue else Color.LightGray)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        // CARDS INFERIORES
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {

                            // STEPS
                            Card(
                                modifier = Modifier.weight(1f).height(160.dp),
                                shape = RoundedCornerShape(16.dp),
                                elevation = CardDefaults.cardElevation(6.dp)
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {

                                    Text("Steps", fontWeight = FontWeight.Bold)

                                    Spacer(modifier = Modifier.height(8.dp))

                                    Text("7,456", fontSize = 22.sp, fontWeight = FontWeight.Bold)

                                    Text("Goal: 10,000 steps", fontSize = 12.sp, color = Color.Gray)

                                    Spacer(modifier = Modifier.height(8.dp))

                                    LinearProgressIndicator(
                                        progress = 0.7f,
                                        modifier = Modifier.fillMaxWidth(),
                                        color = PineBlue
                                    )
                                }
                            }

                            // EXERCISE
                            Card(
                                modifier = Modifier.weight(1f).height(160.dp),
                                shape = RoundedCornerShape(16.dp),
                                elevation = CardDefaults.cardElevation(6.dp),
                                onClick = onWorkoutCardClick
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {

                                    Row(
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text("Exercise", fontWeight = FontWeight.Bold)
                                        Text("+", fontWeight = FontWeight.Bold)
                                    }

                                    Spacer(modifier = Modifier.height(8.dp))

                                    Text("$exerciseCalories cal", fontWeight = FontWeight.Bold)

                                    Spacer(modifier = Modifier.height(8.dp))

                                    Text("${formatMinutes(exerciseTime)} hr", color = Color.Gray)
                                }
                            }
                        } }
        }
    }
}