package com.example.my_movie_app.ui.films

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.my_movie_app.IBaseViewModel
import com.example.my_movie_app.api.ApiHelper
import com.example.my_movie_app.api.models.FilmsModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class FilmsViewModel : ViewModel(), IBaseViewModel<MutableList<FilmsModel>> {

    init {
        onLoadData()
    }

    private val filmsLiveData: MutableLiveData<MutableList<FilmsModel>> = MutableLiveData()
    private val isErrorLiveData: MutableLiveData<String> = MutableLiveData()

    override fun onGetData() = filmsLiveData
    override fun onGetErrorMessage() = isErrorLiveData

    override fun onLoadData() {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val response = ApiHelper.getFilms()
                if (response.message == null) {
                    response.films.let {
                        filmsLiveData.postValue(it)
                    }
                } else {
                    response.message.let {
                        isErrorLiveData.postValue(it)
                    }
                }
            } catch (e: Exception) {
                isErrorLiveData.postValue(e.message)
            }
        }
    }
}