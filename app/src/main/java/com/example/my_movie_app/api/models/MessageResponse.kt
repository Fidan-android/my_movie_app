package com.example.my_movie_app.api.models

import com.google.gson.annotations.SerializedName

data class MessageResponse(
    @SerializedName("message") val message: String? = null
)