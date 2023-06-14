package com.example.my_movie_app.api.models

import com.google.gson.annotations.SerializedName

data class CommentModel(
    @SerializedName("id") val id: Int,
    @SerializedName("user") val userName: String,
    @SerializedName("user_avatar") val userAvatar: String,
    @SerializedName("stars") val star: Int,
    @SerializedName("comment") val comment: String,
    @SerializedName("created_at") val createdAt: String
)
