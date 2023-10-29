package com.example.recipeapplication.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.recipeapplication.databinding.IngredientItemBinding
import com.example.recipeapplication.databinding.InstructionItemBinding
import com.example.recipeapplication.model.Component
import com.example.recipeapplication.model.Result

class IngredientsAdapter: RecyclerView.Adapter<IngredientsAdapter.IngredientsViewHolder>() {

    private val diffCallBack = object : DiffUtil.ItemCallback<Component>(){
        override fun areItemsTheSame(oldItem: Component, newItem: Component): Boolean {
            return oldItem.raw_text == newItem.raw_text
        }

        override fun areContentsTheSame(oldItem: Component, newItem: Component): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffCallBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientsViewHolder {
        return IngredientsViewHolder(IngredientItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ))
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: IngredientsViewHolder, position: Int) {
        val ingredient = differ.currentList[position]
        holder.bind(ingredient)
    }

    inner class IngredientsViewHolder(val ingredientsBinding: IngredientItemBinding): RecyclerView.ViewHolder(ingredientsBinding.root){
        fun bind (ingredient: Component) {
            ingredientsBinding.itemIngredient.text = ingredient.raw_text
        }
    }
}