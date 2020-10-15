package com.raystatic.notekaro.data.remote

import com.raystatic.notekaro.data.requests.AuthRequest
import com.raystatic.notekaro.data.requests.CreateNoteRequest
import com.raystatic.notekaro.data.requests.UpdateNoteRequest
import com.raystatic.notekaro.data.responses.AllNotesResponse
import com.raystatic.notekaro.data.responses.AuthResponse
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @POST("auth")
    suspend fun authenticateUser(@Body authRequest: AuthRequest):Response<AuthResponse>

    @GET("notes")
    suspend fun getAllNotes(@Header("auth-token") token:String):Response<AllNotesResponse>

    @POST("notes")
    suspend fun createNewNote(@Header("auth-token") token:String,
                                @Body createNoteRequest: CreateNoteRequest):Response<AllNotesResponse>

    @PATCH("notes")
    suspend fun updateExistingNote(@Header("auth-token") token: String,
                                    @Body updateNoteRequest: UpdateNoteRequest):Response<AllNotesResponse>

}