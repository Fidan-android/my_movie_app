package com.example.my_movie_app.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.my_movie_app.Properties
import com.example.my_movie_app.api.ApiHelper
import com.example.planner.models.LoginRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch


class LoginViewModel : ViewModel(), ILoginViewModel<String> {

    private val isAuthorize: MutableLiveData<String> = MutableLiveData()
    private val isError: MutableLiveData<String> = MutableLiveData()
    private val isProgress: MutableLiveData<Boolean> = MutableLiveData(false)
    private var email: String = ""
    private var password: String = ""

    override fun onGetData() = isAuthorize
    override fun onGetErrorMessage() = isError
    override fun onProgressBar() = isProgress

    override fun onSaveLoginString(newLogin: String?) {
        email = newLogin ?: ""
    }

    override fun onSavePasswordString(newPassword: String?) {
        password = newPassword ?: ""
    }

    override fun onLoadData() {
        if (email.isEmpty() || password.isEmpty()) {
            isError.value = "Все поля должны быть заполнены"
            return
        }
        if (!Properties.isNetworkConnect) {
            isError.value = "Отсутствует интернет-соединение"
            return
        }
        isProgress.value = true
        MainScope().launch(Dispatchers.IO) {
            val response = ApiHelper.login(LoginRequest(email, password))
            response.token?.let {
                isAuthorize.postValue(it)
            } ?: when (response.message) {
                "uncorrected" -> {
                    isProgress.postValue(false)
                    isError.postValue("Неверный логин или пароль")
                }
                else -> {
                    isProgress.postValue(false)
                    isError.postValue("Непредвиденная ошибка")
                }
            }
        }
    }

}