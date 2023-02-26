package com.example.my_movie_app.api.models

import com.google.gson.annotations.SerializedName

data class CinemaModel(
    @SerializedName("id") val id: Int? = null,
    @SerializedName("name") val cinemaName: String? = null,
    @SerializedName("address") val address: String? = null,
    @SerializedName("img_url") val imageUrl: String? = null,
    @SerializedName("latitude") val latitude: Double? = null,
    @SerializedName("longitude") val longitude: Double? = null
)
