package com.example.vita_app.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "auth_prefs")

class TokenStore(private val context: Context) {
    private val tokenKey = stringPreferencesKey("token")

    //Funciones del dataStore

    //Guarda en disco un tokenKey
    suspend fun save(token: String) {
        context.dataStore.edit { prefs ->
            prefs[tokenKey] = token
        }
    }

    //Lee el valor actual una vez. .mao saca la clave, .first() toma el
    // valor actual y termina
    suspend fun read(): String? {
        return context.dataStore.data.map { prefs ->
            prefs[tokenKey] }.first()
    }

    suspend fun clear() {
        context.dataStore.edit { prefs ->
            prefs.remove(tokenKey)
        }
    }


}