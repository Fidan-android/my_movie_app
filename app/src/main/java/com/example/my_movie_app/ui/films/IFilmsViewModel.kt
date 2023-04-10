package com.example.my_movie_app.ui.films

import com.example.my_movie_app.IBaseViewModel

interface IFilmsViewModel<T> : IBaseViewModel<T> {
    fun onIsLoaded(): Boolean
    fun onGetTotalCount(): Int
    fun onIsUpdate(): Boolean
    fun onFindData(findString: String)
}