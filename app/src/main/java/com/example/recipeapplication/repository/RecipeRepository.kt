package com.example.recipeapplication.repository

import com.example.recipeapplication.api.RetrofitInstance
import com.example.recipeapplication.database.FirebaseDAO
import com.example.recipeapplication.model.Result
import com.example.recipeapplication.util.UiState

class RecipeRepository(
    private val db: FirebaseDAO
){

    suspend fun getRecipesForHomePage(startIndex: Int, tag: String) =
        RetrofitInstance.api.getRecipes(startIndex,40,tag)

    suspend fun getSearchRecipes(responseSize: Int, searchQuery: String) =
        RetrofitInstance.api.searchRecipe(0, responseSize, searchQuery)

    fun getSavedRecipes(result: (UiState<List<Result>>) -> Unit){
        db.getSavedRecipes(){
            result.invoke(it)
        }
    }

    fun getSavedRecipe(recipeName: String ,result: (UiState<Result?>) -> Unit){
        db.getSavedRecipe(recipeName){
            result.invoke(it)
        }
    }

    fun addRecipe(result: Result, response: (UiState<String>) -> Unit){
        db.addRecipe(result){
            response.invoke(it)
        }
    }

    fun removeRecipe(recipeName: String) = db.removeRecipe(recipeName)

    companion object{
        @Volatile private var instance: RecipeRepository? = null

        fun getInstance(db: FirebaseDAO) = instance ?: synchronized(this){
            instance ?: RecipeRepository(db).also {
                instance = it
            }
        }
    }

}