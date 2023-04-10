package com.example.my_movie_app.ui.films

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.my_movie_app.api.ApiHelper
import com.example.my_movie_app.api.models.FilmModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class FilmsViewModel : ViewModel(), IFilmsViewModel<MutableList<FilmModel>> {

    private var minusMonth = 0
    private var countTotal = 0
    private var isLoaded = true
    private var isUpdate = false

    init {
        onLoadData()
    }

    private var cashesFilms: MutableList<FilmModel> = mutableListOf()
    private var filmsLiveData: MutableLiveData<MutableList<FilmModel>> =
        MutableLiveData<MutableList<FilmModel>>()
    private val isErrorLiveData: MutableLiveData<String> = MutableLiveData()

    override fun onGetData() = filmsLiveData
    override fun onIsLoaded() = isLoaded
    override fun onIsUpdate() = isUpdate
    override fun onGetTotalCount() = countTotal

    override fun onGetErrorMessage() = isErrorLiveData

    @SuppressLint("SimpleDateFormat")
    override fun onLoadData() {
        isLoaded = false
        MainScope().launch(Dispatchers.IO) {
            try {
                val calendar = Calendar.getInstance()
                calendar.add(Calendar.MONTH, -minusMonth)
                val response = ApiHelper.getFilms(
                    calendar.get(Calendar.YEAR),
                    SimpleDateFormat("MMMM", Locale.ENGLISH).format(calendar.time)
                ).execute()

                isLoaded = true
                if (response.isSuccessful) {
                    countTotal += response.body()?.total ?: 0
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
                minusMonth++
                isUpdate = false
            } catch (e: Exception) {
                Log.e("error", e.stackTraceToString())
                isErrorLiveData.postValue(e.message)
            }
        }
    }

    override fun onFindData(findString: String) {
        countTotal = 0
        if (findString.isNotEmpty()) {
            isLoaded = false
            try {
                MainScope().launch(Dispatchers.IO) {
                    val response = ApiHelper.getFilteredFilms(findString).execute()

                    isLoaded = true
                    if (response.isSuccessful) {
                        response.body()!!.films.let {
                            filmsLiveData.postValue(it)
                        }
                    } else {
                        when (response.code()) {
                            401 -> isErrorLiveData.postValue("Проблема с api-key")
                        }
                    }
                }
            } catch (ex: Exception) {
                Log.e("error", ex.stackTraceToString())
                isLoaded = true
            }
        } else {
            minusMonth = 0
            filmsLiveData.value = mutableListOf()
            onLoadData()
        }
    }
}