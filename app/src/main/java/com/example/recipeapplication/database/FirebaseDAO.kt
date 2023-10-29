package com.example.recipeapplication.database

import com.example.recipeapplication.model.Recipe
import com.example.recipeapplication.model.Result
import com.example.recipeapplication.util.UiState
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class FirebaseDAO {

    var recipeItem: Result? = null
    val database = FirebaseDatabase.getInstance("https://recipe-application-74a9b-default-rtdb.europe-west1.firebasedatabase.app/")
        .getReference("SavedRecipes")

    fun getSavedRecipes(result: (UiState<List<Result>>) -> Unit){
        database.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val recipeList = arrayListOf<Result>()
                for (document in snapshot.children){
                    recipeItem = document.getValue(Result::class.java)
                    if (recipeItem != null){
                        recipeList.add(recipeItem!!)
                    }
                }
                result.invoke(
                    UiState.Success(recipeList)
                )
            }
            override fun onCancelled(error: DatabaseError) {
                result.invoke(
                    UiState.Error("Cannot Retrieve Recipes")
                )
            }
        })
    }

    fun getSavedRecipe(recipeName: String ,result: (UiState<Result?>) -> Unit){
        database.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                var recipe: Result? = null
                for (document in snapshot.children){
                    recipeItem = document.getValue(Result::class.java)
                    if (recipeItem?.name == recipeName){
                        recipe = recipeItem
                    }
                }
                result.invoke(
                    UiState.Success(recipe)
                )
            }
            override fun onCancelled(error: DatabaseError) {
                result.invoke(
                    UiState.Error("Cannot Retrieve Recipe")
                )
            }
        })
    }

    fun addRecipe(result: Result, response: (UiState<String>) -> Unit){
        result.name?.let {
            database.child(it).setValue(result)
                .addOnSuccessListener{
                    response.invoke(
                        UiState.Success("Recipe Saved")
                    )
                }
                .addOnFailureListener {error ->
                    error.localizedMessage?.let{message ->
                        response.invoke(
                            UiState.Error(message)
                        )
                    }
                }
        }
    }

    fun removeRecipe(recipeName: String){
        database.child(recipeName).removeValue()
    }

    companion object{
        @Volatile private var instance: FirebaseDAO? = null

        fun getInstance() = instance ?: synchronized(this){
            instance ?: FirebaseDAO().also {
                instance = it
            }
        }
    }

}