package com.example.recipeapplication.api

import com.example.recipeapplication.model.Recipe
import com.example.recipeapplication.util.Constants.Companion.API_HOST
import com.example.recipeapplication.util.Constants.Companion.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface RecipesAPI {

    @Headers(
        "X-RapidAPI-Key: $API_KEY",
        "X-RapidAPI-Host: $API_HOST"
    )
    @GET("recipes/list")
    suspend fun getRecipes(
        //Number of items to be ignored in response for paging
        //Everytime the user scrolls to the bottom of recycler view,
        //add 10 to this number, to display the next 10 recipes and vice versa
        @Query("from")
        startIndex: Int = 0,
        //Number of items returned per response
        @Query("size")
        responseSize: Int = 10,
        @Query("tags")
        tag: String = "under_30_minutes",
    ): Response<Recipe>

    @Headers(
        "X-RapidAPI-Key: $API_KEY",
        "X-RapidAPI-Host: $API_HOST"
    )
    @GET("recipes/list")
    suspend fun searchRecipe(
        @Query("from")
        startIndex: Int = 0,
        @Query("size")
        responseSize: Int,
        @Query("q")
        searchQuery: String
    ): Response<Recipe>

}