package com.raystatic.notekaro.data.local.notes

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "note")
data class Note(

    @ColumnInfo(name = "__v")
    val __v: Int?=0,

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "_id")
    val _id: String,

    @ColumnInfo(name = "author")
    val author: String?="",

    @ColumnInfo(name = "color")
    val color: String?="",

    @ColumnInfo(name = "createdAt")
    val createdAt: String?="",

    @ColumnInfo(name = "text")
    val text: String?="",

    @ColumnInfo(name = "title")
    val title: String?="",

    @ColumnInfo(name = "updatedAt")
    val updatedAt: String?=""
)