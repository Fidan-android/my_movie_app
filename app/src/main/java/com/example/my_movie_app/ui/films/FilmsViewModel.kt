package com.example.my_movie_app.ui.films

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.my_movie_app.IBaseViewModel
import com.example.my_movie_app.api.ApiHelper
import com.example.my_movie_app.api.models.FilmModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class FilmsViewModel : ViewModel(), IBaseViewModel<MutableList<FilmModel>> {

    init {
        onLoadData()
    }

    private val filmsLiveData: MutableLiveData<MutableList<FilmModel>> = MutableLiveData()
    private val isErrorLiveData: MutableLiveData<String> = MutableLiveData()

    override fun onGetData() = filmsLiveData
    override fun onGetErrorMessage() = isErrorLiveData

    override fun onLoadData() {
        MainScope().launch(Dispatchers.IO) {
            try {
                val response = ApiHelper.getFilms().execute()

                if (response.isSuccessful) {
                    response.body()!!.films.let {
                        filmsLiveData.postValue(it)
                    }
                } else {
                    when (response.code()) {
                        401 -> isErrorLiveData.postValue("Проблема с api-key")
                    }
                }
            } catch (e: Exception) {
                isErrorLiveData.postValue(e.message)
            }
        }
    }
}