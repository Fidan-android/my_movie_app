package com.example.my_movie_app.models

import com.google.gson.annotations.SerializedName

data class AddCommentRequest(
    @SerializedName("film_id") val filmId: Int,
    @SerializedName("comment") val comment: String,
    @SerializedName("stars") val stars: Int
)
