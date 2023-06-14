package com.example.my_movie_app.ui.genres.filter

import com.example.my_movie_app.IBaseViewModel

interface IFilteredFilmsViewModel<T> : IBaseViewModel<T> {
    fun onSetGenreId(genreId: Int)
    fun onIsLoaded(): Boolean
    fun onGetTotalCount(): Int
}