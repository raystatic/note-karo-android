package com.raystatic.notekaro.data.responses

import com.raystatic.notekaro.data.local.notes.Note

data class AllNotesResponse(
    var _notes: List<Note>?=null,
    val success: Boolean,
    var message:String?=""
)