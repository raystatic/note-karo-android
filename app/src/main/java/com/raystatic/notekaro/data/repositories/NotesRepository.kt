package com.raystatic.notekaro.data.repositories

import com.raystatic.notekaro.data.local.notes.Note
import com.raystatic.notekaro.data.local.notes.NotesDao
import com.raystatic.notekaro.data.remote.ApiHelper
import com.raystatic.notekaro.data.requests.CreateNoteRequest
import javax.inject.Inject

class NotesRepository @Inject constructor(
    private val apiHelper: ApiHelper,
    private val notesDao: NotesDao
){

    // remote
    suspend fun getAllNotes(token:String) = apiHelper.getAllNotes(token)

    suspend fun createNewNote(token: String, createNoteRequest: CreateNoteRequest) = apiHelper.createNewNote(token, createNoteRequest)


    //local
    suspend fun insertNote(note:Note) = notesDao.insertNote(note)

    suspend fun deleteAllNotes() = notesDao.deleteAllNote()

    suspend fun deleteNoteById(id:String) = notesDao.deleteNoteById(id)

    fun getAllNotes() = notesDao.getAllNotes()

}