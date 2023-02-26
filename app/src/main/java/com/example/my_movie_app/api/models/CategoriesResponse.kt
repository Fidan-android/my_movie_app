package com.example.my_movie_app.api.models

import com.google.gson.annotations.SerializedName

data class CategoriesResponse(
    @SerializedName("categories") val categories: MutableList<CategoryModel>? = null,
    @SerializedName("message") val message: String? = null
)
