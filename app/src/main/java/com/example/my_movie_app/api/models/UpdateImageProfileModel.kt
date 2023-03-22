package com.example.my_movie_app.api.models

import com.google.gson.annotations.SerializedName

data class UpdateImageProfileModel(
    @SerializedName("file") val file: String
)
