package com.example.vita_app.ui.screen.meals

import android.widget.ImageView
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextButton
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.vita_app.ui.screen.image.ImageViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
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

    val filtered = catalog.filter { it.name.contains(query, ignoreCase = true) }

    //Analisis por imagen

    val imageViewModel: ImageViewModel = viewModel()
    val context = LocalContext.current

    //Photo Picker
    val pickImage = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) imageViewModel.analyze(uri)
    }

    LaunchedEffect(Unit) {
        imageViewModel.events.collect { msg ->
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
        }
    }

    if (imageViewModel.results.isNotEmpty()) {
        AlertDialog(
            onDismissRequest = {imageViewModel.clear()},
            title = {Text("Comidas detectadas")},
            text = {
                Column{
                    imageViewModel.results.forEach {food ->
                        val mealId = food.mealId
                        if(mealId != null) {
                            Row(
                                modifier = Modifier.fillMaxWidth().clickable {
                                    imageViewModel.clear()
                                    onMealClick(mealId)
                                }
                                    .padding(vertical = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("${food.mealName} (~${food.estimatedGrams.toInt()} g)")
                                Text("Agregar", color = Color(0xFF1FA3A3))
                            }
                        } else {
                            Text(
                                "${food.detectedName} — sin match",
                                color = Color.Gray,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = {imageViewModel.clear() }) {Text("Cerrar")}
            }
        )

    }


    AppBackground {
        Column(modifier = Modifier.fillMaxSize()) {
            CenterAlignedTopAppBar(
                title = {Text("Catalogo")},
                navigationIcon = {IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver")
                }
                },
                actions = {
                    IconButton(onClick = {
                        pickImage.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                    }) {
                        Icon(Icons.Default.PhotoCamera, contentDescription = "Escanear comida")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )

            if (imageViewModel.isLoading) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }

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