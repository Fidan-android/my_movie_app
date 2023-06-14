package com.example.my_movie_app

import androidx.lifecycle.MutableLiveData
import com.example.my_movie_app.api.models.FavouriteFilmModel

object Properties {
    var isNetworkConnect: Boolean = false
    const val RESULT_AUTH = 8

    val favouriteMovies: MutableLiveData<MutableList<FavouriteFilmModel>> = MutableLiveData()
}