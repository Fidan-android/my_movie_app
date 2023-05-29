package com.example.my_movie_app.ui.films.film_page

import androidx.lifecycle.MutableLiveData
import com.example.my_movie_app.IBaseViewModel
import com.example.my_movie_app.api.models.FilmVideoModel
import com.example.my_movie_app.api.models.StaffModel

interface IFilmPageViewModel<T> : IBaseViewModel<T> {
    fun onSaveFilmId(newFilmId: Int)
    fun onProgressBar(): MutableLiveData<Boolean>
    fun onGetVideos(): MutableLiveData<FilmVideoModel>
    fun onFavouriteMovie(isFavourite: Boolean)
    fun onGetStaff(): MutableLiveData<MutableList<StaffModel>>
    fun onCreateComment(stars: Int, comment: String)
}