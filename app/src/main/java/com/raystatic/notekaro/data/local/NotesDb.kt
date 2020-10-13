package com.raystatic.notekaro.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.raystatic.notekaro.data.local.auth.User
import com.raystatic.notekaro.data.local.auth.UserDao

@Database(
    entities = [User::class],
    version = 1
)
abstract class NotesDb : RoomDatabase(){

    abstract fun getUserDao():UserDao

}