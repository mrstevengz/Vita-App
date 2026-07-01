package com.example.vita_app.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


private val Context.settingsDataStore by preferencesDataStore(name = "settings")

class GoalStore(private val context: Context) {
    private val goalKey = intPreferencesKey("goal")

    val goalFlow: Flow<Int> = context.settingsDataStore.data.map { prefs -> prefs[goalKey] ?: 2000}

    suspend fun setGoal(goal:Int) {
        context.settingsDataStore.edit {prefs -> prefs[goalKey] = goal}
    }
}