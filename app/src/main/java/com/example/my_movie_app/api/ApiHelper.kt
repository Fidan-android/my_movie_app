package com.example.my_movie_app.api

import com.example.my_movie_app.api.models.UpdateImageProfileModel
import com.example.planner.models.LoginRequest
import com.example.planner.models.RegistrationRequest
import okhttp3.MultipartBody

object ApiHelper {
    private val apiService = ApiManager.apiService
    private val cinemaService = ApiManager.cinemaService

    suspend fun login(body: LoginRequest) = apiService.login(body)

    suspend fun registration(body: RegistrationRequest) = apiService.registration(body)

    fun getProfile() = apiService.getProfile()
    fun updateImageProfile(model: UpdateImageProfileModel) = apiService.updateImageProfile(model)

    fun getFilms(year: Int, month: String) = cinemaService.getFilms(year, month)

    fun getFilmById(filmId: Int) = cinemaService.getFilmById(filmId)

    fun getVideosByFilm(filmId: Int) = cinemaService.getVideosByFilm(filmId)

    suspend fun getCategories() = apiService.getCategories()

    suspend fun getCinema() = apiService.getCinema()
}