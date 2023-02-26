package com.example.my_movie_app.ui.category

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.my_movie_app.IBaseViewModel
import com.example.my_movie_app.api.ApiHelper
import com.example.my_movie_app.api.models.CategoryModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class CategoryViewModel : ViewModel(), IBaseViewModel<MutableList<CategoryModel>> {

    private val categoriesLiveData: MutableLiveData<MutableList<CategoryModel>> = MutableLiveData()
    private val isErrorLiveData: MutableLiveData<String> = MutableLiveData()

    override fun onGetData() = categoriesLiveData
    override fun onGetErrorMessage() = isErrorLiveData

    override fun onLoadData() {
        GlobalScope.launch(Dispatchers.IO) {
            val response = ApiHelper.getCategories()
            if (response.message == null) {
                response.categories.let {
                    categoriesLiveData.postValue(it)
                }
            } else {
                response.message.let {
                    isErrorLiveData.postValue(it)
                }
            }
        }
    }

}