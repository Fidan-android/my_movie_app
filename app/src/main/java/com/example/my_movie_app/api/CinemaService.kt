package com.example.my_movie_app.api

import com.example.my_movie_app.api.models.FilmModel
import com.example.my_movie_app.api.models.FilmVideoModel
import com.example.my_movie_app.api.models.FilmsResponse
import com.example.my_movie_app.api.models.FilteredFilmsByKeywordModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CinemaService {
    @GET("v2.2/films/premieres")
    fun getFilms(@Query("year") year: Int, @Query("month") month: String): Call<FilmsResponse>

    @GET("v2.2/films/{id}")
    fun getFilmById(@Path("id") filmId: Int): Call<FilmModel>

    @GET("v2.2/films/{id}/videos")
    fun getVideosByFilm(@Path("id") filmId: Int): Call<FilmVideoModel>

    @GET("v2.1/films/search-by-keyword")
    fun getFilteredFilms(
        @Query("keyword") keyword: String,
    ): Call<FilteredFilmsByKeywordModel>
}