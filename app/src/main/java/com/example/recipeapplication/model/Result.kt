package com.example.recipeapplication.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

//@Entity(tableName = "results_table")
data class Result(
    val cook_time_minutes: Int? = null,
    val credits: List<Credit>? = null,
    val description: String? = null,
//    @PrimaryKey(autoGenerate = false)
    val id: Int? = null,
    val instructions: List<Instruction>? = null,
    val keywords: String? = null,
    val language: String? = null,
    val name: String? = null,
    val num_servings: Int? = null,
    val nutrition: Nutrition? = null,
    val prep_time_minutes: Int? = null,
    val sections: List<Section>? = null,
    val show: ShowX? = null,
    val tags: List<Tag>? = null,
    val thumbnail_url: String? = null,
    val total_time_minutes: Int? = 0,
    val user_ratings: UserRatings? = null,
): Serializable