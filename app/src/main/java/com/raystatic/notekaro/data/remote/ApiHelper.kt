package com.raystatic.notekaro.data.remote

import com.raystatic.notekaro.data.requests.AuthRequest
import com.raystatic.notekaro.data.requests.CreateNoteRequest
import com.raystatic.notekaro.data.requests.DeleteNoteRequest
import com.raystatic.notekaro.data.requests.UpdateNoteRequest
import com.raystatic.notekaro.data.responses.AllNotesResponse
import com.raystatic.notekaro.data.responses.AuthResponse
import retrofit2.Response

interface ApiHelper {

    suspend fun authenticateUser(authRequest: AuthRequest):Response<AuthResponse>

    suspend fun getAllNotes(token:String):Response<AllNotesResponse>

    suspend fun createNewNote(token:String, createNoteRequest: CreateNoteRequest):Response<AllNotesResponse>

    suspend fun updateExistingNote(token: String, updateNoteRequest: UpdateNoteRequest):Response<AllNotesResponse>

    suspend fun deleteNote(token: String, deleteNoteRequest: DeleteNoteRequest):Response<AllNotesResponse>

}