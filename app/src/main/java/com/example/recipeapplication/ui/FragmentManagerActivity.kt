package com.example.recipeapplication.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.recipeapplication.R
import com.example.recipeapplication.database.FirebaseDAO
import com.example.recipeapplication.databinding.ActivityFragmentManagerBinding
import com.example.recipeapplication.repository.RecipeRepository
import com.example.recipeapplication.viewmodel.RecipeViewModel
import com.example.recipeapplication.viewmodel.RecipeViewModelFactory

class FragmentManagerActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var binding: ActivityFragmentManagerBinding

    lateinit var viewModel: RecipeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFragmentManagerBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        binding.navBar.background = null
//        binding.navBar.menu.getItem(1).isEnabled = false

        val recipeRepository = RecipeRepository.getInstance(FirebaseDAO.getInstance())
        val viewModelFactory = RecipeViewModelFactory(recipeRepository)
        viewModel = ViewModelProvider(this, viewModelFactory)[RecipeViewModel::class.java]

//        binding.fabRecipeList.setOnClickListener{
//            val intent = Intent(this, AddRecipeActivity::class.java)
//            startActivity(intent)
//        }

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        navController = navHostFragment.findNavController()
        binding.navBar.setupWithNavController(navController)

    }
}