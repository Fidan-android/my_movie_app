package com.example.my_movie_app.api.models

import com.google.gson.annotations.SerializedName

data class FilmsResponse(
    @SerializedName("films") val films: MutableList<FilmsModel>? = null,
    @SerializedName("message") val message: String? = null
)
