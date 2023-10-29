package com.example.recipeapplication.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recipeapplication.databinding.FragmentFavouritesBinding
import com.example.recipeapplication.ui.FragmentManagerActivity
import com.example.recipeapplication.ui.RecipeDetailActivity
import com.example.recipeapplication.ui.RecipeDetailDatabaseActivity
import com.example.recipeapplication.ui.adapters.FavouritesAdapter
import com.example.recipeapplication.util.UiState
import com.example.recipeapplication.viewmodel.RecipeViewModel
import com.google.firebase.database.FirebaseDatabase

class FavouritesFragment: Fragment() {

    private lateinit var binding: FragmentFavouritesBinding
    val TAG = "FavouritesFragment"
    lateinit var viewModel: RecipeViewModel

    val database = FirebaseDatabase.getInstance("https://recipe-application-74a9b-default-rtdb.europe-west1.firebasedatabase.app/")
        .getReference("SavedRecipes")

    val favouritesAdapter by lazy {
        FavouritesAdapter(
            onItemClicked = {pos, recipeName ->
                val intent = Intent(context, RecipeDetailDatabaseActivity::class.java)
                intent.putExtra("recipeName", recipeName)
                startActivity(intent)
            },
            onFabClicked = {pos, recipeName ->
                viewModel.removeRecipe(recipeName)
            }
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavouritesBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as FragmentManagerActivity).viewModel
        setupRecyclerView()

        viewModel.getSavedRecipes()
        viewModel.savedRecipes.observe(viewLifecycleOwner, Observer {recipeResponse ->
            when (recipeResponse){
                is UiState.Success ->{
                    hideProgressBar()
                    recipeResponse.data?.let { recipeList ->
                        favouritesAdapter.differ.submitList(recipeList)
                    }
                }
                is UiState.Error ->{
                    hideProgressBar()
                    recipeResponse.errorMessage?.let { message ->
                        Log.e(TAG, "An error occured: $message")
                    }
                }
                is UiState.Loading ->{
                    showProgressBar()
                }
            }
        })

    }

    private fun hideProgressBar(){
        binding.fixedTVFavourites.visibility = View.VISIBLE
        binding.rvSavedRecipes.visibility = View.VISIBLE
        binding.fixedTvLoading.visibility = View.INVISIBLE
        binding.progressBar.visibility = View.INVISIBLE
    }

    private fun showProgressBar(){
        binding.fixedTVFavourites.visibility = View.VISIBLE
        binding.rvSavedRecipes.visibility = View.INVISIBLE
        binding.fixedTvLoading.visibility = View.VISIBLE
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun setupRecyclerView(){
        binding.rvSavedRecipes.apply {
            adapter = favouritesAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

}