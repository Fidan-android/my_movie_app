package com.example.my_movie_app.models

import com.google.gson.annotations.SerializedName

data class EditProfileRequest(
    @SerializedName("first_name") val firstName: String,
    @SerializedName("middle_name") val middleName: String,
    @SerializedName("last_name") val lastName: String
)
