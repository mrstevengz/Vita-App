package com.example.vita_app.ui.components

// Proposito: Componentes visuales para agrupar y mostrar comidas del diario, incluyendo swipe para borrar.

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.vita_app.data.remote.model.DiaryEntryResponse
import com.example.vita_app.ui.theme.CarbonBlack


@Composable
// Renderiza una seccion del diario y sus comidas registradas.
fun MealSection(
    /*La seccion de meal tiene como parametros su seccion especifica, la lista de meals que empiezan vacias,
    * y una funcion para cuando se cliquee (abre addmeal) y cuando se desea borrar por medio de un swipe*/
    section: String,
    entries: List<DiaryEntryResponse> = emptyList(),
    onAddClick: () -> Unit = {},
    onEntryDelete: (DiaryEntryResponse) -> Unit = {},
    onEntryClick: (DiaryEntryResponse) -> Unit = {}
) {
    Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp)) {
        Text(section, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = CarbonBlack)
        Spacer(Modifier.height(8.dp))

        //CARDS de cada seccion

        Card(
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(4.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            /*SE REALIZA UN CARD DENTRO DE LA COLUMNA DE SECCION, donde por cada meal adentro de una seccion, se le otorga
            * una llave (que es su ID) y se crea un MealRow con funcionalidad de swipe */
            Column {
                entries.forEach { entry ->
                    key(entry.id) {
                        EntryRow(entry = entry, onDelete = onEntryDelete, onClick = onEntryClick)
                    }
                }
            }

            Spacer(Modifier.height(8.dp))

            //Add Food Row

            Row(
                modifier = Modifier.fillMaxWidth().clickable {onAddClick()}.padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("ADD FOOD", color = Color(0xFF1FA3A3), fontWeight = FontWeight.Bold)
                Text("...", fontSize = 20.sp, color = Color.Gray)
            }
        }
    }
}

//Se crea un private row donde se le otorga un swipe, un lambda para pasar un meal y realizar una accion (delete) y el UI
@Composable
private fun EntryRow(entry: DiaryEntryResponse, onDelete: (DiaryEntryResponse) -> Unit, onClick: (DiaryEntryResponse) -> Unit) {
    //Se crea un SwipeToDismissBoxState Object
    // Estado que controla el gesto de swipe para detectar eliminacion.
    val dismissState = rememberSwipeToDismissBoxState()

    //Si cambia el valor del dismiss state, se confirma que haya terminado (end to start) y se mand a allamar la funcion
    //para borrar el row especifico
    LaunchedEffect(dismissState.currentValue) {
        if (dismissState.currentValue == SwipeToDismissBoxValue.EndToStart) {
            onDelete(entry)
        }
    }

    //Calorias de la entrada = calorias por 100g
    val per100 = entry.meal.calories.toDoubleOrNull() ?: 0.0
    //Gramos del meal
    val grams = entry.grams.toDoubleOrNull() ?: 0.0
    //Calorias totales = calorias * gramos / 100
    // Calcula calorias reales segun los gramos registrados por el usuario.
    val totalCal = (per100 * grams / 100.0).toInt()

    //En el objeto se le asigna un estado (dismiss state), se desactiva la opcion para hacer swipe de derecha a izquierda,
    // y se le da un diseño. Es el que esta debajo del row y lo que se mira al hacerle swipe
    SwipeToDismissBox(
        state = dismissState,
        enableDismissFromStartToEnd = false,
        backgroundContent = {
            Box(
                modifier = Modifier.fillMaxSize().background(Color.Red).padding(horizontal = 20.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                Icon(Icons.Default.Delete, "Delete", tint = Color.White)
            }
        }
    ) {
        /*Adentro del swipe object, se realiza el row que estara por encima. En este caso el row de comida normal*/
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .padding(vertical = 12.dp, horizontal = 8.dp)
                .clickable{onClick(entry)},

            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Izquierda: nombre (del catálogo) + gramos
            Column {
                Text(entry.meal.name, fontWeight = FontWeight.Medium)
                Text("${grams.toInt()} g", fontSize = 12.sp, color = Color.Gray)
            }
            // Derecha: calorías calculadas
            Text("$totalCal cal", color = Color.Gray)
        }
        }
    }