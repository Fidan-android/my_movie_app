package com.example.my_movie_app.ui.genres.filter

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.my_movie_app.api.ApiHelper
import com.example.my_movie_app.api.models.FilmModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import java.util.*

class FilteredFilmsViewModel : ViewModel(), IFilteredFilmsViewModel<MutableList<FilmModel>> {

    private var countTotal = 0
    private var isLoaded = true
    private var genreId = 0
    private var currentPage = 1

    private var filmsLiveData: MutableLiveData<MutableList<FilmModel>> =
        MutableLiveData<MutableList<FilmModel>>()
    private val isErrorLiveData: MutableLiveData<String> = MutableLiveData()

    override fun onGetData() = filmsLiveData
    override fun onIsLoaded() = isLoaded
    override fun onGetTotalCount() = countTotal

    override fun onGetErrorMessage() = isErrorLiveData


    override fun onSetGenreId(genreId: Int) {
        this.genreId = genreId
        onLoadData()
    }

    @SuppressLint("SimpleDateFormat")
    override fun onLoadData() {
        isLoaded = false
        if (currentPage != -1) {
            MainScope().launch(Dispatchers.IO) {
                try {
                    val response = ApiHelper.getFilmsByGenre(genreId, currentPage).execute()

                    isLoaded = true
                    if (response.isSuccessful) {
                        countTotal += response.body()?.total ?: 0
                        currentPage = if ((response.body()?.totalPages
                                ?: 0) > currentPage
                        ) currentPage + 1 else -1
                        Log.d("current page", currentPage.toString())
                        response.body()!!.films.let {
                            val list = filmsLiveData.value ?: mutableListOf()
                            list.addAll(it)
                            filmsLiveData.postValue(list)
                        }
                    } else {
                        when (response.code()) {
                            401 -> isErrorLiveData.postValue("Проблема с api-key")
                        }
                    }
                } catch (e: Exception) {
                    Log.e("error", e.stackTraceToString())
                    isErrorLiveData.postValue(e.message)
                }
            }
        }
    }
}