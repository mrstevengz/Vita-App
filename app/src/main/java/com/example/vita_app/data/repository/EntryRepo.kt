package com.example.vita_app.data.repository

import com.example.vita_app.data.RetrofitHelper
import com.example.vita_app.data.TokenManager
import com.example.vita_app.data.remote.api.EntriesApi
import com.example.vita_app.data.remote.model.DiaryEntryRequest
import com.example.vita_app.data.remote.model.DiaryEntryResponse
import com.example.vita_app.ui.navigation.Diary

class EntryRepo {
    private val api = RetrofitHelper.getInstance().create(EntriesApi::class.java)

    private fun bearer(): String = "Bearer ${TokenManager.token}"

    //Funciones REST

    suspend fun getEntries(): List<DiaryEntryResponse> = api.getEntries(bearer())

    suspend fun createEntry(body: DiaryEntryRequest): DiaryEntryResponse = api.createEntry(body, bearer())

    suspend fun updateEntry(id: Int, body: DiaryEntryRequest): DiaryEntryResponse = api.updateEntry(id, body, bearer())

    suspend fun deleteEntry(id: Int) {
        val res = api.deleteEntry(id, bearer())
        if (!res.isSuccessful) throw Exception("Fallo al eliminar: ${res.code()}")
    }
}