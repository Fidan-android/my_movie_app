package com.example.my_movie_app.api.models

import com.google.gson.annotations.SerializedName

data class FilmsModel(
    @SerializedName("id") val id: Int? = null,
    @SerializedName("title") val title: String? = null,
    @SerializedName("description") val description: String? = null,
    @SerializedName("time") val time: Int? = null,
    @SerializedName("stars") val stars: String? = null,
    @SerializedName("author") val author: String? = null,
    @SerializedName("image") val image: String? = null,
    @SerializedName("bg_image") val bgImage: String? = null,
    @SerializedName("category") val category: String? = null
)
