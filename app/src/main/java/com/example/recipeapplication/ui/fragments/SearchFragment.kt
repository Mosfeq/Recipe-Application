package com.example.recipeapplication.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recipeapplication.database.FirebaseDAO
import com.example.recipeapplication.databinding.FragmentSearchBinding
import com.example.recipeapplication.repository.RecipeRepository
import com.example.recipeapplication.ui.FilterBottomSheet
import com.example.recipeapplication.ui.FragmentManagerActivity
import com.example.recipeapplication.ui.RecipeDetailActivity
import com.example.recipeapplication.ui.adapters.RecipeAdapter
import com.example.recipeapplication.ui.adapters.SearchAdapter
import com.example.recipeapplication.util.Constants.Companion.DELAY_AFTER_SEARCH
import com.example.recipeapplication.util.UiState
import com.example.recipeapplication.viewmodel.RecipeViewModel
import com.example.recipeapplication.viewmodel.RecipeViewModelFactory
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchFragment: Fragment() {

    private lateinit var binding: FragmentSearchBinding
    val TAG = "SearchActivity"

    lateinit var viewModel: RecipeViewModel
    val searchAdapter by lazy {
        SearchAdapter(
            onItemClicked = {pos, recipeName ->
                val intent = Intent(context, RecipeDetailActivity::class.java)
                intent.putExtra("recipeName", recipeName)
                startActivity(intent)
            }
        )
    }

    lateinit var filterBtmSheet: FilterBottomSheet

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as FragmentManagerActivity).viewModel
        setupRecyclerView()

        binding.ivFilterIcon.setOnClickListener{
            filterBtmSheet = FilterBottomSheet()
            filterBtmSheet.show(childFragmentManager, "BottomSheet")
        }

        var job: Job? = null
        binding.etSearch.addTextChangedListener { editable ->
            job?.cancel()
            job = MainScope().launch {
                delay(DELAY_AFTER_SEARCH)
                editable?.let {
                    if (editable.toString().isNotEmpty()){
                        binding.rvRecipes.visibility = View.VISIBLE
                        viewModel.getSearchRecipe(editable.toString())
                    }else if (editable.toString().isEmpty()){
                        binding.rvRecipes.visibility = View.INVISIBLE
                    }
                }
            }
        }

        viewModel.searchRecipe.observe(viewLifecycleOwner, Observer { response ->
            when(response){
                is UiState.Success ->{
                    hideProgressBar()
                    response.data?.let { recipeResponse ->
                        searchAdapter.differ.submitList(recipeResponse.results)
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
        binding.rvRecipes.visibility = View.VISIBLE
    }

    private fun showProgressBar(){
        binding.progressBar.visibility = View.VISIBLE
        binding.fixedTvLoading.visibility = View.VISIBLE
        binding.rvRecipes.visibility = View.INVISIBLE
    }

    private fun setupRecyclerView(){
        binding.rvRecipes.apply {
            adapter = searchAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

}