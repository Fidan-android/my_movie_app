package com.example.my_movie_app.ui.films.film_page

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.my_movie_app.api.ApiHelper
import com.example.my_movie_app.api.models.FilmsModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class FilmPageViewModel : ViewModel(), IFilmPageViewModel<FilmsModel> {

    private val filmLiveData: MutableLiveData<FilmsModel> = MutableLiveData()
    private val isErrorLiveData: MutableLiveData<String> = MutableLiveData()
    private var filmId: Int = 0
    private val isProgress: MutableLiveData<Boolean> = MutableLiveData(false)

    override fun onGetData() = filmLiveData
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
        GlobalScope.launch(Dispatchers.IO) {
            val response = ApiHelper.getFilms()
            if (response.message == null) {
                response.films.let { list ->
                    filmLiveData.postValue(list?.first { item -> item.id == filmId })
                    isProgress.postValue(false)
                }
            } else {
                response.message.let {
                    isErrorLiveData.postValue(it)
                }
            }
        }
    }
}