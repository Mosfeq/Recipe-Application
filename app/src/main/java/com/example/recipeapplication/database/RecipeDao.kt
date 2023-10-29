package com.example.recipeapplication.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.recipeapplication.model.Result

//@Dao
//interface RecipeDao {
//
//    @Query("SELECT * FROM results_table")
//    fun getSavedRecipes(): LiveData<List<Result>>
//
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun saveRecipe(result: Result)
//
//    @Delete
//    suspend fun deleteRecipe(result: Result)
//
//}