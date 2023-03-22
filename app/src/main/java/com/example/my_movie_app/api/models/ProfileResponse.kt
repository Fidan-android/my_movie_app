package com.example.my_movie_app.api.models

import com.google.gson.annotations.SerializedName

data class ProfileResponse(
    @SerializedName("first_name") val firstName: String,
    @SerializedName("name") val name: String,
    @SerializedName("last_name") val lastName: String? = null,
    @SerializedName("birthday") val birthday: String? = null,
    @SerializedName("is_admin") val isAdmin: Boolean? = null,
    @SerializedName("image_url") val imageUrl: String? = null,
    @SerializedName("message") val message: String? = null
)
