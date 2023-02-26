package com.example.my_movie_app

import androidx.lifecycle.MutableLiveData

interface IBaseViewModel<T> {
    fun onGetData(): MutableLiveData<T>
    fun onGetErrorMessage(): MutableLiveData<String>
    fun onLoadData()
}