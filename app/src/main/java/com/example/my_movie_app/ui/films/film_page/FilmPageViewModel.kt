package com.example.my_movie_app.ui.films.film_page

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.my_movie_app.api.ApiHelper
import com.example.my_movie_app.api.models.FilmModel
import com.example.my_movie_app.api.models.FilmVideoModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class FilmPageViewModel : ViewModel(), IFilmPageViewModel<FilmModel> {

    private val filmLiveData: MutableLiveData<FilmModel> = MutableLiveData()
    private val videosLiveData: MutableLiveData<FilmVideoModel> = MutableLiveData()
    private val isErrorLiveData: MutableLiveData<String> = MutableLiveData()
    private var filmId: Int = 0
    private val isProgress: MutableLiveData<Boolean> = MutableLiveData(true)

    override fun onGetData() = filmLiveData
    override fun onGetVideos() = videosLiveData
    override fun onGetErrorMessage() = isErrorLiveData
    override fun onProgressBar() = isProgress

    override fun onSaveFilmId(newFilmId: Int) {
        filmId = newFilmId
    }

    override fun onLoadData() {
        if (filmId == 0) {
            isErrorLiveData.value = "Не выбран фильм, вернитесь на предыдущий экран."
            return
        }
        isProgress.value = true
        MainScope().launch(Dispatchers.IO) {
            val filmInfoResponse = ApiHelper.getFilmById(filmId).execute()
            if (filmInfoResponse.isSuccessful) {
                filmLiveData.postValue(filmInfoResponse.body())
            } else {
                isErrorLiveData.postValue(filmInfoResponse.message())
            }

            // load video
            val videosResponse = ApiHelper.getVideosByFilm(filmId).execute()
            if (videosResponse.isSuccessful) {
                videosLiveData.postValue(videosResponse.body())
                isProgress.postValue(false)
            } else {
                isErrorLiveData.postValue(videosResponse.message())
            }
        }
    }
}