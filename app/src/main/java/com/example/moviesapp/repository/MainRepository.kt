package com.example.moviesapp.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.example.moviesapp.model.ApiResponse
import com.example.moviesapp.model.PopularApiResponse
import com.example.moviesapp.model.UpcomingApiResponse
import com.example.moviesapp.service.MovieDAO
import com.example.moviesapp.service.MovieInterface
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import com.example.moviesapp.model.Result
import androidx.lifecycle.viewModelScope
import com.example.moviesapp.data.MovieRoomDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class MainRepository @Inject constructor(val movieInterface: MovieInterface, val movieDAO: MovieDAO) {

    fun getNowPlaying(api_key: String) : Flow<ApiResponse> = flow {
        emit(movieInterface.getNowPlaying(api_key))
    }

    fun getPopular(api_key: String) : Flow<PopularApiResponse> = flow {
        emit(movieInterface.getPopular(api_key))
    }

    fun getUpcoming(api_key: String) : Flow<UpcomingApiResponse> = flow {
        emit(movieInterface.getUpcoming(api_key))
    }

    suspend fun insertItem(movie: Result) = movieDAO.insert(movie)

    suspend fun deleteItem(movie: Result) = movieDAO.delete(movie)

    fun getAllItems() = movieDAO.getMovies()

}