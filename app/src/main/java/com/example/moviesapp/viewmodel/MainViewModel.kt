package com.example.moviesapp.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviesapp.extra.Events
import com.example.moviesapp.repository.MainRepository
import com.example.moviesapp.model.Result
import com.example.moviesapp.model.ResultX
import com.example.moviesapp.model.ResultXX
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(val mainRepository: MainRepository) : ViewModel() {
    val nowPlaying : MutableLiveData<Events<List<Result>>> = MutableLiveData()
    val popular : MutableLiveData<Events<List<ResultX>>> = MutableLiveData()
    val upcoming : MutableLiveData<Events<List<ResultXX>>> = MutableLiveData()

    private val savedMovieEventChannel = Channel<SavedMovieEvent>()

    sealed class SavedMovieEvent {
        data class ShowUndoDeleteMovieMessage(val movie: Result) : SavedMovieEvent()
    }

    sealed class MovieEvent{
        data class ShowMovieSavedMessage(val message: String): MovieEvent()
    }

    private val movieEventChannel = Channel<MovieEvent>()
    val movieEvent = movieEventChannel.receiveAsFlow()

    val savedMovieEvent = savedMovieEventChannel.receiveAsFlow()

    fun getNowPlayingMovie(api_key: String) {
        viewModelScope.launch {
            nowPlaying.postValue(Events.Loading())
            mainRepository.getNowPlaying(api_key).catch {
                Log.e("Now_playing", "get: ${it.localizedMessage}")
                nowPlaying.postValue(Events.Error(msg = it.localizedMessage))
            }.collect { list ->
                nowPlaying.postValue(Events.Success(list.results))
            }
        }
    }

    fun getPopularMovie(api_key: String) {
        viewModelScope.launch {
            popular.postValue(Events.Loading())
            mainRepository.getPopular(api_key).catch {
                Log.e("Popular", "get: ${it.localizedMessage}")
                popular.postValue(Events.Error(msg = it.localizedMessage))
            }.collect { list ->
                popular.postValue(Events.Success(list.results))
            }
        }
    }

    fun getUpcomingMovie(api_key: String) {
        viewModelScope.launch {
            upcoming.postValue(Events.Loading())
            mainRepository.getUpcoming(api_key).catch {
                Log.e("Upcoming", "get: ${it.localizedMessage}")
                upcoming.postValue(Events.Error(msg = it.localizedMessage))
            }.collect { list ->
                upcoming.postValue(Events.Success(list.results))
            }
        }
    }

    fun getAllItems() = mainRepository.getAllItems()

    //delete movie
    fun onMovieSwiped(movie: Result) {
        viewModelScope.launch {
            mainRepository.deleteItem(movie)
            savedMovieEventChannel.send(SavedMovieEvent.ShowUndoDeleteMovieMessage(movie))
        }
    }

    fun onUndoDeleteClick(movie: Result) {
        viewModelScope.launch {
            mainRepository.insertItem(movie)
        }
    }

    // insert movie
    fun saveMovie(movie: Result) {
        viewModelScope.launch {
            mainRepository.insertItem(movie)
            movieEventChannel.send(MovieEvent.ShowMovieSavedMessage("Movie Saved!"))
        }
    }
}