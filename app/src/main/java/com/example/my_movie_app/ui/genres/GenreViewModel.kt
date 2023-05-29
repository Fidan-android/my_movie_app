package com.example.my_movie_app.ui.genres

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.my_movie_app.IBaseViewModel
import com.example.my_movie_app.api.ApiHelper
import com.example.my_movie_app.api.models.FilterResponse
import com.example.my_movie_app.api.models.GenreModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class GenreViewModel : ViewModel(), IBaseViewModel<MutableList<GenreModel>> {

    init {
        onLoadData()
    }

    private val cinemasLiveData: MutableLiveData<MutableList<GenreModel>> = MutableLiveData()
    private val isErrorLiveData: MutableLiveData<String> = MutableLiveData()

    override fun onGetData() = cinemasLiveData
    override fun onGetErrorMessage() = isErrorLiveData

    override fun onLoadData() {
        MainScope().launch(Dispatchers.IO) {
            try {
                val response = ApiHelper.getGenres().execute()
                if (response.isSuccessful) {
                    val body = response.body() as FilterResponse
                    body.genres.let {
                        cinemasLiveData.postValue(it)
                    }
                } else {
                    response.message().let {
                        isErrorLiveData.postValue(it)
                    }
                }
            } catch (e: Exception) {
                isErrorLiveData.postValue(e.message)
            }
        }
    }
}