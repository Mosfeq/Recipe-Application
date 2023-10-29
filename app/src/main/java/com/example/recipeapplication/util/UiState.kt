package com.example.recipeapplication.util

sealed class UiState<T>(
    val data: T? = null,
    val errorMessage: String? = null
) {
    class Success<T>(data: T): UiState<T>(data)
    class Error<T>(errorMessage: String?, data: T? = null): UiState<T>(data, errorMessage)
    class Loading<T>: UiState<T>()
}