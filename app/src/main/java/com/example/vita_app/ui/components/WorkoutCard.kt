package com.example.vita_app.ui.components

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
import com.example.vita_app.data.remote.model.WorkoutEntryResponse
import com.example.vita_app.ui.theme.CarbonBlack
import kotlin.collections.forEach

@Composable
fun WorkoutSection(
    entries: List<WorkoutEntryResponse> = emptyList(),
    onAddClick: () -> Unit = {},
    onEntryDelete: (WorkoutEntryResponse) -> Unit = {},
    onEntryClick: (WorkoutEntryResponse) -> Unit = {}
) {
    Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp)) {
        Text("Exercise", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = CarbonBlack)
        Spacer(Modifier.height(8.dp))


        Card(
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(4.dp),
            modifier = Modifier.fillMaxWidth()
        ) {

            Column {
                entries.forEach { entry ->
                    key(entry.id) {
                        WorkoutEntryRow(entry = entry, onDelete = onEntryDelete, onClick = onEntryClick)
                    }
                }
            }

            Spacer(Modifier.height(8.dp))

            //Add Workout Row

            Row(
                modifier = Modifier.fillMaxWidth().clickable {onAddClick()}.padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("ADD WORKOUT", color = Color(0xFF1FA3A3), fontWeight = FontWeight.Bold)
                Text("...", fontSize = 20.sp, color = Color.Gray)
            }
        }
    }
}

//Se crea un private row donde se le otorga un swipe, un lambda para pasar un ejercicio y realizar una accion (delete) y el UI
@Composable
private fun WorkoutEntryRow(
    entry: WorkoutEntryResponse,
    onDelete: (WorkoutEntryResponse) -> Unit,
    onClick: (WorkoutEntryResponse) -> Unit
) {
    //Se crea un SwipeToDismissBoxState Object
    val dismissState = rememberSwipeToDismissBoxState()

    //Si cambia el valor del dismiss state, se confirma que haya terminado (end to start) y se mand a allamar la funcion
    //para borrar el row especifico
    LaunchedEffect(dismissState.currentValue) {
        if (dismissState.currentValue == SwipeToDismissBoxValue.EndToStart) {
            onDelete(entry)
        }
    }

    val perHour = entry.workout.caloriesPerHour.toDoubleOrNull() ?: 0.0
    val minutes = entry.minutes.toDoubleOrNull() ?: 0.0
    val burned = (perHour * minutes / 60.0).toInt()

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
        /*Adentro del swipe object, se realiza el row que estara por encima. En este caso el row de ejercicio normal*/
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .padding(vertical = 12.dp, horizontal = 8.dp)
                .clickable {onClick(entry)},

            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Izquierda: nombre del ejercicio + minutos
            Column {
                Text(entry.workout.name, fontWeight = FontWeight.Medium)
                Text("${minutes.toInt()} min", fontSize = 12.sp, color = Color.Gray)
            }
            // Derecha: calorías quemadas
            Text("$burned cal", color = Color.Gray)
        }
    }
}