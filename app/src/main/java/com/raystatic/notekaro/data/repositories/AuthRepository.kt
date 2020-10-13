package com.raystatic.notekaro.data.repositories

import com.raystatic.notekaro.data.local.auth.User
import com.raystatic.notekaro.data.local.auth.UserDao
import com.raystatic.notekaro.data.remote.ApiHelper
import com.raystatic.notekaro.data.requests.AuthRequest
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val apiHelper:ApiHelper,
    private val userDao: UserDao
){

    //Remote
    suspend fun authenticateUser(authRequest: AuthRequest) = apiHelper.authenticateUser(authRequest)



    //Local
    suspend fun insertUser(user: User) = userDao.insertUser(user)

    fun getUser() = userDao.getUser()

}