package com.example.my_movie_app.api.models

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("token") val token: String? = null,
    @SerializedName("message") val message: String? = null
)
