package com.example.vita_app.ui.screen.workouts

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vita_app.data.remote.model.WorkoutEntryRequest
import com.example.vita_app.data.remote.model.WorkoutEntryResponse
import com.example.vita_app.data.remote.model.WorkoutResponse
import com.example.vita_app.data.repository.WorkoutEntryRepo
import com.example.vita_app.data.repository.WorkoutRepo
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class WorkoutViewModel: ViewModel() {
    private val workoutRepo = WorkoutRepo()
    private val entryRepo = WorkoutEntryRepo()

    val workouts = mutableStateListOf<WorkoutResponse>()
    val entries = mutableStateListOf<WorkoutEntryResponse>()

    private val _events = Channel<String>()
    val events = _events.receiveAsFlow()

    val exerciseCalories: Int
        get() = entries.sumOf { entry ->
        val perHour = entry.workout.caloriesPerHour.toDoubleOrNull() ?: 0.0
        val minutes = entry.minutes.toDoubleOrNull() ?: 0.0
        perHour * minutes / 60.0
    }.toInt()

    val exerciseTime: Int
        get() = entries.sumOf { entry ->
            val totalMinutes = entry.minutes.toDoubleOrNull() ?: 0.0
            totalMinutes
        }.toInt()

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
            }catch (e: Exception) {
                println("Fallo al editar workout: ${e.message}")
                _events.send("Error updating the workout")
            }
        }
    }
}