package com.example.moviesapp.service

import com.example.moviesapp.model.ApiResponse
import com.example.moviesapp.model.PopularApiResponse
import com.example.moviesapp.model.UpcomingApiResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieInterface {

    @GET("/3/movie/now_playing")
    suspend fun getNowPlaying(
        @Query("api_key") api_key: String
    ) : ApiResponse

    @GET("/3/movie/popular")
    suspend fun getPopular(
        @Query("api_key") api_key: String
    ) : PopularApiResponse

    @GET("/3/movie/upcoming")
    suspend fun getUpcoming(
        @Query("api_key") api_key: String
    ) : UpcomingApiResponse
}