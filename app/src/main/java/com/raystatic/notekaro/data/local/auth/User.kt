package com.raystatic.notekaro.data.local.auth

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class User(

    @ColumnInfo(name = "__v")
    val __v: Int?=0,

    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "_id")
    val _id: String,

    @ColumnInfo(name = "avatar")
    val avatar: String?="",

    @ColumnInfo(name = "createdAt")
    val createdAt: String?="",

    @ColumnInfo(name = "email")
    val email: String?="",

    @ColumnInfo(name = "name")
    val name: String?="",

    @ColumnInfo(name = "updatedAt")
    val updatedAt: String?=""
)