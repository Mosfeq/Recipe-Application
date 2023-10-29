package com.example.recipeapplication.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.recipeapplication.R
import com.example.recipeapplication.databinding.RecipeItemBinding
import com.example.recipeapplication.model.Result

class RecipeAdapter(
    val onItemClicked: (Int, String) -> Unit,
): RecyclerView.Adapter<RecipeAdapter.ResultViewHolder>() {

    private val diffCallBack = object : DiffUtil.ItemCallback<Result>(){
        override fun areItemsTheSame(oldItem: Result, newItem: Result): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Result, newItem: Result): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffCallBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultViewHolder {
        return ResultViewHolder(RecipeItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ))
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: ResultViewHolder, position: Int) {
        val recipe = differ.currentList[position]
        holder.bind(recipe)
    }

    inner class ResultViewHolder(val binding: RecipeItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(recipe: Result){
            Glide.with(binding.itemRecipeImage)
                .load(recipe.thumbnail_url)
                .placeholder(R.drawable.no_image)
                .override(140, 130)
                .into(binding.itemRecipeImage)
            binding.itemRecipeName.text = recipe.name
            binding.itemRecipeCalories.text = recipe.nutrition?.calories.toString()
            binding.itemRecipeAuthor.text = recipe.credits?.first()?.name
            binding.recipeItem.setOnClickListener{
                recipe.name?.let { it1 -> onItemClicked.invoke(adapterPosition, it1) }
            }
        }
    }

}