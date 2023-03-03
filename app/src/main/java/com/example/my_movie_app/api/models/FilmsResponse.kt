package com.example.my_movie_app.api.models

import com.google.gson.annotations.SerializedName

data class FilmsResponse(
    @SerializedName("total") val total: Int,
    @SerializedName("items") val films: MutableList<FilmModel>
)
