package com.example.my_movie_app.api.models

import com.google.gson.annotations.SerializedName

data class CommentResponse(
    @SerializedName("message") val message: String? = null,
    @SerializedName("comments") val comments: MutableList<CommentModel>? = null
)