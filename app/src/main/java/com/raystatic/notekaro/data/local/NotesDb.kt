package com.raystatic.notekaro.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.raystatic.notekaro.data.local.auth.User
import com.raystatic.notekaro.data.local.auth.UserDao
import com.raystatic.notekaro.data.local.notes.Note
import com.raystatic.notekaro.data.local.notes.NotesDao

@Database(
    entities = [User::class, Note::class],
    version = 1
)
abstract class NotesDb : RoomDatabase(){

    abstract fun getUserDao():UserDao

    abstract fun getNotesDao():NotesDao

}