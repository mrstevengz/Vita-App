package com.example.vita_app.ui.screen.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Verified
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.vita_app.ui.components.AppBackground
import com.example.vita_app.ui.theme.BackgroundLight
import com.example.vita_app.ui.theme.CarbonBlack
import com.example.vita_app.ui.theme.PineBlue
import com.example.vita_app.ui.theme.White

/**
 * Pantalla reutilizable para agregar alimentos a una sección de comida.
 *
 * @param mealType Sección desde donde se abrió (Breakfast, Lunch, Dinner, Snacks).
 *                 Se muestra en la parte superior y permite saber a qué comida agregar.
 * @param onClose  Callback al presionar la X (cerrar y volver al Diary).
 */
@Composable
fun AddFoodScreen(
    mealType: String,
    onClose: () -> Unit
) {
    // Estado local del input de búsqueda (sin lógica conectada aún)
    var query by remember { mutableStateOf("") }
    // Tab seleccionado (All, My Meals, My Recipes, My Foods)
    var selectedTab by remember { mutableStateOf("All") }
    val tabs = listOf("All", "My Meals", "My Recipes", "My Foods")

    AppBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Top bar: X de cerrar + tipo de comida arriba al centro
            AddFoodTopBar(mealType = mealType, onClose = onClose)

            Spacer(modifier = Modifier.height(12.dp))

            // Barra de búsqueda
            SearchField(
                value = query,
                onValueChange = { query = it }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Tabs
            TabsRow(
                tabs = tabs,
                selected = selectedTab,
                onSelect = { selectedTab = it }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // "Best Match" + filtro "Only"
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Best Match",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = CarbonBlack
                )
                OnlyChip()
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Card de "best match" (resaltada con borde)
            FoodResultCard(
                name = "Banana",
                detail = "114 cal, 1 medium",
                verified = true,
                highlighted = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                "More Results",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = CarbonBlack
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Resultados estáticos (sin funcionalidad por ahora)
            FoodResultCard("Banana", "105 cal, 1 medium", verified = true)
            Spacer(modifier = Modifier.height(8.dp))
            FoodResultCard("Large Banana", "121 cal, 1 piece, Banana", verified = true)
            Spacer(modifier = Modifier.height(8.dp))
            FoodResultCard("Banana - (One)", "105 cal, 118 gram, Banana - (One)", verified = true)
            Spacer(modifier = Modifier.height(8.dp))
            FoodResultCard("Banana", "72 cal, 1 whole, Generic - DAB", verified = false)

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                "Show more results...",
                color = PineBlue,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

/**
 * Top bar con X (cerrar) a la izquierda y el tipo de comida al centro.
 * El dropdown es solo visual por ahora.
 */
@Composable
private fun AddFoodTopBar(mealType: String, onClose: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 40.dp, bottom = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onClose) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Cerrar",
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
        placeholder = { Text("Buscar alimento") },
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

@Composable
private fun OnlyChip() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .border(1.dp, Color.LightGray, RoundedCornerShape(20.dp))
            .background(White)
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Verified,
            contentDescription = null,
            tint = Color.Gray,
            modifier = Modifier.size(14.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text("Only", fontSize = 12.sp, color = CarbonBlack)
    }
}

/**
 * Tarjeta reutilizable para mostrar un resultado de alimento.
 * @param highlighted Si es el "Best Match" (borde resaltado en PineBlue).
 */
@Composable
private fun FoodResultCard(
    name: String,
    detail: String,
    verified: Boolean,
    highlighted: Boolean = false
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .then(
                if (highlighted) Modifier.border(
                    width = 1.5.dp,
                    color = PineBlue,
                    shape = RoundedCornerShape(16.dp)
                ) else Modifier
            ),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(3.dp),
        colors = CardDefaults.cardColors(containerColor = White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = name,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = CarbonBlack
                    )
                    if (verified) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(
                            imageVector = Icons.Default.Verified,
                            contentDescription = "Verificado",
                            tint = Color(0xFF34C759),
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = detail, fontSize = 13.sp, color = Color.Gray)
            }

            // Botón "+" circular (resaltado si es best match)
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(if (highlighted) PineBlue.copy(alpha = 0.15f) else BackgroundLight)
                    .then(
                        if (highlighted) Modifier.border(1.5.dp, PineBlue, CircleShape)
                        else Modifier
                    )
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Agregar",
                    tint = PineBlue
                )
            }
        }
    }
}
