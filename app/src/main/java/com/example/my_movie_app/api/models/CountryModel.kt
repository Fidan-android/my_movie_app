package com.example.my_movie_app.api.models

import com.google.gson.annotations.SerializedName

data class CountryModel(
    @SerializedName("id") val id: Int? = null,
    @SerializedName("country") val country: String
)
