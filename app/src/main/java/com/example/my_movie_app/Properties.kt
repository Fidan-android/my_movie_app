package com.example.my_movie_app

import androidx.lifecycle.MutableLiveData
import com.example.my_movie_app.api.models.FavouriteFilmModel

object Properties {
    var isNetworkConnect: Boolean = false
    var isHome: Boolean = false

    val favouriteMovies: MutableLiveData<MutableList<FavouriteFilmModel>> = MutableLiveData()
}