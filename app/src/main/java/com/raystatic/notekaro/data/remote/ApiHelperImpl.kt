package com.raystatic.notekaro.data.remote

import com.raystatic.notekaro.data.requests.AuthRequest
import com.raystatic.notekaro.data.responses.AuthResponse
import retrofit2.Response
import javax.inject.Inject

class ApiHelperImpl @Inject constructor(
    private val apiService: ApiService
):ApiHelper{

    override suspend fun authenticateUser(authRequest: AuthRequest): Response<AuthResponse>  = apiService.authenticateUser(authRequest)
}