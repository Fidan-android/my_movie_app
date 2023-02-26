package com.example.my_movie_app.api.models

import com.google.gson.annotations.SerializedName

data class CategoryModel(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val categoryName: String,
    @SerializedName("image_url") val imageUrl: String
)
