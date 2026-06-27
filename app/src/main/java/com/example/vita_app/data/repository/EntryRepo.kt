package com.example.vita_app.data.repository

import com.example.vita_app.data.RetrofitHelper
import com.example.vita_app.data.remote.api.EntriesApi
import com.example.vita_app.data.remote.model.DiaryEntry
import com.example.vita_app.data.remote.model.DiaryEntryRequest

class EntryRepo {
    private val api = RetrofitHelper.getInstance().create(EntriesApi::class.java)

    //Funciones REST

    suspend fun getEntries(): List<DiaryEntry> = api.getEntries()

    suspend fun createEntry(body: DiaryEntryRequest): DiaryEntry = api.createEntry(body)

    suspend fun updateEntry(id: Int, body: DiaryEntryRequest): DiaryEntry = api.updateEntry(id, body)

    suspend fun deleteEntry(id: Int) {
        val res = api.deleteEntry(id)
        if (!res.isSuccessful) throw Exception("Fallo al eliminar: ${res.code()}")
    }
}