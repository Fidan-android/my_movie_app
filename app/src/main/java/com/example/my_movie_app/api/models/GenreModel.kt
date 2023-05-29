package com.example.my_movie_app.api.models

import com.google.gson.annotations.SerializedName

data class GenreModel(
    @SerializedName("id") val id: Int? = null,
    @SerializedName("genre") val genre: String
)
