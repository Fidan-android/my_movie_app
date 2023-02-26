package com.example.my_movie_app.ui.map

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.my_movie_app.IBaseViewModel
import com.example.my_movie_app.api.ApiHelper
import com.example.my_movie_app.api.models.CinemaModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MapViewModel : ViewModel(), IBaseViewModel<MutableList<CinemaModel>> {

    init {
        onLoadData()
    }

    private val cinemasLiveData: MutableLiveData<MutableList<CinemaModel>> = MutableLiveData()
    private val isErrorLiveData: MutableLiveData<String> = MutableLiveData()

    override fun onGetData() = cinemasLiveData
    override fun onGetErrorMessage() = isErrorLiveData

    override fun onLoadData() {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val response = ApiHelper.getCinema()
                if (response.message == null) {
                    response.cinemas.let {
                        cinemasLiveData.postValue(it)
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