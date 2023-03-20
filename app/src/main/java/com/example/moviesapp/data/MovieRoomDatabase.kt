package com.example.moviesapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.moviesapp.model.Converter
import com.example.moviesapp.model.Result
import com.example.moviesapp.service.MovieDAO

@Database(entities = [Result::class], version = 1, exportSchema = false)
@TypeConverters(Converter::class)
abstract class MovieRoomDatabase : RoomDatabase() {

    abstract fun movieDao() : MovieDAO

    companion object {
        @Volatile
        private var INSTANCE: MovieRoomDatabase? = null
        fun getDatabase(context: Context) : MovieRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MovieRoomDatabase::class.java,
                    "movie_database"
                ).fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}