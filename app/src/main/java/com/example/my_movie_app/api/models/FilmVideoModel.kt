package com.example.my_movie_app.api.models

import com.google.gson.annotations.SerializedName

data class FilmVideoModel(
    @SerializedName("total") val total: Int,
    @SerializedName("items") val videoItems: MutableList<VideoModel>
)