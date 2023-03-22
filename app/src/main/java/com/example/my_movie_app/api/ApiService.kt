package com.example.my_movie_app.api

import com.example.my_movie_app.api.models.*
import com.example.planner.models.LoginRequest
import com.example.planner.models.RegistrationRequest
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @POST("login/")
    suspend fun login(@Body body: LoginRequest): LoginResponse

    @POST("registration/")
    suspend fun registration(@Body body: RegistrationRequest): MessageResponse

    @GET("account/")
    fun getProfile(): Call<ProfileResponse>

    @POST("account-image/")
    fun updateImageProfile(
        @Body body: UpdateImageProfileModel
    ): Call<Any>

    @GET("categories/")
    suspend fun getCategories(): CategoriesResponse

    @GET("cinema/")
    suspend fun getCinema(): CinemaResponse
}