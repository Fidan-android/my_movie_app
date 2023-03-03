package com.example.my_movie_app.api.models

import com.google.gson.annotations.SerializedName

data class GenreModel(
    @SerializedName("genre") val genre: String
)
