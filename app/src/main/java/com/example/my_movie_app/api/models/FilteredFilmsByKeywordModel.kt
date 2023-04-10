package com.example.my_movie_app.api.models

import com.google.gson.annotations.SerializedName

data class FilteredFilmsByKeywordModel(
    @SerializedName("keyword") val keyword: String,
    @SerializedName("pagesCount") val pages: Int,
    @SerializedName("searchFilmsCountResult") val filmsCount: Int,
    @SerializedName("films") val films: MutableList<FilmModel>
)