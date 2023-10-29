package com.example.recipeapplication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.recipeapplication.repository.RecipeRepository

class RecipeViewModelFactory(
    val recipeRepository: RecipeRepository
): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return RecipeViewModel(recipeRepository) as T
    }

}