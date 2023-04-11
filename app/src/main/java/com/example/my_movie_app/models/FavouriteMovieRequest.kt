package com.example.my_movie_app.models

import com.google.gson.annotations.SerializedName

data class FavouriteMovieRequest(
    @SerializedName("kinopoisk_id") val kinopoiskId: Int? = null,
    @SerializedName("film_id") val filmId: Int? = null,
    @SerializedName("name_ru") val nameRu: String? = null,
    @SerializedName("poster_url_preview") val posterUrlPreview: String? = null,
    @SerializedName("is_favourite") val isFavourite: Boolean? = null,
)