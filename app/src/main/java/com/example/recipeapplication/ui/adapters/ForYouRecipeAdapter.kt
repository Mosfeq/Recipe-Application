package com.example.recipeapplication.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.recipeapplication.R
import com.example.recipeapplication.databinding.HomeRecipeItemBinding
import com.example.recipeapplication.model.Result

class ForYouRecipeAdapter(
    val onItemClicked: (Int, String) -> Unit,
): RecyclerView.Adapter<ForYouRecipeAdapter.ForYouViewHolder>() {

    private val diffCallBack = object : DiffUtil.ItemCallback<Result>(){
        override fun areItemsTheSame(oldItem: Result, newItem: Result): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Result, newItem: Result): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffCallBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForYouViewHolder {
        return ForYouViewHolder(
            HomeRecipeItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ))
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: ForYouViewHolder, position: Int) {
        val recipe = differ.currentList[position]
        holder.bind(recipe)
    }

    inner class ForYouViewHolder(val binding: HomeRecipeItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(recipe: Result){
            Glide.with(binding.itemRecipeImage)
                .load(recipe.thumbnail_url)
                .placeholder(R.drawable.no_image)
                .override(290, 190)
                .into(binding.itemRecipeImage)
            binding.itemRecipeName.text = recipe.name
            binding.itemRecipeServings.text = recipe.num_servings.toString()
            val score = (recipe.user_ratings?.score?.div(2))?.times(10)
            binding.itemRecipeRating.text = "%.1f".format(score)
            binding.recipeItem.setOnClickListener{
                recipe.name?.let { it1 -> onItemClicked.invoke(adapterPosition, it1) }
            }
        }
    }

}