package com.example.my_movie_app.api

import com.example.my_movie_app.api.models.*
import com.example.planner.models.LoginRequest
import com.example.planner.models.RegistrationRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @POST("login/")
    suspend fun login(@Body body: LoginRequest): LoginResponse

    @POST("registration/")
    suspend fun registration(@Body body: RegistrationRequest): MessageResponse

    @GET("account/")
    suspend fun getProfile(): ProfileResponse

    @GET("categories/")
    suspend fun getCategories(): CategoriesResponse

    @GET("cinema/")
    suspend fun getCinema(): CinemaResponse
}