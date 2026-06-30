package com.example.vita_app.ui.screen.meals

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vita_app.data.remote.model.DiaryEntryRequest
import com.example.vita_app.data.remote.model.DiaryEntryResponse
import com.example.vita_app.data.remote.model.MealResponse
import com.example.vita_app.data.remote.model.MealType
import com.example.vita_app.data.repository.EntryRepo
import com.example.vita_app.data.repository.MealRepo
import com.example.vita_app.ui.util.isOnDate
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.time.LocalDate

class MealsViewModel: ViewModel() {
    //Se inicializa una instancia del repositorio del api, y un array de Meal's para guardarlo en
    // las pantallas y mostrarlo
    private val mealRepo = MealRepo()
    private val entryRepo = EntryRepo()

    val meals = mutableStateListOf<MealResponse>()
    val entries = mutableStateListOf<DiaryEntryResponse>()
    private val _events = Channel<String>()
    val events = _events.receiveAsFlow()

    //Suma de las calorias de TODAS las meals entries

    fun entriesOn(date: LocalDate): List<DiaryEntryResponse> = entries.filter { isOnDate(it.date, date) }

    fun foodCaloriesOn(date: LocalDate): Int =
        entriesOn(date).sumOf {entry ->
        val calPer100 = entry.meal.calories.toDoubleOrNull() ?: 0.0
        val grams = entry.grams.toDoubleOrNull() ?: 0.0
        calPer100 * grams / 100.0
    }.toInt()

    val foodCalories: Int get() = foodCaloriesOn(LocalDate.now())


    //Cuando se inicializa la clase, siempre va a cargar la lista de Meals del API
    init {
        loadMeals()
    }

    //Funcion para cargar los meals existentes.
    fun loadMeals() {
        viewModelScope.launch {
            //Try Catch para excepciones al cargar
            try {
                //Manda a llamar la funcion de getMeals al API, y guarda la lista en la lista anterior
                val data = mealRepo.getMeals()
                //Se limpia la lista antes de llenarla con la informacion del API.
                //En caso de duplicados, se agregaria informacion encima de estos repetida, por eso se limpia
                meals.clear()
                meals.addAll(data)
            } catch (e: Exception) {
                println("Fallo al cargar los meals ${e.message}")
            }
        }
    }
    fun loadEntries() {
        viewModelScope.launch {
            try {
                val data = entryRepo.getEntries()
                entries.clear()
                entries.addAll(data)
            }catch (e: Exception) {
                println("Fallo al cargar el diario: ${e.message}")
            }
        }
    }

    // -- Acciones del diario ------------------

    fun addEntry(mealId: Int, grams: String, section: MealType) {
        viewModelScope.launch {
            try {
                //Se manda a llamar el metodo de createmeal para el API, solamente se manda
                //el nombre, calorias y la seccion que pertenece

                entryRepo.createEntry(DiaryEntryRequest(mealId, grams, section))


                //Se manda a llamar de nuevo para mantenerse siempre actualizado con la DB
                //Como solo se agrega una comida a la vez, no considero que sea TAN
                // wasteful hacer un HTTP request extra
                loadEntries()
                _events.send("Entry added")
            } catch(e: Exception) {
                println("Fallo al agregar meal: ${e.message}")
                _events.send("Error saving the entry")
            }
        }
    }

    fun deleteEntry(id : Int) {
        //Se manda a llamar el metodo de deleteMeal del repositorio, se le pasa el ID y borra todos los meals que
        // coincidan con el ID (solo puede ser 1 al ser unico). Esto se hace con el fin de agilizar la actualizacion del
        //UI para el usuario.
        /*todo BUSCAR UNA MANERA DE OPTIMIZAR ESTE METODO: UNA OPCION ES BORRAR LOCALMENTE EL ROW PARA EL USUARIO, Y LUEGO
        *  BORRAR EL REQUEST, EN CASO DE QUE FALLE SE PODRIA VOLVER A PONER EL ROW EN EL UI. */
        viewModelScope.launch {
            try {
                entryRepo.deleteEntry(id)
                entries.removeAll {it.id == id}
                _events.send("Entry deleted")
            } catch (ex: Exception) { // restore on failure
                println("Fallo al eliminar entry: ${ex.message}")
                _events.send("Error deleting the entry")
            }
        }
    }

    fun updateEntry(id:Int, grams: String, section: MealType) {
        viewModelScope.launch {
            try {

                val current = entries.find { it.id == id } ?: return@launch
                entryRepo.updateEntry(id, DiaryEntryRequest(current.mealId, grams, section))
                loadEntries()
                _events.send("Entry updated")
            } catch (ex: Exception) {
                println("Fallo al editar el entry: ${ex.message}")
                _events.send("Error updating the entry")
            }
        }
    }
}