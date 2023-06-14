package com.example.my_movie_app.ui.profile

import com.example.my_movie_app.IBaseViewModel

interface IProfileViewModel<T> : IBaseViewModel<T> {
    fun changeImageProfile(path: String)
    fun onEditProfile(firstName: String, middleName: String, lastName: String)
}