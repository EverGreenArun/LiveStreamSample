package com.arun.livestreamsample.network

sealed class GenericResponse<out T: Any> {
    data class Success<out T : Any>(val data: T) : GenericResponse<T>()
    data class Error(val exception: Exception) : GenericResponse<Nothing>()
}