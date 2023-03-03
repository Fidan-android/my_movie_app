package com.example.my_movie_app.api

import com.example.planner.models.LoginRequest
import com.example.planner.models.RegistrationRequest

object ApiHelper {
    private val apiService = ApiManager.apiService
    private val cinemaService = ApiManager.cinemaService

    suspend fun login(body: LoginRequest) = apiService.login(body)

    suspend fun registration(body: RegistrationRequest) = apiService.registration(body)

    suspend fun getProfile() = apiService.getProfile()

    suspend fun getFilms() = cinemaService.getFilms()

    suspend fun getCategories() = apiService.getCategories()

    suspend fun getCinema() = apiService.getCinema()
}