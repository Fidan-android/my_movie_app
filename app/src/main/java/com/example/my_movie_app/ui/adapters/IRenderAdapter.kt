package com.example.my_movie_app.ui.adapters

interface IRenderAdapter<T> {
    fun onUpdateItems(list: MutableList<T>)
}