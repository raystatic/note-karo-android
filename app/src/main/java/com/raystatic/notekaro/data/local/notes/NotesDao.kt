package com.raystatic.notekaro.data.local.notes

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface NotesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: Note)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateNote(n: Note)

    @Query("DELETE from note")
    suspend fun deleteAllNote()

    @Query("DELETE from note WHERE _id=:id")
    suspend fun deleteNoteById(id:String)

    @Query("SELECT * from note ORDER BY updatedAt DESC")
    fun getAllNotes(): LiveData<List<Note>>

}