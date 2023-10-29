package com.example.recipeapplication.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.recipeapplication.R
import com.example.recipeapplication.databinding.SavedRecipeItemBinding
import com.example.recipeapplication.model.Result

class FavouritesAdapter(
    val onItemClicked: (Int, String) -> Unit,
    val onFabClicked: (Int, String) -> Unit
): RecyclerView.Adapter<FavouritesAdapter.FavouritesViewHolder>() {

    private val diffCallBack = object : DiffUtil.ItemCallback<Result>(){
        override fun areItemsTheSame(oldItem: Result, newItem: Result): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Result, newItem: Result): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffCallBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouritesViewHolder {
        return FavouritesViewHolder(
            SavedRecipeItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ))
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: FavouritesViewHolder, position: Int) {
        val recipe = differ.currentList[position]
        holder.bind(recipe)
    }

    inner class FavouritesViewHolder(val binding: SavedRecipeItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(recipe: Result){
            Glide.with(binding.itemRecipeImage)
                .load(recipe.thumbnail_url)
                .placeholder(R.drawable.no_image)
                .into(binding.itemRecipeImage)
            binding.itemRecipeName.text = recipe.name
            if (recipe.num_servings == 1){
                binding.itemRecipeServings.text = "Person"
            }
            binding.itemRecipeServings.text = recipe.num_servings.toString()
            val score = (recipe.user_ratings?.score?.div(2))?.times(10)
            binding.itemRecipeRating.text = "%.1f".format(score)
            binding.itemRecipeCalories.text = recipe.nutrition?.calories.toString()
            binding.itemRecipeTime.text = recipe.total_time_minutes.toString()
            binding.itemRecipeAuthor.text = recipe.credits?.first()?.name
            binding.fabRemoveRecipe.setOnClickListener {
                recipe.name?.let { name -> onFabClicked.invoke(adapterPosition, name) }
            }
            binding.recipeItem.setOnClickListener{
                recipe.name?.let { name -> onItemClicked.invoke(adapterPosition, name) }
            }
        }
    }

}