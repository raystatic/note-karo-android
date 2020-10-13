package com.raystatic.notekaro.data.local.auth

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    @Query("DELETE from user")
    suspend fun deleteUser()

    @Query("SELECT * from user")
    fun getUser(): LiveData<User>

}