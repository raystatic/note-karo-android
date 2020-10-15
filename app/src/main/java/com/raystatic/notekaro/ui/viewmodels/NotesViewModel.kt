package com.raystatic.notekaro.ui.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.raystatic.notekaro.data.local.notes.Note
import com.raystatic.notekaro.data.repositories.NotesRepository
import com.raystatic.notekaro.data.requests.CreateNoteRequest
import com.raystatic.notekaro.data.requests.UpdateNoteRequest
import com.raystatic.notekaro.data.responses.AllNotesResponse
import com.raystatic.notekaro.other.Constants
import com.raystatic.notekaro.other.NetworkHelper
import com.raystatic.notekaro.other.Resource
import kotlinx.coroutines.launch

class NotesViewModel @ViewModelInject constructor(
    private val networkHelper: NetworkHelper,
    private val notesRepository: NotesRepository
):ViewModel(){

    private val _notes = MutableLiveData<Resource<AllNotesResponse>>()
    private val _createNote = MutableLiveData<Resource<AllNotesResponse>>()
    private val _updateNoteResponse =  MutableLiveData<Resource<AllNotesResponse>>()
    private val _currentNotes = MutableLiveData<List<Note>>()

    val notes: LiveData<Resource<AllNotesResponse>>
        get() = _notes

    val createdNote:LiveData<Resource<AllNotesResponse>>
        get() = _createNote

    val updateNoteResponse:LiveData<Resource<AllNotesResponse>>
        get() = _updateNoteResponse

    val currentNotes = notesRepository.getAllNotes()


    fun updateExistingNote(token: String, updateNoteRequest: UpdateNoteRequest) = viewModelScope.launch {
        _updateNoteResponse.postValue(Resource.loading(null))

        if (networkHelper.isNetworkConnected()){
            notesRepository.updateExistingNote(token, updateNoteRequest).let {
                if (it.isSuccessful){
                    _updateNoteResponse.postValue(Resource.success(it.body()))
                }else{
                    _updateNoteResponse.postValue(Resource.error(Constants.SOMETHING_WENT_WRONG, null))
                }
            }
        }else{
            _updateNoteResponse.postValue(Resource.error(Constants.NO_INTERNET_CONNECTION, null))
        }
    }

    fun addNoteToLocal(note:Note) = viewModelScope.launch {
        notesRepository.insertNote(note)
    }

    fun updateNoteToLocal(note: Note) = viewModelScope.launch {
        notesRepository.updateNote(note)
    }

    fun deleteNoteByIdFromLocal(id:String) = viewModelScope.launch {
        notesRepository.deleteNoteById(id)
    }

    fun deleteAllNotesFromLocal() = viewModelScope.launch {
        notesRepository.deleteAllNotes()
    }

    fun createNewNote(token: String, createNoteRequest: CreateNoteRequest) = viewModelScope.launch {
        _createNote.postValue(Resource.loading(null))
        if (networkHelper.isNetworkConnected()){

            notesRepository.createNewNote(token,createNoteRequest).let {
                if (it.isSuccessful){
                    _createNote.postValue(Resource.success(it.body()))
                }else{
                    _createNote.postValue(Resource.error(Constants.SOMETHING_WENT_WRONG, null))
                }
            }

        }else{
            _createNote.postValue(Resource.error(Constants.NO_INTERNET_CONNECTION, null))
        }
    }

    fun getAllNotes(token:String) = viewModelScope.launch {
        _notes.postValue(Resource.loading(null))
        if (networkHelper.isNetworkConnected()){

            notesRepository.getAllNotes(token).let {
                if (it.isSuccessful){
                    _notes.postValue(Resource.success(it.body()))
                }else{
                    _notes.postValue(Resource.error(Constants.SOMETHING_WENT_WRONG, null))
                }
            }

        }else{
            _notes.postValue(Resource.error(Constants.NO_INTERNET_CONNECTION, null))
        }

    }


}