package com.example.vita_app.ui.screen.workouts

// Proposito: ViewModel de ejercicios. Mantiene catalogo, registros y operaciones sobre workout entries.

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

// Mantiene el estado de workouts y registros para la parte de ejercicio.
class WorkoutViewModel: ViewModel() {
    private val workoutRepo = WorkoutRepo()
    private val entryRepo = WorkoutEntryRepo()

    val workouts = mutableStateListOf<WorkoutResponse>()
    val entries = mutableStateListOf<WorkoutEntryResponse>()

    private val _events = Channel<String>()
    val events = _events.receiveAsFlow()

    init {
        loadWorkouts()
    }

    // Carga el catalogo de ejercicios desde la API.
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

    // Carga los ejercicios registrados por el usuario autenticado.
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

    // Registra minutos realizados para un workout seleccionado.
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

    // Elimina un registro de ejercicio y actualiza la lista.
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

    // Actualiza los minutos de un registro de ejercicio existente.
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