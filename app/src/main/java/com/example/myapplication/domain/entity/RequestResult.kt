package com.example.myapplication.domain.entity

sealed class RequestResult<out R> {
    data class Success<out T>(val result: T) : RequestResult<T>()
    data class Error<out T>(
        val message: String? = null,
        val result: T? = null,
    ) : RequestResult<T>()
}