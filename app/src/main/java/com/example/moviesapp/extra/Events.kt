package com.example.moviesapp.extra

sealed class Events<T> (var data: T? = null, val msh: String? = "") {

    class Success<T>(data: T?) : Events<T>(data)

    class Loading<T>(data: T? = null) : Events<T>()

    class Error<T>(data: T? = null, msg: String? = null) : Events<T>(data, msg)
}
