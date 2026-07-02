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
    private val mealRepo = MealRepo() //Catalogo de comidas
    private val entryRepo = EntryRepo() //Entradas de diario (requieren token)

    //ESTADO OBSERVABLE. La UI lee y recompone al calcular
    val meals = mutableStateListOf<MealResponse>()
    val entries = mutableStateListOf<DiaryEntryResponse>()

    //EVENTOS (snackbars)
    //Canal privado que solo emite el VM
    private val _events = Channel<String>()
    //receiveAsFlow expone como Flow de read-only
    val events = _events.receiveAsFlow()

    //PROPIEDADES calculadas

    //Filtra TODAS las entradas que caen en date (isOnDate convierte la fecha UTC a la local)

    fun entriesOn(date: LocalDate): List<DiaryEntryResponse> = entries.filter { isOnDate(it.date, date) }

    //Suma las calorias de todas las comidas en un dia: por cada entrada se obtiene las calorias y los gramos, los cuales luego se calculan en calorias
    fun foodCaloriesOn(date: LocalDate): Int =
        entriesOn(date).sumOf {entry ->
        val calPer100 = entry.meal.calories.toDoubleOrNull() ?: 0.0
        val grams = entry.grams.toDoubleOrNull() ?: 0.0
        calPer100 * grams / 100.0
    }.toInt()

    //Se guardan las calorias de HOY, get() calcula las calorias cada vez que se leen (no se guarda)
    val foodCalories: Int get() = foodCaloriesOn(LocalDate.now())

    //Contenedor de datos para los 3 macros
    data class MacroTotals(val protein: Int, val carbs: Int, val fat: Int)

    //Suma la proteina/carbs/grasa de un dia(mismo parseo y regla (100g) ya que asi se ingresan los datos, se guardan en MacroTotals para poder acceder a los 3 macros facilmente
    fun macrosOnDate(date: LocalDate): MacroTotals {
        val entries = entriesOn(date)

        val protein = entries.sumOf { entry ->
            val per100 = entry.meal.protein.toDoubleOrNull() ?: 0.0
            val grams = entry.grams.toDoubleOrNull() ?: 0.0
            per100 * grams / 100.0
        }.toInt()

        val carbs = entries.sumOf { entry ->
            val per100 = entry.meal.carbs.toDoubleOrNull() ?: 0.0
            val grams = entry.grams.toDoubleOrNull() ?: 0.0
            per100 * grams / 100.0
        }.toInt()

        val fat = entries.sumOf { entry ->
            val per100 = entry.meal.fat.toDoubleOrNull() ?: 0.0
            val grams = entry.grams.toDoubleOrNull() ?: 0.0
            per100 * grams / 100.0
        }.toInt()

        return MacroTotals(protein, carbs, fat)
    }

    //Atajo para obtener macros de hoy
    val macros: MacroTotals get() = macrosOnDate(LocalDate.now())



    //Cuando se inicializa la clase, siempre va a cargar la lista de Meals del API
    //Las entradas no se cargan al entrar
    init {
        loadMeals()
    }

    //Funcion para cargar los meals existentes.
    fun loadMeals() {
        viewModelScope.launch { //viewModelScope.launch arranca una corrutina que esta atada a la vida del viewModel, se cancela si el VM muere (usuario sale de a app) y no bloquea el UI
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

    fun clear() {
        entries.clear()
    }

    // -- Acciones del diario ------------------

    fun addEntry(mealId: Int, grams: String, section: MealType) {
        viewModelScope.launch {
            try {
                //Se manda a llamar el metodo de createmeal para el API, solamente se manda
                //el nombre, calorias y la seccion que pertenece

                entryRepo.createEntry(DiaryEntryRequest(mealId, grams, section))


                //Se manda a llamar de nuevo para mantenerse siempre actualizado con la DB
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

    //Para actualizar se manda "current.mealId" (la fk)
    fun updateEntry(id:Int, grams: String, section: MealType) {
        viewModelScope.launch {
            try {

                val current = entries.find { it.id == id } ?: return@launch //Return@launch sale de la corutina, si no existe la ID, lo corta. Si es nulo, corta el lambda
                entryRepo.updateEntry(id, DiaryEntryRequest(current.mealId, grams, section))
                //Se manda a llamar de nuevo para mantenerse siempre actualizado con la DB
                loadEntries()
                _events.send("Entry updated")
            } catch (ex: Exception) {
                println("Fallo al editar el entry: ${ex.message}")
                _events.send("Error updating the entry")
            }
        }
    }
}