package com.example.my_movie_app.api.models

import com.google.gson.annotations.SerializedName

data class StaffModel(
    @SerializedName("staffId") val staffId: Int,
    @SerializedName("nameRu") val nameRu: String,
    @SerializedName("nameEn") val nameEn: String,
    @SerializedName("description") val description: String,
    @SerializedName("posterUrl") val posterUrl: String,
    @SerializedName("professionText") val professionText: String,
    @SerializedName("professionKey") val professionKey: String
)
