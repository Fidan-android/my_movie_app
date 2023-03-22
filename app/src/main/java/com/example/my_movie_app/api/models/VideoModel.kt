package com.example.my_movie_app.api.models

import com.google.gson.annotations.SerializedName

data class VideoModel(
    @SerializedName("url") val url: String,
    @SerializedName("name") val name: String,
    @SerializedName("site") val site: String
)
