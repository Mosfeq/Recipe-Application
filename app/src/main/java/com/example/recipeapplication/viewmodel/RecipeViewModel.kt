package com.example.recipeapplication.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipeapplication.model.Recipe
import com.example.recipeapplication.model.Result
import com.example.recipeapplication.repository.RecipeRepository
import com.example.recipeapplication.util.UiState
import kotlinx.coroutines.launch
import retrofit2.Response
import kotlin.random.Random

class RecipeViewModel(
    val recipeRepository: RecipeRepository
): ViewModel() {

    private val startIndexForYou: Int = Random.nextInt(800)
    private val mutableForYouRecipes = MutableLiveData<UiState<Recipe>>()
    val forYouRecipes: LiveData<UiState<Recipe>>
        get() = mutableForYouRecipes

    private val startIndexTrending: Int = Random.nextInt(30)
    private val mutableTrendingRecipes = MutableLiveData<UiState<Recipe>>()
    val trendingRecipes: LiveData<UiState<Recipe>>
        get() = mutableTrendingRecipes

    private val mutableRecipeDetail = MutableLiveData<UiState<Recipe>>()
    val recipeDetail: LiveData<UiState<Recipe>>
        get() = mutableRecipeDetail

    private val responseSize: Int = 40
    private val mutableSearchRecipe = MutableLiveData<UiState<Recipe>>()
    val searchRecipe: LiveData<UiState<Recipe>>
        get() = mutableSearchRecipe

    private val mutableSavedRecipes = MutableLiveData<UiState<List<Result>>>()
    val savedRecipes: LiveData<UiState<List<Result>>>
        get() = mutableSavedRecipes

    private val mutableSavedRecipe = MutableLiveData<UiState<Result?>>()
    val savedRecipe: LiveData<UiState<Result?>>
        get() = mutableSavedRecipe

    private val mutableAddRecipe = MutableLiveData<UiState<String>>()
    val addRecipe: LiveData<UiState<String>>
        get() = mutableAddRecipe

    fun getSavedRecipes() {
        mutableSavedRecipes.postValue(UiState.Loading())
        recipeRepository.getSavedRecipes {
            mutableSavedRecipes.postValue(it)
        }
    }

    fun getSavedRecipe(recipeName: String){
        mutableSavedRecipe.postValue(UiState.Loading())
        recipeRepository.getSavedRecipe(recipeName){
            mutableSavedRecipe.postValue(it)
        }
    }

    fun addRecipe(result: Result){
        recipeRepository.addRecipe(result){
            mutableAddRecipe.postValue(it)
        }
    }

    fun removeRecipe(recipeName: String){
        recipeRepository.removeRecipe(recipeName)
    }

    fun getRecipeForYou(tag: String) = viewModelScope.launch {
        mutableForYouRecipes.postValue(UiState.Loading())
        val response = recipeRepository.getRecipesForHomePage(startIndexForYou, tag)
        mutableForYouRecipes.postValue(handleForYouRecipes(response))
    }

    fun getRecipeTrending() = viewModelScope.launch {
        mutableTrendingRecipes.postValue(UiState.Loading())
        val response = recipeRepository.getRecipesForHomePage(startIndexTrending, "")
        mutableTrendingRecipes.postValue(handleTrendingRecipes(response))
    }

    fun getRecipeDetail(searchQuery: String) = viewModelScope.launch {
        mutableRecipeDetail.postValue(UiState.Loading())
        val response = recipeRepository.getSearchRecipes(1, searchQuery)
        mutableRecipeDetail.postValue(handleRecipeDetail(response))
    }

    fun getSearchRecipe(searchQuery: String) = viewModelScope.launch {
        mutableSearchRecipe.postValue(UiState.Loading())
        val response = recipeRepository.getSearchRecipes(responseSize, searchQuery)
        mutableSearchRecipe.postValue(handleSearchRecipe(response))
    }

    private fun handleForYouRecipes(response: Response<Recipe>): UiState<Recipe>{
        if (response.isSuccessful){
            response.body()?.let { resultResponse ->
                return UiState.Success(resultResponse)
            }
        }
        return UiState.Error(response.message())
    }

    private fun handleTrendingRecipes(response: Response<Recipe>): UiState<Recipe>{
        if (response.isSuccessful){
            response.body()?.let { resultResponse ->
                return UiState.Success(resultResponse)
            }
        }
        return UiState.Error(response.message())
    }

    private fun handleRecipeDetail(response: Response<Recipe>): UiState<Recipe>{
        if (response.isSuccessful){
            response.body()?.let { resultResponse ->
                return UiState.Success(resultResponse)
            }
        }
        return UiState.Error(response.message())
    }

    private fun handleSearchRecipe(response: Response<Recipe>): UiState<Recipe>{
        if (response.isSuccessful){
            response.body()?.let { resultResponse ->
                return UiState.Success(resultResponse)
            }
        }
        return UiState.Error(response.message())
    }

}