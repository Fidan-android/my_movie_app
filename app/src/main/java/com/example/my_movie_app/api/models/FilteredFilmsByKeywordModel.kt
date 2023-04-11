package com.example.my_movie_app.api.models

import com.google.gson.annotations.SerializedName

data class FilteredFilmsByKeywordModel(
    @SerializedName("keyword") val keyword: String,
    @SerializedName("pagesCount") val pages: Int? = null,
    @SerializedName("searchFilmsCountResult") val filmsCount: Int? = null,
    @SerializedName("films") val films: MutableList<FilmModel>
)