package com.example.vita_app.ui.screen.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.vita_app.ui.components.AppBackground
import com.example.vita_app.ui.theme.CarbonBlack
import com.example.vita_app.ui.theme.PineBlue
import com.example.vita_app.ui.theme.White

/**
 * Pantalla reutilizable "My Meals" abierta desde el tab del mismo nombre en AddFoodScreen.
 *
 * @param mealType       Sección de comida desde la que se llega (Breakfast, Lunch, Dinner, Snacks).
 *                       Se muestra en la parte superior.
 * @param onBack         Callback al presionar la flecha de volver.
 * @param onCreateMeal   Callback al tocar el botón "Create meal". Sin funcionalidad aún.
 */
@Composable
fun MyMealsScreen(
    mealType: String,
    onBack: () -> Unit,
    onCreateMeal: () -> Unit = {}
) {
    var query by remember { mutableStateOf("") }
    // Tab seleccionado: My Meals viene preseleccionado al entrar a esta pantalla
    var selectedTab by remember { mutableStateOf("My Meals") }
    val tabs = listOf("All", "My Meals")

    AppBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Top bar: flecha de volver + tipo de comida al centro
            TopBar(mealType = mealType, onBack = onBack)

            Spacer(modifier = Modifier.height(12.dp))

            // Barra de búsqueda
            SearchField(value = query, onValueChange = { query = it })

            Spacer(modifier = Modifier.height(16.dp))

            // Tabs (visual; al tocar "All" se podría volver, pero por ahora solo estado)
            TabsRow(
                tabs = tabs,
                selected = selectedTab,
                onSelect = { tab ->
                    if (tab == "All") onBack() // "All" regresa a AddFoodScreen
                    else selectedTab = tab
                }
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Botón Create meal
            CreateMealCard(onClick = onCreateMeal)

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
private fun TopBar(mealType: String, onBack: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 40.dp, bottom = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onBack) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Volver",
                tint = CarbonBlack
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = mealType,
                color = PineBlue,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = null,
                tint = PineBlue
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        // Espacio simétrico para mantener el título centrado
        Spacer(modifier = Modifier.width(48.dp))
    }
}

@Composable
private fun SearchField(value: String, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        singleLine = true,
        placeholder = { Text("Search my meals...") },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null,
                tint = PineBlue
            )
        },
        trailingIcon = {
            if (value.isNotEmpty()) {
                IconButton(onClick = { onValueChange("") }) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = "Limpiar",
                        tint = Color.Gray
                    )
                }
            }
        },
        colors = TextFieldDefaults.colors(
            focusedContainerColor = White,
            unfocusedContainerColor = White
        )
    )
}

@Composable
private fun TabsRow(
    tabs: List<String>,
    selected: String,
    onSelect: (String) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        tabs.forEach { tab ->
            val isSelected = tab == selected
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.clickable { onSelect(tab) }
            ) {
                Text(
                    text = tab,
                    color = if (isSelected) CarbonBlack else Color.Gray,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Box(
                    modifier = Modifier
                        .height(2.dp)
                        .width(24.dp)
                        .background(if (isSelected) CarbonBlack else Color.Transparent)
                )
            }
        }
    }
}

/**
 * Botón "Create meal" estilo card centrada, sin funcionalidad por ahora.
 */
@Composable
private fun CreateMealCard(onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(3.dp),
        colors = CardDefaults.cardColors(containerColor = White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null,
                tint = PineBlue,
                modifier = Modifier.size(28.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Create meal",
                color = PineBlue,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }
    }
}
