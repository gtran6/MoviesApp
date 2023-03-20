package com.example.moviesapp.model

data class UpcomingApiResponse(
    val dates: DatesX,
    val page: Int,
    val results: List<ResultXX>,
    val total_pages: Int,
    val total_results: Int
)