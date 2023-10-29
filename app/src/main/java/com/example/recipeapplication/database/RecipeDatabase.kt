package com.example.recipeapplication.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.recipeapplication.model.Result

//@Database(entities = [Result::class], version = 1)
//@TypeConverters(Converter::class)
//abstract class RecipeDatabase: RoomDatabase() {
//
//    abstract fun getRecipeDao(): RecipeDao
//
//    companion object{
//        @Volatile
//        private var instance: RecipeDatabase? = null
//
//        //When this class is initialised, this invoke function will be executed
//        operator fun invoke(context: Context) = instance ?: synchronized(this){
//            instance ?: createDatabase(context).also{ instance = it}
//        }
//
//        private fun createDatabase(context: Context) =
//            Room.databaseBuilder(
//                context.applicationContext,
//                RecipeDatabase::class.java,
//                "recipes_db.db"
//            ).build()
//    }
//
//}