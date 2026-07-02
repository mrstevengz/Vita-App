package com.example.vita_app.ui.screen.meals

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextButton
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.vita_app.data.remote.model.MealType
import com.example.vita_app.ui.screen.image.ImageViewModel
import com.example.vita_app.ui.util.label
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogScreen(
    viewModel: MealsViewModel,
    onMealClick: (Int) -> Unit,
    onBack: () -> Unit
) {
    val catalog = viewModel.meals
    LaunchedEffect(Unit) { viewModel.loadMeals() }

    var query by remember { mutableStateOf("") }
    val filtered = catalog.filter { it.name.contains(query, ignoreCase = true) }

    val imageViewModel: ImageViewModel = viewModel()
    val context = LocalContext.current
    var selectedSection by remember { mutableStateOf(MealType.BREAKFAST) }

    val pickImage = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri -> if (uri != null) imageViewModel.analyze(uri) }

    //Abrir camara

    var cameraUri by remember {mutableStateOf<Uri?>(null)}
    var showSourceDialog by remember {mutableStateOf(false)}

    val takePhoto = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) cameraUri?.let {
            imageViewModel.analyze(it)
        }
    }

    fun newCameraUri(): Uri {
        val dir = File(context.cacheDir, "images").apply { mkdirs() }
        val file = File(dir, "capture_${System.currentTimeMillis()}.jpg")
        return FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
    }

    LaunchedEffect(Unit) {
        imageViewModel.events.collect { msg ->
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
        }
    }

    if (imageViewModel.results.isNotEmpty()) {
        AlertDialog(
            onDismissRequest = { imageViewModel.clear() },
            title = { Text("Comidas detectadas") },
            text = {
                Column {
                    Text("Sección", fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface)
                    FlowRow(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        MealType.entries.forEach { type ->
                            FilterChip(
                                selected = selectedSection == type,
                                onClick = { selectedSection = type },
                                label = { Text(type.label()) }
                            )
                        }
                    }
                    Spacer(Modifier.height(12.dp))

                    imageViewModel.results.forEach { food ->
                        val mealId = food.mealId
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(food.detectedName, fontWeight = FontWeight.Medium,
                                    color = MaterialTheme.colorScheme.onSurface)
                                Text(
                                    if (mealId != null) "${food.mealName} · ~${food.estimatedGrams.toInt()} g"
                                    else "sin match en catálogo",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            if (mealId != null) {
                                TextButton(onClick = {
                                    viewModel.addEntry(mealId, food.estimatedGrams.toInt().toString(), selectedSection)
                                    imageViewModel.removeResult(food)
                                }) { Text("Agregar") }
                            } else {
                                TextButton(onClick = { imageViewModel.removeResult(food) }) {
                                    Text("Descartar")
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { imageViewModel.clear() }) { Text("Cerrar") }
            }
        )
    }

    if (showSourceDialog) {
        AlertDialog(
            onDismissRequest = { showSourceDialog = false },
            title = { Text("Escanear comida") },
            text = { Text("¿De dónde tomamos la foto?") },
            confirmButton = {
                TextButton(onClick = {
                    showSourceDialog = false
                    val uri = newCameraUri()
                    cameraUri = uri
                    takePhoto.launch(uri)
                }) { Text("Tomar foto") }
            },
            dismissButton = {
                TextButton(onClick = {
                    showSourceDialog = false
                    pickImage.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                }) { Text("Elegir de galería") }
            }
        )
    }


    AppBackground {
        Column(modifier = Modifier.fillMaxSize()) {
            CenterAlignedTopAppBar(
                title = { Text("Catálogo") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        showSourceDialog = true
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
                onValueChange = { query = it },
                placeholder = { Text("Buscar comida") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp)
            )

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (filtered.isEmpty()) {
                    item {
                        Text(
                            if (catalog.isEmpty()) "No hay comidas en el catálogo"
                            else "Sin resultados para \"$query\"",
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }

                items(items = filtered, key = { it.id }) { meal ->
                    Card(
                        modifier = Modifier.fillMaxWidth().clickable { onMealClick(meal.id) },
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(1.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier.size(40.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(MaterialTheme.colorScheme.surfaceVariant),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(Icons.Default.Restaurant, null,
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(20.dp))
                                }
                                Spacer(Modifier.width(12.dp))
                                Text(meal.name, style = MaterialTheme.typography.bodyLarge,
                                    fontWeight = FontWeight.Medium,
                                    color = MaterialTheme.colorScheme.onSurface)
                            }
                            Text("${meal.calories} cal/100g",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
            }
        }
    }
}