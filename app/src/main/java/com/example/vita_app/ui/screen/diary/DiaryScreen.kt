package com.example.vita_app.ui.screen.diary

// Proposito: Pantalla del diario. Agrupa entradas por seccion y muestra comidas registradas.

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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.vita_app.data.remote.model.MealType
import com.example.vita_app.ui.components.AppBackground
import com.example.vita_app.ui.components.HomeTopBar
import com.example.vita_app.ui.components.MealSection
import com.example.vita_app.ui.screen.meals.MealsViewModel
import com.example.vita_app.ui.theme.CarbonBlack


@Composable
// Muestra el diario del usuario agrupado por desayuno, almuerzo, cena y snacks.
fun DiaryScreen(
    viewModel: MealsViewModel, //Se manda a llamar el viewmodel de Meals para obtener los metodos
    onAddMealClick: () -> Unit,
    onMealEditClick: (Int) -> Unit
) {
    LaunchedEffect(Unit) {
        viewModel.loadEntries()
    }

    val entries =
        viewModel.entries//Se manda a llamar la variable que contiene la lista de meals ya fetcheadas

    //Uso equivalente a filter en JS, se pasa en el lambda la seccion y meal como (it)
    //Se guarda el resultado (un mapa) en la variable grouped
    // Agrupa las entradas por seccion para renderizar cada bloque del dia.
    val grouped = entries.groupBy { it.section }

    AppBackground { // Fondo personalizado de la app
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(top = 10.dp, bottom = 110.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                HomeTopBar("Yo")
            }

            item {
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
            }

                //Se itera sobre cada entrie o valor en el enum. Por cada tipo que hay se crea un
                //componente de MealSection donde se le pasa la seccion arriba, los meals del API
                //agrupados por la especifica seccion y una funcion vacia para llamar adentro
            items(
                    items = MealType.entries,
                    key = {type -> type.name}
            )
            { type ->
                MealSection(
                    section = type.name,
                    entries = grouped[type].orEmpty(),
                    onAddClick = onAddMealClick,
                    onEntryDelete = {entry -> viewModel.deleteEntry(entry.id)},
                    onEntryClick = {entry -> onMealEditClick(entry.id)}
                )
            }

        }
    }
}
