package com.example.recipeapplication.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.recipeapplication.databinding.ActivityAddRecipeBinding

class AddRecipeActivity: AppCompatActivity() {

    private lateinit var binding: ActivityAddRecipeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddRecipeBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

}