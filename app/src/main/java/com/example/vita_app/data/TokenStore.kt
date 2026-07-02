package com.example.vita_app.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

//Se crea un nuevo Context.dataStore con el nombre de "auth_prefs", authPrefs es un archivo de almacenamiento dentro de la app, cada name es un archivo distinto
//Context es un objeto de la app en ejecucion, es una objeto global de entorno (como window)
//Se usa con datastore para guardar en disco, ya que solo Context sabe el lugar del disco
private val Context.dataStore by preferencesDataStore(name = "auth_prefs")

class TokenStore(private val context: Context) {
    //Se crea una llave de datastore con el nombre de "token"
    private val tokenKey = stringPreferencesKey("token")

    //Funciones del dataStore

    //Guarda en disco un tokenKey
    suspend fun save(token: String) {
        //Context.dataStore abre todas las llaves almacenadas en el DataStore, como solo hay una, a esa se le asigna el tokenKey (una entrada nueva)
        context.dataStore.edit { prefs ->
            prefs[tokenKey] = token //La llave se llama token
        }
    }

    //Lee el valor actual una vez. .map saca la clave, .first() toma el
    // valor actual y termina
    suspend fun read(): String? {
        //Busca en el cajon de context.datastore(auth prefs) y busca la primera llave donde saca solo el tokenKey. El resultado es el token guardado o null si no hay
        return context.dataStore.data.map { prefs ->
            prefs[tokenKey] }.first()
    }

    //Quita la clave del archivo al hacer logout.
    suspend fun clear() {
        context.dataStore.edit { prefs ->
            prefs.remove(tokenKey)
        }
    }


}