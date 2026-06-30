package com.example.vita_app.ui.screen.workouts

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vita_app.data.remote.model.WorkoutEntryRequest
import com.example.vita_app.data.remote.model.WorkoutEntryResponse
import com.example.vita_app.data.remote.model.WorkoutResponse
import com.example.vita_app.data.repository.WorkoutEntryRepo
import com.example.vita_app.data.repository.WorkoutRepo
import com.example.vita_app.ui.util.isOnDate
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.time.LocalDate

class WorkoutViewModel: ViewModel() {
    private val workoutRepo = WorkoutRepo()
    private val entryRepo = WorkoutEntryRepo()

    val workouts = mutableStateListOf<WorkoutResponse>()
    val entries = mutableStateListOf<WorkoutEntryResponse>()

    private val _events = Channel<String>()
    val events = _events.receiveAsFlow()

    fun entriesOn(date: LocalDate): List<WorkoutEntryResponse> =
        entries.filter { isOnDate(it.date, date) }

    fun exerciseCaloriesOn(date: LocalDate): Int =
        entriesOn(date).sumOf { entry ->
            val perHour = entry.workout.caloriesPerHour.toDoubleOrNull() ?: 0.0
            val minutes = entry.minutes.toDoubleOrNull() ?: 0.0
            perHour * minutes / 60.0
        }.toInt()

    fun exerciseTimeOn(date: LocalDate): Int =
        entriesOn(date).sumOf{it.minutes.toDoubleOrNull() ?: 0.0}.toInt()

    val exerciseCalories: Int get() = exerciseCaloriesOn(LocalDate.now())
    val exerciseTime: Int get() = exerciseTimeOn(LocalDate.now())

    init {
        loadWorkouts()
    }

    fun loadWorkouts() {
        viewModelScope.launch {
            try {
                val data = workoutRepo.getWorkouts()
                workouts.clear()
                workouts.addAll(data)
            } catch (e: Exception) {
                println("Fallo al cargar workouts: ${e.message}")
            }
        }
    }

    fun loadEntries() {
        viewModelScope.launch {
            try {
                val data = entryRepo.getEntries()
                entries.clear()
                entries.addAll(data)
            } catch (e: Exception) {
                println("Fallo al cargar workout entries: ${e.message}")
            }

        }
    }

    fun addEntry(workoutId: Int, minutes: String) {
        viewModelScope.launch {
            try {
                entryRepo.createWorkoutEntry(WorkoutEntryRequest(workoutId, minutes))
                loadEntries()
                _events.send("Workout added")
            } catch (e: Exception) {
                println("Fallo al agregar un entry ${e.message}")
                _events.send("Error saving the workout")
            }
        }
    }

    fun deleteEntry(id: Int) {
        viewModelScope.launch {
            try {
                entryRepo.deleteWorkoutEntry(id)
                entries.removeAll {it.id == id}
                _events.send("Workout deleted")
            }catch (e: Exception) {
                println("Fallo al eliminar entry: ${e.message}")
                _events.send("Error deleting the workout")
            }
        }
    }

    fun updateEntry(id: Int, minutes: String) {
        viewModelScope.launch {
            try {
                val current = entries.find {it.id == id} ?: return@launch
                entryRepo.updateWorkoutEntry(id, WorkoutEntryRequest(current.workoutId, minutes))
                loadEntries()
                _events.send("Workout updated")
            }catch (e: Exception) {
                println("Fallo al editar workout: ${e.message}")
                _events.send("Error updating the workout")
            }
        }
    }
}