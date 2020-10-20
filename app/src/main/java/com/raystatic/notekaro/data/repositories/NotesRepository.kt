package com.raystatic.notekaro.data.repositories

import com.raystatic.notekaro.data.local.notes.Note
import com.raystatic.notekaro.data.local.notes.NotesDao
import com.raystatic.notekaro.data.remote.ApiHelper
import com.raystatic.notekaro.data.requests.CreateNoteRequest
import com.raystatic.notekaro.data.requests.DeleteNoteRequest
import com.raystatic.notekaro.data.requests.UpdateNoteRequest
import timber.log.Timber
import javax.inject.Inject

class NotesRepository @Inject constructor(
    private val apiHelper: ApiHelper,
    private val notesDao: NotesDao
){

    // remote
    suspend fun getAllNotes(token:String) = apiHelper.getAllNotes(token)

    suspend fun createNewNote(token: String, createNoteRequest: CreateNoteRequest) = apiHelper.createNewNote(token, createNoteRequest)

    suspend fun updateExistingNote(token: String, updateNoteRequest: UpdateNoteRequest) = apiHelper.updateExistingNote(token, updateNoteRequest)

    suspend fun deleteNote(token: String, deleteNoteRequest: DeleteNoteRequest) = apiHelper.deleteNote(token, deleteNoteRequest)


    //local
    suspend fun insertNote(note:Note) {
        Timber.d("notesdao $notesDao")
        notesDao.insertNote(note)
    }

    suspend fun updateNote(note: Note) = notesDao.updateNote(note)

    suspend fun deleteAllNotes() = notesDao.deleteAllNote()

    suspend fun deleteNoteById(id:String) = notesDao.deleteNoteById(id)

    fun getAllNotes() = notesDao.getAllNotes()

}