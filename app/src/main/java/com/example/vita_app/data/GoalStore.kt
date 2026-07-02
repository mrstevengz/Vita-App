package com.example.vita_app.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


//Archivo de preferencias separado del auth, es nuevo
private val Context.settingsDataStore by preferencesDataStore(name = "settings")


class GoalStore(private val context: Context) {
    //Clave para la meta de calorias
    private val goalKey = intPreferencesKey("goal")

    //Flow de tipo Int es literalmente un grifo, cada vez que se cambia la meta este emite un numero, y esta constantemente pendiente de cualquier actualizacion para emitirla
    //Map a prefs -> saca la meta, y si no hay usa el valor por defecto de 2000
    val goalFlow: Flow<Int> = context.settingsDataStore.data.map { prefs -> prefs[goalKey] ?: 2000}

    //Guardar la nueva meta en el datastore
    suspend fun setGoal(goal:Int) {
        context.settingsDataStore.edit {prefs -> prefs[goalKey] = goal}
    }
}