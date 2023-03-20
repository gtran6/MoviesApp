package com.example.moviesapp.di

import android.content.Context
import com.example.moviesapp.data.MovieRoomDatabase
import com.example.moviesapp.extra.Utils.BASE_URL
import com.example.moviesapp.repository.MainRepository
import com.example.moviesapp.service.MovieDAO
import com.example.moviesapp.service.MovieInterface
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideRepo(movieInterface: MovieInterface, movieDAO: MovieDAO) : MainRepository = MainRepository(movieInterface, movieDAO)

    @Singleton
    @Provides
    fun provideRetrofitInstance() : MovieInterface {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MovieInterface::class.java)
    }

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context) = MovieRoomDatabase.getDatabase(context)

    @Provides
    fun provideMovieDao(db: MovieRoomDatabase): MovieDAO {
        return db.movieDao()
    }
}