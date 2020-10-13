package com.raystatic.notekaro.ui.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.raystatic.notekaro.data.local.auth.User
import com.raystatic.notekaro.data.repositories.AuthRepository
import com.raystatic.notekaro.data.requests.AuthRequest
import com.raystatic.notekaro.data.responses.AuthResponse
import com.raystatic.notekaro.other.Constants.NO_INTERNET_CONNECTION
import com.raystatic.notekaro.other.Constants.SOMETHING_WENT_WRONG
import com.raystatic.notekaro.other.NetworkHelper
import com.raystatic.notekaro.other.Resource
import kotlinx.coroutines.launch

class AuthViewModel @ViewModelInject constructor(
    private val authRepository: AuthRepository,
    private val networkHelper: NetworkHelper
):ViewModel(){

    private val _authResponse = MutableLiveData<Resource<AuthResponse>>()


    val authResponse: LiveData<Resource<AuthResponse>>
        get() = _authResponse

    fun insertUser(user: User) = viewModelScope.launch {
        authRepository.insertUser(user)
    }

    fun getUser():LiveData<User> = authRepository.getUser()

    fun authenticateUser(authRequest: AuthRequest) = viewModelScope.launch {
        _authResponse.postValue(Resource.loading(null))

        if (networkHelper.isNetworkConnected()){
            authRepository.authenticateUser(authRequest).let {
                if (it.isSuccessful){
                    _authResponse.postValue(Resource.success(it.body()))
                }else{
                    _authResponse.postValue(Resource.error(SOMETHING_WENT_WRONG, null))
                }
            }
        }else{
            _authResponse.postValue(Resource.error(NO_INTERNET_CONNECTION, null))
        }

    }

}