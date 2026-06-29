package com.example.vita_app.ui.screen.catalog

// Proposito: Pantalla de catalogo de comidas. Permite buscar y elegir una comida para agregarla al diario.

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.vita_app.ui.components.AppBackground
import com.example.vita_app.ui.screen.meals.MealsViewModel
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
// Muestra todas las comidas disponibles y permite filtrarlas por texto.
fun CatalogScreen(
    viewModel: MealsViewModel,
    onMealClick: (Int) -> Unit,
    onBack: () -> Unit
) {
    val catalog = viewModel.meals

    LaunchedEffect(Unit) {
        viewModel.loadMeals()
    }

    var query by remember { mutableStateOf("") }

    // Filtra en memoria para que la busqueda responda de inmediato mientras se escribe.
    val filtered = catalog.filter { it.name.contains(query, ignoreCase = true) }

    AppBackground {
        Column(modifier = Modifier.fillMaxSize()) {
            CenterAlignedTopAppBar(
                title = {Text("Catalogo")},
                navigationIcon = {IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver")
                }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )

            OutlinedTextField(
                value = query,
                onValueChange = {query = it},
                placeholder = {Text("Buscar comida")},
                leadingIcon = {Icon(Icons.Default.Search, contentDescription = null) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp)
            )



            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                if(filtered.isEmpty()) {
                    item {
                        Text(
                            if (catalog.isEmpty()) "No hay comidas en el catalogo"
                            else "Sin resultados para \"$query\"",
                            color = Color.Gray,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }

                items(
                    items = filtered,
                    key = {meal -> meal.id}
                ) { meal ->
                    Card(
                        modifier = Modifier.fillMaxWidth().clickable
                        {
                            onMealClick(meal.id)
                        },
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                            )
                        {
                            Text(meal.name, fontWeight = FontWeight.Medium)
                            Text("${meal.calories} cal / 100g", color = Color.Gray, fontSize = 12.sp)
                        }
                    }
                }
            }
        }
    }
}