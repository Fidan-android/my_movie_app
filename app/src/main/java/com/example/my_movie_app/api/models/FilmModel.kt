package com.example.my_movie_app.api.models

data class FilmModel(
    val kinopoiskId: Int,
    val nameRu: String,
    val nameEn: String,
    val year: Int,
    val posterUrl: String,
    val posterUrlPreview: String,
    val countries: MutableList<CountryModel>,
    val genres: MutableList<GenreModel>,
    val duration: Int,
    val premiereRu: String,
)
