package com.raystatic.notekaro.data.requests

data class UpdateNoteRequest(
    val title:String,
    val text:String,
    val color:String,
    val noteId:String
)