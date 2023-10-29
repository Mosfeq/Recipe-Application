package com.example.recipeapplication.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.recipeapplication.R
import com.example.recipeapplication.databinding.SearchItemBinding
import com.example.recipeapplication.model.Result

class SearchAdapter(
    val onItemClicked: (Int, String) -> Unit,
): RecyclerView.Adapter<SearchAdapter.SearchViewHolder>() {

    private val diffCallBack = object : DiffUtil.ItemCallback<Result>(){
        override fun areItemsTheSame(oldItem: Result, newItem: Result): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Result, newItem: Result): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffCallBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        return SearchViewHolder(
            SearchItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ))
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val recipe = differ.currentList[position]
        holder.bind(recipe)
    }

    inner class SearchViewHolder(val binding: SearchItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(recipe: Result){
            Glide.with(binding.itemRecipeImage)
                .load(recipe.thumbnail_url)
                .placeholder(R.drawable.no_image)
                .into(binding.itemRecipeImage)
            binding.itemRecipeName.text = recipe.name
            val score = (recipe.user_ratings?.score?.div(2))?.times(10)
            binding.itemRecipeRating.text = "%.1f".format(score)
            binding.itemRecipeCalories.text = recipe.nutrition?.calories.toString()
            binding.itemRecipeAuthor.text = recipe.credits?.first()?.name
            binding.itemRecipeTime.text = recipe.cook_time_minutes.toString()
            binding.recipeItem.setOnClickListener{
                recipe.name?.let { it1 -> onItemClicked.invoke(adapterPosition, it1) }
            }
        }
    }

}