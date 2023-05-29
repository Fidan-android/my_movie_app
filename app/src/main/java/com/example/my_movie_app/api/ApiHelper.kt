package com.example.my_movie_app.api

import com.example.my_movie_app.api.models.UpdateImageProfileModel
import com.example.my_movie_app.models.AddCommentRequest
import com.example.my_movie_app.models.FavouriteMovieRequest
import com.example.planner.models.LoginRequest
import com.example.planner.models.RegistrationRequest

object ApiHelper {
    private val apiService = ApiManager.apiService
    private val cinemaService = ApiManager.cinemaService

    suspend fun login(body: LoginRequest) = apiService.login(body)
    suspend fun registration(body: RegistrationRequest) = apiService.registration(body)
    fun getProfile() = apiService.getProfile()
    fun updateImageProfile(model: UpdateImageProfileModel) = apiService.updateImageProfile(model)
    fun addComment(model: AddCommentRequest) = apiService.addComment(model)
    fun getFilms(year: Int, month: String) = cinemaService.getFilms(year, month)
    fun getFilmById(filmId: Int) = cinemaService.getFilmById(filmId)
    fun getFilteredFilms(keyword: String) = cinemaService.getFilteredFilms(keyword)
    fun getVideosByFilm(filmId: Int) = cinemaService.getVideosByFilm(filmId)
    fun getGenres() = cinemaService.getGenres()
    fun getFavouriteMovies() = apiService.getFavouriteMovies()
    fun saveFavouriteMovie(model: FavouriteMovieRequest) = apiService.saveFavouriteMovie(model)
    fun getStaffByFilm(filmId: Int) = cinemaService.getStaffByFilm(filmId)
}