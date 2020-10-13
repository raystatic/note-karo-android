package com.raystatic.notekaro.data.remote

import com.raystatic.notekaro.data.requests.AuthRequest
import com.raystatic.notekaro.data.responses.AllNotesResponse
import com.raystatic.notekaro.data.responses.AuthResponse
import retrofit2.Response

interface ApiHelper {

    suspend fun authenticateUser(authRequest: AuthRequest):Response<AuthResponse>

    suspend fun getAllNotes(token:String):Response<AllNotesResponse>

}