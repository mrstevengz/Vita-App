package com.example.vita_app.ui.screen.diary

import android.annotation.SuppressLint
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
import com.example.vita_app.data.remote.model.Meal
import com.example.vita_app.data.remote.model.MealType
import com.example.vita_app.ui.components.AppBackground
import com.example.vita_app.ui.components.BottomBar
import com.example.vita_app.ui.components.HomeTopBar
import com.example.vita_app.ui.components.MealSection
import com.example.vita_app.ui.screen.meals.MealsViewModel
import com.example.vita_app.ui.theme.CarbonBlack


@Composable
fun DiaryScreen(
    viewModel: MealsViewModel, //Se manda a llamar el viewmodel de Meals para obtener los metodos
    onAddMealClick: () -> Unit
) {
        val meals = viewModel.meals //Se manda a llamar la variable que contiene la lista de meals ya fetcheadas

        AppBackground { // Fondo personalizado de la app
                Column(
                    modifier = Modifier.fillMaxSize().padding(top = 10.dp)
                ) {
                    // Barra superior con el nombre del usuario
                    HomeTopBar("Yo")
                    Spacer(modifier = Modifier.height(20.dp))

                    //Uso equivalente a filter en JS, se pasa en el lambda la seccion y meal como (it)
                    //Se guarda el resultado (un mapa) en la variable grouped
                    val grouped = meals.groupBy{it.section}

                    // Panel de calorias
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        // Bordes redondeados
                        shape = RoundedCornerShape(20.dp),
                        // Elevación (sombra)
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

                    //Se itera sobre cada entrie o valor en el enum. Por cada tipo que hay se crea un
                    //componente de MealSection donde se le pasa la seccion arriba, los meals del API
                    //agrupados por la especifica seccion y una funcion vacia para llamar adentro
                    MealType.entries.forEach{ type ->
                        MealSection(
                            section = type.name,
                            //Esta linea en especifico, muestra todas las meals por tipo, o ninguna
                            //si esta vacio
                            meals = grouped[type].orEmpty(),
                            onAddClick = onAddMealClick
                        )
                    }

                }
            }
        }