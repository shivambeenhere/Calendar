package com.example.calendarapp.utils


sealed class Result<T> {
    data class Loading<T>(val data: T? = null): Result<T>()

    data class Success<T>(val data: T): Result<T>()

    data class Error<T>(val error: String): Result<T>()

    data class RestError<T>(val error: String) : Result<T>()
}