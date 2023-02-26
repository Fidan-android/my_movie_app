package com.example.my_movie_app.registration

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.my_movie_app.Properties
import com.example.my_movie_app.api.ApiHelper
import com.example.planner.models.RegistrationRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch


class RegistrationViewModel : ViewModel(), IRegistrationViewModel<String> {

    private val isAuthorize: MutableLiveData<String> = MutableLiveData()
    private val isError: MutableLiveData<String> = MutableLiveData()
    private val isProgress: MutableLiveData<Boolean> = MutableLiveData(false)
    private var firstName: String = ""
    private var name: String = ""
    private var email: String = ""
    private var password: String = ""
    private var repeatPassword: String = ""

    override fun onGetData() = isAuthorize
    override fun onGetErrorMessage() = isError
    override fun onProgressBar() = isProgress

    override fun onSaveFirstNameString(newFirstName: String?) {
        firstName = newFirstName ?: ""
    }

    override fun onSaveNameString(newName: String?) {
        name = newName ?: ""
    }

    override fun onSaveEmailString(newEmail: String?) {
        email = newEmail ?: ""
    }

    override fun onSavePasswordString(newPassword: String?) {
        password = newPassword ?: ""
    }

    override fun onSaveRepeatPasswordString(newRepeatPassword: String?) {
        repeatPassword = newRepeatPassword ?: ""
    }

    override fun onLoadData() {
        if (firstName.isEmpty() || name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            isError.value = "Все поля должны быть заполнены"
            return
        }
        if (password != repeatPassword) {
            isError.value = "Пароли должны совпадать"
            return
        }
        if (!Properties.isNetworkConnect) {
            isError.value = "Отсутствует интернет-соединение"
            return
        }
        isProgress.value = true
        MainScope().launch(Dispatchers.IO) {
            val response =
                ApiHelper.registration(RegistrationRequest(email, password, firstName, name))

            when (response.message) {
                "success" -> isAuthorize.postValue("Регистрация прошла успешно")
                else ->  {
                    isProgress.postValue(false)
                    isError.postValue("Непредвиденная ошибка")
                }
            }
        }
    }

}