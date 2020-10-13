package com.raystatic.notekaro.ui.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.raystatic.notekaro.data.local.notes.Note
import com.raystatic.notekaro.data.repositories.NotesRepository
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

    val notes: LiveData<Resource<AllNotesResponse>>
        get() = _notes

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