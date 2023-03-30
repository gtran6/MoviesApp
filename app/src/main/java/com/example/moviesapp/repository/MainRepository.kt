package com.example.moviesapp.repository

import com.example.moviesapp.model.ApiResponse
import com.example.moviesapp.model.PopularApiResponse
import com.example.moviesapp.model.UpcomingApiResponse
import com.example.moviesapp.service.MovieDAO
import com.example.moviesapp.service.MovieInterface
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import com.example.moviesapp.model.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn

class MainRepository @Inject constructor(val movieInterface: MovieInterface, val movieDAO: MovieDAO) {

    fun getNowPlaying(api_key: String) : Flow<ApiResponse> = flow {
        emit(movieInterface.getNowPlaying(api_key))
    }.flowOn(Dispatchers.Main)

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