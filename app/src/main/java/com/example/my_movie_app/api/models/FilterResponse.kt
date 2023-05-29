package com.example.my_movie_app.api.models

import com.google.gson.annotations.SerializedName

data class FilterResponse(
    @SerializedName("genres") val genres: MutableList<GenreModel>,
    @SerializedName("countries") val countries: MutableList<CountryModel>
)