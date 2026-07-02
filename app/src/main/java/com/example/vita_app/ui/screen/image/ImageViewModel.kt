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

class ImageViewModel(application: Application) : AndroidViewModel(application) {
    private val repo = ImageRepo()

    var isLoading by mutableStateOf(false)
        private set
    var results by mutableStateOf<List<DetectedFood>>(emptyList())
        private set

    //Errores
    private val _events = Channel<String>()
    val events = _events.receiveAsFlow()

    fun analyze(uri: Uri) {
        viewModelScope.launch {
            isLoading = true
            try {
                val bytes = withContext(Dispatchers.IO) {uriToJpeg(getApplication(), uri) }

                val res = repo.analyze(bytes)
                results = res.results
            } catch (ex: Exception) {
                println("Fallo al analizar la imagen: ${ex.message}")
            } finally {
                isLoading = false
            }
        }
    }

    private fun uriToJpeg(context: Context, uri: Uri): ByteArray {
        val bitmap = context.contentResolver.openInputStream(uri)?.use { input -> BitmapFactory.decodeStream(input) } ?: throw Exception("No se pudo decodificar la imagen")
        val output = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 85, output)

        return output.toByteArray()
    }

    fun clear() {
        results = emptyList()
    }
    fun removeResult(food: DetectedFood) {
        results = results.filter {it !== food}
    }
}