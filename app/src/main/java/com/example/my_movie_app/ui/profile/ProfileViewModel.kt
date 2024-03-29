package com.example.my_movie_app.ui.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.my_movie_app.api.ApiHelper
import com.example.my_movie_app.api.models.ProfileResponse
import com.example.my_movie_app.api.models.UpdateImageProfileModel
import com.example.my_movie_app.models.EditProfileRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import java.io.File
import java.util.*

class ProfileViewModel : ViewModel(), IProfileViewModel<ProfileResponse> {

    private val profileLiveData: MutableLiveData<ProfileResponse> = MutableLiveData()
    private val isErrorLiveData: MutableLiveData<String> = MutableLiveData()

    override fun changeImageProfile(path: String) {
        isErrorLiveData.postValue(Base64.getEncoder().encodeToString(File(path).readBytes()))
        MainScope().launch(Dispatchers.IO) {
            ApiHelper.updateImageProfile(
                UpdateImageProfileModel(
                    Base64.getEncoder().encodeToString(File(path).readBytes())
                )
            ).execute()
        }.invokeOnCompletion {
            onLoadData()
        }
    }

    override fun onEditProfile(firstName: String, middleName: String, lastName: String) {
        MainScope().launch(Dispatchers.IO) {
            try {
                val response = ApiHelper.editProfile(EditProfileRequest(firstName, middleName, lastName)).execute()
                profileLiveData.postValue(response.body())
            } catch (e: Exception) {
                isErrorLiveData.postValue(e.message)
            }
        }
    }

    override fun onGetData() = profileLiveData
    override fun onGetErrorMessage() = isErrorLiveData

    override fun onLoadData() {
        MainScope().launch(Dispatchers.IO) {
            try {
                val response = ApiHelper.getProfile().execute()
                profileLiveData.postValue(response.body())
            } catch (e: Exception) {
                isErrorLiveData.postValue(e.message)
            }
        }
    }

}