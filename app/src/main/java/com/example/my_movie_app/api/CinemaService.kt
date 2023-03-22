package com.example.my_movie_app.api

import com.example.my_movie_app.api.models.FilmModel
import com.example.my_movie_app.api.models.FilmVideoModel
import com.example.my_movie_app.api.models.FilmsResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CinemaService {
    @GET("films/premieres")
    fun getFilms(@Query("year") year: Int, @Query("month") month: String): Call<FilmsResponse>
    @GET("films/{id}")
    fun getFilmById(@Path("id") filmId: Int): Call<FilmModel>
    @GET("films/{id}/videos")
    fun getVideosByFilm(@Path("id") filmId: Int): Call<FilmVideoModel>
}