package com.raystatic.notekaro.data.requests

data class CreateNoteRequest(
    val title:String,
    val text:String,
    val color:String
)