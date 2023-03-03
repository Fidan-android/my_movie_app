package com.example.my_movie_app.api

import com.example.my_movie_app.api.models.FilmsResponse
import retrofit2.Call
import retrofit2.http.GET

interface CinemaService {

    @GET("films/premieres")
    suspend fun getFilms(): Call<FilmsResponse>
}