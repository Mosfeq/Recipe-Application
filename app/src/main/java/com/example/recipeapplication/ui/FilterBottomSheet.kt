package com.example.recipeapplication.ui

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.example.recipeapplication.R
import com.example.recipeapplication.databinding.BottomsheetFilterBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class FilterBottomSheet: BottomSheetDialogFragment() {

    private lateinit var binding: BottomsheetFilterBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = BottomsheetFilterBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvPopularity.setOnClickListener {
            binding.tvPopularity.setBackgroundResource(R.drawable.filter_option_selected_border)
            binding.tvPopularity.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
        }

        binding.tv5Star.setOnClickListener {
            binding.tv5Star.setBackgroundResource(R.drawable.filter_option_selected_border)
            binding.tv5Star.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
        }

        binding.tvUnder1Hour.setOnClickListener {
            binding.tvUnder1Hour.setBackgroundResource(R.drawable.filter_option_selected_border)
            binding.tvUnder1Hour.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
        }

    }

}