package com.example.my_movie_app.registration

import androidx.lifecycle.MutableLiveData
import com.example.my_movie_app.IBaseViewModel

interface IRegistrationViewModel<T> : IBaseViewModel<T> {
    fun onSaveFirstNameString(newFirstName: String?)
    fun onSaveNameString(newName: String?)
    fun onSaveEmailString(newEmail: String?)
    fun onSavePasswordString(newPassword: String?)
    fun onSaveRepeatPasswordString(newRepeatPassword: String?)
    fun onProgressBar(): MutableLiveData<Boolean>
}