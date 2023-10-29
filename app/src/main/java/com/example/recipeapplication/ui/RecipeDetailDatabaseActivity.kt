package com.example.recipeapplication.ui

import  android.app.Dialog
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.recipeapplication.R
import com.example.recipeapplication.database.FirebaseDAO
import com.example.recipeapplication.databinding.ActivityRecipeDetailBinding
import com.example.recipeapplication.databinding.ActivityRecipeDetailDatabaseBinding
import com.example.recipeapplication.databinding.DialogDescriptionBinding
import com.example.recipeapplication.databinding.DialogDetailsBinding
import com.example.recipeapplication.databinding.DialogRatingBinding
import com.example.recipeapplication.model.Component
import com.example.recipeapplication.model.Instruction
import com.example.recipeapplication.repository.RecipeRepository
import com.example.recipeapplication.ui.adapters.IngredientsAdapter
import com.example.recipeapplication.ui.adapters.InstructionsAdapter
import com.example.recipeapplication.util.UiState
import com.example.recipeapplication.viewmodel.RecipeViewModel
import com.example.recipeapplication.viewmodel.RecipeViewModelFactory
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class RecipeDetailDatabaseActivity: AppCompatActivity() {

    val TAG = "RecipeDetailDatabaseActivity"
    private lateinit var binding: ActivityRecipeDetailDatabaseBinding
    lateinit var viewModel: RecipeViewModel
    private var imageLiked = false

    lateinit var ingredientsAdapter: IngredientsAdapter
    lateinit var instructionsAdapter: InstructionsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecipeDetailDatabaseBinding.inflate(layoutInflater)
        val recipeRepository = RecipeRepository.getInstance(FirebaseDAO.getInstance())
        val viewModelFactory = RecipeViewModelFactory(recipeRepository)
        viewModel = ViewModelProvider(this, viewModelFactory)[RecipeViewModel::class.java]
        setContentView(binding.root)
        setupIngredientsRecyclerView()

        val recipeNameRetrieved = intent.getStringExtra("recipeName")

        recipeNameRetrieved?.let { recipeName ->
            viewModel.getSavedRecipe(recipeName)
        }

        viewModel.addRecipe.observe(this, Observer {
            when(it){
                is UiState.Loading ->{

                }
                is UiState.Success ->{
                    Toast.makeText(this, it.data, Toast.LENGTH_SHORT).show()
                }
                is UiState.Error ->{
                    Toast.makeText(this, it.errorMessage, Toast.LENGTH_SHORT).show()
                }
            }
        })

        viewModel.savedRecipe.observe(this, Observer { response ->
            when (response){
                is UiState.Success ->{
                    hideProgressBar()
                    response.data?.let { recipe ->

                        ingredientsAdapter.differ.submitList(recipe.sections?.first()?.components)

//                        val zoomIn = AnimationUtils.loadAnimation(this, R.anim.zoom_in)
//                        val zoomOut = AnimationUtils.loadAnimation(this, R.anim.zoom_out)

//                        binding.ivDetailRecipeImage.setOnClickListener(object : DoubleClickListener(){
//                            override fun onDoubleClick(v: View?) {
//                                binding.ivFavourite.startAnimation(zoomIn)
//                                binding.ivFavourite.startAnimation(zoomOut)
//                                binding.ivFavouriteButton.setImageResource(R.drawable.baseline_favorite_24)
//                                imageLiked = true
//                                viewModel.addRecipe(recipe)
//                            }
//                        })

//                        binding.fabFavourites.setOnClickListener{
//                            if (imageLiked){
//                                binding.fabFavourites.setImageResource(R.drawable.baseline_heart_broken_24)
//                                imageLiked = false
//                                Log.e(TAG, "Liked = $imageLiked")
//                                recipe.name?.let { name -> viewModel.removeRecipe(name) }
//                            } else{
//                                binding.fabFavourites.setImageResource(R.drawable.baseline_favorite_24)
//                                viewModel.addRecipe(recipe)
//                                imageLiked = true
//                                Log.e(TAG, "Liked = $imageLiked")
//                            }
//                        }

                        binding.fabToInstructions.setOnClickListener {
                            binding.rvIngredients.adapter = null
                            binding.rvIngredients.visibility = View.GONE
                            binding.rvInstructions.visibility = View.VISIBLE
                            binding.fixedTvIngredients.visibility = View.INVISIBLE
                            binding.fabToInstructions.visibility = View.INVISIBLE
                            binding.fixedTvInstructions.visibility = View.VISIBLE
                            binding.fabToIngredients.visibility = View.VISIBLE
                            binding.fixedTvDot.visibility = View.INVISIBLE
                            binding.tvRecipeIngredients.visibility = View.INVISIBLE
                            binding.fixedTvItems.visibility = View.INVISIBLE
                            setupInstructionsRecyclerView()
                            instructionsAdapter.differ.submitList(recipe.instructions)
                        }

                        binding.fabToIngredients.setOnClickListener {
                            binding.rvInstructions.adapter = null
                            binding.rvInstructions.visibility = View.GONE
                            binding.rvIngredients.visibility = View.VISIBLE
                            binding.fixedTvInstructions.visibility = View.INVISIBLE
                            binding.fabToIngredients.visibility = View.INVISIBLE
                            binding.fixedTvIngredients.visibility = View.VISIBLE
                            binding.fabToInstructions.visibility = View.VISIBLE
                            binding.fixedTvDot.visibility = View.VISIBLE
                            binding.tvRecipeIngredients.visibility = View.VISIBLE
                            binding.fixedTvItems.visibility = View.VISIBLE
                            setupIngredientsRecyclerView()
                            ingredientsAdapter.differ.submitList(recipe.sections?.first()?.components)
                        }

                        Glide.with(binding.ivDetailRecipeImage)
                            .load(recipe.thumbnail_url)
                            .placeholder(R.drawable.no_image)
                            .into(binding.ivDetailRecipeImage)
                        val score = (recipe.user_ratings?.score?.div(2))?.times(10)
                        binding.tvRecipeRating.text = "%.1f".format(score)
                        binding.tvRecipeAuthor.text = recipe.credits?.first()?.name
                        binding.tvRecipeCompany.text = recipe.show?.name
                        binding.tvRecipeTime.text = recipe.total_time_minutes.toString()
                        binding.tvRecipeServings.text = recipe.num_servings.toString()
                        binding.tvRecipeDescription.text = recipe.description
                        binding.tvRecipeIngredients.text = recipe.sections?.first()?.components?.size.toString()

                        if (recipe.description == null){
                            binding.tvRecipeDescription.text == " "
                        }

                        if (binding.tvRecipeDescription.lineCount > 6){
                            binding.tvRecipeDescription.setOnClickListener {
                                val dialogDescription  = Dialog(this)
                                val dialogDescriptionBinding = DialogDescriptionBinding.inflate(layoutInflater)
                                dialogDescription.setContentView(dialogDescriptionBinding.root)

                                dialogDescriptionBinding.dialogDescription.text = recipe.description

                                dialogDescription.show()
                            }
                        }

                        binding.llRating.setOnClickListener{
                            val dialogRating = Dialog(this)
                            val dialogRatingBinding = DialogRatingBinding.inflate(layoutInflater)
                            dialogRating.setContentView(dialogRatingBinding.root)

                            dialogRating.show()
                        }

                        binding.tvRecipeNutrition.setOnClickListener {
                            val dialogDetails = Dialog(this)
                            val dialogDetailsBinding = DialogDetailsBinding.inflate(layoutInflater)
                            dialogDetails.setContentView(dialogDetailsBinding.root)

                            dialogDetailsBinding.dialogCalories.text = recipe.nutrition?.calories.toString()
                            dialogDetailsBinding.dialogFat.text = recipe.nutrition?.fat.toString()
                            dialogDetailsBinding.dialogCarbohydrates.text = recipe.nutrition?.carbohydrates.toString()
                            dialogDetailsBinding.dialogSugar.text = recipe.nutrition?.sugar.toString()
                            dialogDetailsBinding.dialogFiber.text = recipe.nutrition?.fiber.toString()
                            dialogDetailsBinding.dialogProtein.text = recipe.nutrition?.protein.toString()

                            dialogDetails.show()
                        }
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

    abstract class DoubleClickListener: View.OnClickListener{
        private var lastClickTime: Long = 0

        companion object{
            private const val DOUBLE_CLICK_TIME_DELTA = 300
        }

        override fun onClick(v: View?) {
            val clickTime = System.currentTimeMillis()
            if (clickTime - lastClickTime < DOUBLE_CLICK_TIME_DELTA){
                onDoubleClick(v)
            }
            lastClickTime = clickTime
        }

        abstract fun onDoubleClick(v: View?)

    }


    private fun setupIngredientsRecyclerView(){
        ingredientsAdapter = IngredientsAdapter()
        binding.rvIngredients.apply {
            adapter = ingredientsAdapter
            layoutManager = LinearLayoutManager(this@RecipeDetailDatabaseActivity)
        }
    }

    private fun setupInstructionsRecyclerView(){
        instructionsAdapter = InstructionsAdapter()
        binding.rvInstructions.apply {
            adapter = instructionsAdapter
            layoutManager = LinearLayoutManager(this@RecipeDetailDatabaseActivity)
        }
    }

    private fun hideProgressBar(){
        binding.progressBar.visibility = View.INVISIBLE
        binding.fixedTvLoading.visibility = View.INVISIBLE
        binding.ivDetailRecipeImage.visibility = View.VISIBLE
        binding.tvRecipeNutrition.visibility = View.VISIBLE
        binding.tvRecipeRating.visibility = View.VISIBLE
        binding.tvRecipeAuthor.visibility = View.VISIBLE
        binding.tvRecipeCompany.visibility = View.VISIBLE
        binding.fixedTvTime.visibility = View.VISIBLE
        binding.tvRecipeTime.visibility = View.VISIBLE
        binding.fixedTvMinutes.visibility = View.VISIBLE
        binding.innerConstrainedLayout.visibility = View.VISIBLE
        binding.fixedTvIngredients.visibility = View.VISIBLE
        binding.fixedTvInstructions.visibility = View.INVISIBLE
        binding.fabToInstructions.visibility = View.VISIBLE
        binding.fabToIngredients.visibility = View.INVISIBLE
        binding.rvIngredients.visibility = View.VISIBLE
        binding.rvInstructions.visibility = View.GONE
        binding.ivFavourite.visibility = View.INVISIBLE
        binding.tvRecipeDescription.visibility = View.VISIBLE
        binding.fixedTvDot.visibility = View.VISIBLE
        binding.tvRecipeIngredients.visibility = View.VISIBLE
        binding.fixedTvItems.visibility = View.VISIBLE
        binding.fixedTvStar.visibility = View.VISIBLE
        binding.fixedTvServings.visibility = View.VISIBLE
        binding.tvRecipeServings.visibility = View.VISIBLE
//        binding.fixedTvTutorial.visibility = View.VISIBLE
    }

    private fun showProgressBar(){
        binding.progressBar.visibility = View.VISIBLE
        binding.fixedTvLoading.visibility = View.VISIBLE
        binding.ivDetailRecipeImage.visibility = View.INVISIBLE
        binding.tvRecipeNutrition.visibility = View.INVISIBLE
        binding.tvRecipeRating.visibility = View.INVISIBLE
        binding.tvRecipeAuthor.visibility = View.INVISIBLE
        binding.tvRecipeCompany.visibility = View.INVISIBLE
        binding.fixedTvTime.visibility = View.INVISIBLE
        binding.tvRecipeTime.visibility = View.INVISIBLE
        binding.fixedTvMinutes.visibility = View.INVISIBLE
        binding.innerConstrainedLayout.visibility = View.INVISIBLE
        binding.fixedTvIngredients.visibility = View.INVISIBLE
        binding.fixedTvInstructions.visibility = View.INVISIBLE
        binding.fabToInstructions.visibility = View.INVISIBLE
        binding.fabToIngredients.visibility = View.INVISIBLE
        binding.rvIngredients.visibility = View.INVISIBLE
        binding.rvInstructions.visibility = View.GONE
        binding.ivFavourite.visibility = View.INVISIBLE
        binding.tvRecipeDescription.visibility = View.INVISIBLE
        binding.fixedTvDot.visibility = View.INVISIBLE
        binding.tvRecipeIngredients.visibility = View.INVISIBLE
        binding.fixedTvItems.visibility = View.INVISIBLE
        binding.fixedTvStar.visibility = View.INVISIBLE
        binding.fixedTvServings.visibility = View.INVISIBLE
        binding.tvRecipeServings.visibility = View.INVISIBLE
//        binding.fixedTvTutorial.visibility = View.INVISIBLE
    }

}