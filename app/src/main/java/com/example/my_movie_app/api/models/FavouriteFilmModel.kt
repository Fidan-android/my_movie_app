package com.example.my_movie_app.api.models

import com.google.gson.annotations.SerializedName

data class FavouriteFilmModel(
    @SerializedName("kinopoisk_id") val kinopoiskId: Int? = null,
    @SerializedName("film_id") val filmId: Int? = null,
    @SerializedName("name_ru") val nameRu: String? = null,
    @SerializedName("poster_url_preview") val posterUrlPreview: String? = null,
)
