package com.raystatic.notekaro.data.remote

import com.raystatic.notekaro.data.requests.AuthRequest
import com.raystatic.notekaro.data.requests.CreateNoteRequest
import com.raystatic.notekaro.data.requests.UpdateNoteRequest
import com.raystatic.notekaro.data.responses.AllNotesResponse
import com.raystatic.notekaro.data.responses.AuthResponse
import retrofit2.Response
import javax.inject.Inject

class ApiHelperImpl @Inject constructor(
    private val apiService: ApiService
):ApiHelper{

    override suspend fun authenticateUser(authRequest: AuthRequest): Response<AuthResponse>  = apiService.authenticateUser(authRequest)

    override suspend fun getAllNotes(token: String): Response<AllNotesResponse>  = apiService.getAllNotes(token)

    override suspend fun createNewNote(token: String, createNoteRequest: CreateNoteRequest): Response<AllNotesResponse>  = apiService.createNewNote(token, createNoteRequest)

    override suspend fun updateExistingNote(
        token: String,
        updateNoteRequest: UpdateNoteRequest
    ): Response<AllNotesResponse>  = apiService.updateExistingNote(token, updateNoteRequest)
}