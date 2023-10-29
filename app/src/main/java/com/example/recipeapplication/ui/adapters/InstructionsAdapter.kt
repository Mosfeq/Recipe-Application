package com.example.recipeapplication.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.recipeapplication.databinding.IngredientItemBinding
import com.example.recipeapplication.databinding.InstructionItemBinding
import com.example.recipeapplication.model.Component
import com.example.recipeapplication.model.Instruction
import com.example.recipeapplication.model.Result

class InstructionsAdapter: RecyclerView.Adapter<InstructionsAdapter.InstructionsViewHolder>() {

    private val diffCallBack = object : DiffUtil.ItemCallback<Instruction>(){
        override fun areItemsTheSame(oldItem: Instruction, newItem: Instruction): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Instruction, newItem: Instruction): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffCallBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InstructionsViewHolder {
        return InstructionsViewHolder(InstructionItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ))
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: InstructionsViewHolder, position: Int) {
        val instruction = differ.currentList[position]
        holder.bind(instruction)
    }

    inner class InstructionsViewHolder(val instructionsBinding: InstructionItemBinding): RecyclerView.ViewHolder(instructionsBinding.root){
        fun bind (instruction: Instruction) {
            instructionsBinding.itemInstruction.text = instruction.display_text
        }
    }
}