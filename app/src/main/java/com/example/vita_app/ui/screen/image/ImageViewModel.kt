package com.example.vita_app.ui.screen.image

import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.vita_app.data.remote.model.DetectedFood
import com.example.vita_app.data.repository.ImageRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream

//Recibe una application y usa AndrodiViewModel (Context) para acceder al Uri de la foto en contentResolver
class ImageViewModel(application: Application) : AndroidViewModel(application) {
    private val repo = ImageRepo()

    var isLoading by mutableStateOf(false) //Para el spinner
        private set
    var results by mutableStateOf<List<DetectedFood>>(emptyList()) //Resultados del escaneo
        private set

    //Errores -> Toast
    private val _events = Channel<String>()
    val events = _events.receiveAsFlow()




    //Recibe un parametro UrI, que es la direccion de la foto.
    fun analyze(uri: Uri) {
        viewModelScope.launch { //Arranca la corutina vinculada al viewmodel
            isLoading = true //Spinner
            try {
                // Bytes guarda los ByteArray (bytes del JPEG)
                //withContext(Dispatchers.IO) ejecuta el bloque en un hilo de fondo, y devuelve el resultado.
                //Se utiliza porque comprimir una imagen es pesado, y no se quiere hacer en el hilo de la UI.
                //UriToJpeg llama a la funcion auxiliar con dos argumentos, getApplication (para el Context) y la direccion uri
                val bytes = withContext(Dispatchers.IO) {uriToJpeg(getApplication(), uri) }

                //Se guarda la imagen en bytes y luego se manda al backend y espera la respuesta. La corrutina se pausa aqui hasta terminar
                val res = repo.analyze(bytes)
                //Res tiene un campo results, se copia esa lista en una variable
                results = res.results
            } catch (ex: Exception) {
                println("Fallo al analizar la imagen: ${ex.message}")
            } finally {
                isLoading = false //Se quita el spinner
            }
        }
    }

    //Funcion privada para este viewmodel, recibe el context para abrir el uri, y una direccion. Devuelve un
    // bytearray (datos byte de la image)
    private fun uriToJpeg(context: Context, uri: Uri): ByteArray {
        //Esta funcion abre un grifo (InputStream) para leer los bytes del Uri.
        //Si el grifo no es NULL, lo usa y decodifica la foto a Bitmap (imagen ya descomprimida en memoria)
        //input es el nombre que se le da al InputStream dentro del bloque.

        val bitmap = context.contentResolver.openInputStream(uri)?.use { input -> BitmapFactory.decodeStream(input) } ?: throw Exception("No se pudo decodificar la imagen")
        //Crea un ByteArrayOutputStream, equivale a un balde en memoria que crece a medida que recibe bytes. Recibe los bytes del JPEG

        val output = ByteArrayOutputStream()

        //Toma el bitmap (pixeles crudos) y lo comprime a JPEG, escribiendo los bytes resultantes en output
        bitmap.compress(Bitmap.CompressFormat.JPEG, 85, output)

        //Entra un bitmap grande (alta calidad) y sale un JPEG razonable, convierte el contenido en un ByteArray normal y lo duelve
        return output.toByteArray()
    }

    fun clear() {
        results = emptyList() //Vacia los resultados
    }
    fun removeResult(food: DetectedFood) {
        results = results.filter {it !== food} //Quita un resultado de la lista
    }
}