package com.example.moviesapp

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.moviesapp.model.Result
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.moviesapp.data.MovieRoomDatabase
import com.example.moviesapp.service.MovieDAO
import kotlinx.coroutines.test.runTest

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class MovieDAOTest {

    @get:Rule
    var instantTaskExecutor = InstantTaskExecutorRule()

    lateinit var movieDao: MovieDAO
    lateinit var movieData: MovieRoomDatabase

    @Before
    fun init() {
        movieData  = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext<Context?>().applicationContext,
            movieData::class.java
        ).allowMainThreadQueries()
            .build()
        movieDao = movieData.movieDao()
    }

    @Test
    fun insertMovie() = runTest {
        val dummyData = Result(true,
            "backdrop", listOf(123, 345), 1, "English", "title",
        "over view", 123.23, "poster", "release date",
        "title", true, 3.0, 1)
        movieDao.insert(dummyData)
        val list = movieDao.getMovies().getOrAwaitValue()
        assertThat(list).contains(dummyData)

    }

    @Test
    fun deleteMovie() = runTest {
        val dummyData = Result(true,
            "backdrop", listOf(123, 345), 1, "English", "title",
            "over view", 123.23, "poster", "release date",
            "title", true, 3.0, 1)
        movieDao.insert(dummyData)
        movieDao.delete(dummyData)

        val list = movieDao.getMovies().getOrAwaitValue()
        assertThat(list).doesNotContain(dummyData)
    }

    @After
    fun teardown() {
        movieData.close()
    }
}