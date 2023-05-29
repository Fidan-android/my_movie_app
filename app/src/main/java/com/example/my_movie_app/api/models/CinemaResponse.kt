package com.example.my_movie_app.api.models

import com.google.gson.annotations.SerializedName

data class CinemaResponse(
    @SerializedName("cinema") val cinemas: MutableList<GenreModel>? = null,
    @SerializedName("message") val message: String? = null
)
