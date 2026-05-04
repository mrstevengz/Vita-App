package com.example.vita_app.ui.screen.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.vita_app.ui.components.AppBackground
import com.example.vita_app.ui.components.BottomBar
import com.example.vita_app.ui.components.HomeTopBar
import com.example.vita_app.ui.components.MealSection
import com.example.vita_app.ui.theme.CarbonBlack


@Composable
fun DiaryScreen(name: String, navController: NavHostController) {
    Scaffold(
        bottomBar = { BottomBar(navController, name) }
    ) {
        innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            AppBackground {
                Column(
                    modifier = Modifier.fillMaxSize().padding(10.dp)
                ) {

                    HomeTopBar(name)
                    Spacer(modifier = Modifier.height(20.dp))

                    // Panel de calorias
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        shape = RoundedCornerShape(20.dp),
                        elevation = CardDefaults.cardElevation(6.dp)
                    ) {

                        Column(modifier = Modifier.padding(16.dp)) {

                            Text(
                                "Calories Remaining",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = CarbonBlack
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                // GOAL
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text("1300", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                                    Text("Goal", fontSize = 12.sp, color = Color.Gray)
                                }

                                Text("-", fontSize = 18.sp)

                                // FOOD
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text("259", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                                    Text("Food", fontSize = 12.sp, color = Color.Gray)
                                }

                                Text("+", fontSize = 18.sp)

                                // EXERCISE
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text("3", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                                    Text("Exercise", fontSize = 12.sp, color = Color.Gray)
                                }

                                Text("=", fontSize = 18.sp)

                                // RESULTADO
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        "1044",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 20.sp,
                                        color = Color(0xFF1FA3A3)
                                    )
                                    Text("Remaining", fontSize = 12.sp, color = Color.Gray)
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Secciones de comidas para insertar meals
                    MealSection(title = "Breakfast")
                    MealSection(title = "Lunch")
                    MealSection(title = "Dinner")
                    MealSection(title = "Snacks")
                    MealSection(title = "Water")
                }
            }
        }
    }

}