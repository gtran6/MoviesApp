package com.example.moviesapp.service

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.moviesapp.model.Result
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(movie: Result)

    @Delete
    suspend fun delete(movie: Result)

    @Query("SELECT * from movie_table")
    fun getMovies() : LiveData<List<Result>>
}