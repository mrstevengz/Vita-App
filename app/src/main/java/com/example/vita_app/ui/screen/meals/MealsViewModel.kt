package com.example.vita_app.ui.screen.meals

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vita_app.data.remote.model.DiaryEntry
import com.example.vita_app.data.remote.model.Meal
import com.example.vita_app.data.remote.model.MealType
import com.example.vita_app.data.repository.EntryRepo
import com.example.vita_app.data.repository.MealRepo
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class MealsViewModel: ViewModel() {
    //Se inicializa una instancia del repositorio del api, y un array de Meal's para guardarlo en
    // las pantallas y mostrarlo
    private val mealRepo = MealRepo()
    private val entryRepo = EntryRepo()

    val meals = mutableStateListOf<Meal>()
    val entries = mutableStateListOf<DiaryEntry>()
    private val _events = Channel<String>()
    val events = _events.receiveAsFlow()

    //Cuando se inicializa la clase, siempre va a cargar la lista de Meals del API
    init {
        loadMeals()
        loadEntries()
    }

    //Funcion para cargar los meals existentes.
    fun loadMeals() {
        viewModelScope.launch {
            //Try Catch para excepciones al cargar
            try {
                //Manda a llamar la funcion de getMeals al API, y guarda la lista en la lista anterior
                val mealsApi = mealRepo.getMeals()
                //Se limpia la lista antes de llenarla con la informacion del API.
                //En caso de duplicados, se agregaria informacion encima de estos repetida, por eso se limpia
                meals.clear()
                meals.addAll(mealsApi)
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

    fun addMeal(name: String, calories: Double, section: MealType) {
        viewModelScope.launch {
            try {
                //Se manda a llamar el metodo de createmeal para el API, solamente se manda
                //el nombre, calorias y la seccion que pertenece

                /*IDEAS FUTURAS => La seccion que se mande debe ser la seccion de ADD MEAL que se cliquee
                * en la interfaz. Si se cliquea el boton de ADD MEAL en Breakfast, la seccion automaticamente
                * debe ser breakfast.
                *
                * Se tiene que agregar en el UI opcion para enviar fat grams, protein grams y
                * carbs. Falta el campo de carbs, y direcciones no tiene mucho sentido para una comida
                * en especifico. Diria que en caso */
                val meal = repo.createMeal(
                    Meal(name = name, calories = calories, section = section)
                )
                meals.add(meal)

                //Se manda a llamar de nuevo para mantenerse siempre actualizado con la DB
                //Como solo se agrega una comida a la vez, no considero que sea TAN
                // wasteful hacer un HTTP request extra
                //todo: Cambiar esto y buscar una mejor alternativa, talvez guardarlo localmente primero?
                loadMeals()
                _events.send("Meal added")
            } catch(e: Exception) {
                println("Fallo al agregar meal: ${e.message}")
                _events.send("Error saving the meal")
            }
        }
    }

    fun deleteMeal(id : Int) {
        //Se manda a llamar el metodo de deleteMeal del repositorio, se le pasa el ID y borra todos los meals que
        // coincidan con el ID (solo puede ser 1 al ser unico). Esto se hace con el fin de agilizar la actualizacion del
        //UI para el usuario.
        /*todo BUSCAR UNA MANERA DE OPTIMIZAR ESTE METODO: UNA OPCION ES BORRAR LOCALMENTE EL ROW PARA EL USUARIO, Y LUEGO
        *  BORRAR EL REQUEST, EN CASO DE QUE FALLE SE PODRIA VOLVER A PONER EL ROW EN EL UI. */
        viewModelScope.launch {
            try {
                repo.deleteMeal(id)
                meals.removeAll {it.id == id}
                _events.send("Meal deleted")
            } catch (ex: Exception) { // restore on failure
                println("Fallo al eliminar meal: ${ex.message}")
                _events.send("Error deleting the meal")
            }
        }
    }

    fun updateMeal(id:Int, updated: Meal) {
        viewModelScope.launch {
            try {
                //Se g
                val result = repo.updateMeal(id, updated)
                val idx = meals.indexOfFirst { it.id == id }
                if (idx != -1) meals[idx] = result
                _events.send("Meal updated")
            } catch (ex: Exception) {
                println("Fallo al editar meal: ${ex.message}")
                _events.send("Error updating the meal")
            }
        }
    }
}