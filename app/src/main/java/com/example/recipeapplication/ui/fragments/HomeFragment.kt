package com.example.recipeapplication.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recipeapplication.databinding.FragmentHomeBinding
import com.example.recipeapplication.ui.FragmentManagerActivity
import com.example.recipeapplication.ui.RecipeDetailActivity
import com.example.recipeapplication.ui.adapters.ForYouRecipeAdapter
import com.example.recipeapplication.ui.adapters.RecipeAdapter
import com.example.recipeapplication.util.UiState
import com.example.recipeapplication.viewmodel.RecipeViewModel

class HomeFragment: Fragment() {

    private lateinit var binding: FragmentHomeBinding
    val TAG = "HomeFragment"

    lateinit var viewModel: RecipeViewModel
    val forYouAdapter by lazy {
        ForYouRecipeAdapter(
            onItemClicked = {pos, recipeName, ->
                val intent = Intent(context, RecipeDetailActivity::class.java)
                intent.putExtra("recipeName", recipeName)
                startActivity(intent)
            }
        )
    }

    val trendingAdapter by lazy {
        RecipeAdapter(
            onItemClicked = {pos, recipeName, ->
                val intent = Intent(context, RecipeDetailActivity::class.java)
                intent.putExtra("recipeName", recipeName)
                startActivity(intent)
            }
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as FragmentManagerActivity).viewModel
        setupForYouRecyclerView()
        setupTrendingRecyclerView()

//        binding.tvSearch.setOnClickListener{
//            val intent = Intent(context, SearchFragment::class.java)
//            startActivity(intent)
//        }

        viewModel.getRecipeForYou("under_30_minutes")
        viewModel.getRecipeTrending()

        viewModel.forYouRecipes.observe(viewLifecycleOwner, Observer { response ->
            when(response){
                is UiState.Success ->{
                    hideProgressBar()
                    response.data?.let { recipeResponse ->
                        forYouAdapter.differ.submitList(recipeResponse.results)
                    }
                }
                is UiState.Error ->{
                    hideProgressBar()
                    response.errorMessage?.let { message ->
                        Log.e(TAG, "An error occured: $message")
                    }
                }
                is UiState.Loading ->{
                    showProgressBar()
                }
            }
        })

        viewModel.trendingRecipes.observe(viewLifecycleOwner, Observer { response ->
            when(response){
                is UiState.Success ->{
                    hideProgressBar()
                    response.data?.let { recipeResponse ->
                        trendingAdapter.differ.submitList(recipeResponse.results)
                    }
                }
                is UiState.Error ->{
                    hideProgressBar()
                    response.errorMessage?.let { message ->
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
        binding.progressBar.visibility = View.INVISIBLE
        binding.fixedTvLoading.visibility = View.INVISIBLE
        binding.fixedTvGreeting.visibility = View.VISIBLE
        binding.fixedTvRecipesForYou.visibility = View.VISIBLE
        binding.rvRecipes.visibility = View.VISIBLE
        binding.fixedTvTrendingRecipes.visibility = View.VISIBLE
        binding.rvTrendingRecipes.visibility = View.VISIBLE
        binding.ivMixingIcon.visibility = View.VISIBLE
        binding.ivFireIcon.visibility = View.VISIBLE
    }

    private fun showProgressBar(){
        binding.progressBar.visibility = View.VISIBLE
        binding.fixedTvLoading.visibility = View.VISIBLE
        binding.fixedTvGreeting.visibility = View.INVISIBLE
        binding.fixedTvRecipesForYou.visibility = View.INVISIBLE
        binding.rvRecipes.visibility = View.INVISIBLE
        binding.fixedTvTrendingRecipes.visibility = View.INVISIBLE
        binding.rvTrendingRecipes.visibility = View.INVISIBLE
        binding.ivMixingIcon.visibility = View.INVISIBLE
        binding.ivFireIcon.visibility = View.INVISIBLE
    }

    private fun setupForYouRecyclerView(){
        binding.rvRecipes.apply {
            adapter = forYouAdapter
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        }
    }

    private fun setupTrendingRecyclerView(){
        binding.rvTrendingRecipes.apply {
            adapter = trendingAdapter
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        }
    }

}