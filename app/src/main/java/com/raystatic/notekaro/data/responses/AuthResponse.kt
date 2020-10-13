package com.raystatic.notekaro.data.responses

import com.raystatic.notekaro.data.local.auth.User

data class AuthResponse(
    val _token: String?="",
    val _user: User?=null,
    val error:String?=""
)