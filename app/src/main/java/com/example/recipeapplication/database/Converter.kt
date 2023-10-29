package com.example.recipeapplication.database

import androidx.room.TypeConverter
import com.example.recipeapplication.model.Credit
import com.example.recipeapplication.model.Instruction

//class Converter {
//
//    @TypeConverter
//    fun fromCredit(credit: Credit): String{
//        return credit.name
//    }
//    @TypeConverter
//    fun toCredit(name: String): Credit{
//        return Credit(name)
//    }
//
//    @TypeConverter
//    fun fromInstruction(instruction: Instruction): Int{
//        return instruction.id
//    }
//    @TypeConverter
//    fun toInstruction(id: Int): Instruction{
//        return Instruction(
//            display_text = "",
//            id,
//            position = 0,
//            temperature = 0
//        )
//    }
//    @TypeConverter
//    fun fromInstructionList(instructions: List<Instruction>): String{
//        return instructions.joinToString(separator = "."){
//            it.id.toString()
//        }
//    }
//    @TypeConverter
//    fun toInstructionList(instructionIds: String): List<Instruction>{
//        return instructionIds.split(".").map {
//            Instruction(id = it.toInt())
//        }
//    }
//
//}