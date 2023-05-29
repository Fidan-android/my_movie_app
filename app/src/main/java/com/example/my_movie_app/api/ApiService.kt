package com.example.my_movie_app.api

import com.example.my_movie_app.api.models.*
import com.example.my_movie_app.models.AddCommentRequest
import com.example.my_movie_app.models.FavouriteMovieRequest
import com.example.planner.models.LoginRequest
import com.example.planner.models.RegistrationRequest
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @POST("login/")
    suspend fun login(@Body body: LoginRequest): LoginResponse

    @POST("registration/")
    suspend fun registration(@Body body: RegistrationRequest): MessageResponse

    @GET("account/")
    fun getProfile(): Call<ProfileResponse>

    @POST("account/image/")
    fun updateImageProfile(
        @Body body: UpdateImageProfileModel
    ): Call<Any>

    @GET("favourites/")
    fun getFavouriteMovies(): Call<FavouriteMoviesResponse>

    @POST("favourites/")
    fun saveFavouriteMovie(@Body body: FavouriteMovieRequest): Call<FavouriteMoviesResponse>

    @POST("comment/")
    fun addComment(@Body body: AddCommentRequest): Call<MessageResponse>
}