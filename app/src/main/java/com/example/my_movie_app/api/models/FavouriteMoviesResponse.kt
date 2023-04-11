package com.example.my_movie_app.api.models

import com.google.gson.annotations.SerializedName

data class FavouriteMoviesResponse(
    @SerializedName("message") val message: String? = null,
    @SerializedName("favourites") val favourites: MutableList<FavouriteFilmModel>? = null
)