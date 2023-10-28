package com.globant.imdb.core

import java.text.SimpleDateFormat
import java.util.Locale

class TextTransforms {
    companion object {
        fun createDescription(tagline:String, date:String): String{
            val formattedDate = convertDate(date)
            return "$tagline $formattedDate"
        }

        private fun convertDate(date:String):String {
            lateinit var result:String
            val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val outputFormat = SimpleDateFormat("MMMM yyyy", Locale.getDefault())
            try {
                val dateObject = inputFormat.parse(date)!!
                result = outputFormat.format(dateObject)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return result
        }
    }
}