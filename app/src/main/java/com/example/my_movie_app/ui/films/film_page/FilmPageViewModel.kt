package com.example.my_movie_app.ui.films.film_page

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.my_movie_app.Properties
import com.example.my_movie_app.api.ApiHelper
import com.example.my_movie_app.api.models.FilmModel
import com.example.my_movie_app.api.models.FilmVideoModel
import com.example.my_movie_app.api.models.StaffModel
import com.example.my_movie_app.models.AddCommentRequest
import com.example.my_movie_app.models.FavouriteMovieRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class FilmPageViewModel : ViewModel(), IFilmPageViewModel<FilmModel> {

    private val filmLiveData: MutableLiveData<FilmModel> = MutableLiveData()
    private val staffOfFilmLiveData: MutableLiveData<MutableList<StaffModel>> = MutableLiveData()
    private val videosLiveData: MutableLiveData<FilmVideoModel> = MutableLiveData()
    private val isErrorLiveData: MutableLiveData<String> = MutableLiveData()
    private var filmId: Int = 0
    private val isProgress: MutableLiveData<Boolean> = MutableLiveData(true)

    override fun onGetData() = filmLiveData
    override fun onGetVideos() = videosLiveData
    override fun onGetStaff() = staffOfFilmLiveData

    override fun onGetErrorMessage() = isErrorLiveData
    override fun onProgressBar() = isProgress

    override fun onSaveFilmId(newFilmId: Int) {
        filmId = newFilmId
    }

    override fun onLoadData() {
        if (filmId == 0) {
            isErrorLiveData.value = "Не выбран фильм, вернитесь на предыдущий экран."
            return
        }
        isProgress.value = true
        MainScope().launch(Dispatchers.IO) {
            // load film info
            val filmInfoResponse = ApiHelper.getFilmById(filmId).execute()
            if (filmInfoResponse.isSuccessful) {
                filmLiveData.postValue(filmInfoResponse.body())
            } else {
                isErrorLiveData.postValue(filmInfoResponse.message())
            }

            // load film staff
            val staffResponse = ApiHelper.getStaffByFilm(filmId).execute()
            if (staffResponse.isSuccessful) {
                staffOfFilmLiveData.postValue(staffResponse.body()?.filter { staff -> staff.professionKey == "ACTOR" }?.toMutableList())
            } else {
                isErrorLiveData.postValue(staffResponse.message())
            }

            // load video
            val videosResponse = ApiHelper.getVideosByFilm(filmId).execute()
            if (videosResponse.isSuccessful) {
                videosLiveData.postValue(videosResponse.body())
                isProgress.postValue(false)
            } else {
                isErrorLiveData.postValue(videosResponse.message())
            }
        }
    }

    override fun onFavouriteMovie(isFavourite: Boolean) {
        try {
            MainScope().launch(Dispatchers.IO) {
                val response = ApiHelper.saveFavouriteMovie(
                    FavouriteMovieRequest(
                        filmLiveData.value?.kinopoiskId,
                        filmLiveData.value?.filmId,
                        filmLiveData.value?.nameRu,
                        filmLiveData.value?.posterUrlPreview,
                        isFavourite
                    )
                ).execute()

                if (response.isSuccessful) {
                    Properties.favouriteMovies.postValue(response.body()?.favourites ?: mutableListOf())
                }
            }
        } catch (ex: Exception) {
            Log.e("Error", ex.printStackTrace().toString())
        }
    }

    override fun onCreateComment(stars: Int, comment: String) {
        try {
            MainScope().launch(Dispatchers.IO) {
                val response = ApiHelper.addComment(
                    AddCommentRequest(filmId, comment, stars)
                ).execute()

                if (response.isSuccessful) {

                }
            }
        } catch (ex: Exception) {
            Log.e("Error", ex.printStackTrace().toString())
        }
    }
}