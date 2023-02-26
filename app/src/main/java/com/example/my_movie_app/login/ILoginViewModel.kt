package com.example.my_movie_app.login

import androidx.lifecycle.MutableLiveData
import com.example.my_movie_app.IBaseViewModel

interface ILoginViewModel<T> : IBaseViewModel<T> {
    fun onSaveLoginString(newLogin: String?)
    fun onSavePasswordString(newPassword: String?)
    fun onProgressBar(): MutableLiveData<Boolean>
}