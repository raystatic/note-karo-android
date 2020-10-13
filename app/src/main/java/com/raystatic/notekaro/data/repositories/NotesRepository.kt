package com.raystatic.notekaro.data.repositories

import com.raystatic.notekaro.data.remote.ApiHelper
import javax.inject.Inject

class NotesRepository @Inject constructor(
    private val apiHelper: ApiHelper
){

    suspend fun getAllNotes(token:String) = apiHelper.getAllNotes(token)

}