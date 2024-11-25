package com.arunabha.expensetracker

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.Locale

object Utils {
    // checked omk
    fun formatDateToHumanReadableForm(dateInMillis: Long): String {
        val dateFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return dateFormatter.format(dateInMillis)
    }

    fun convertDateStringToMillis(dateString: String?): Long {
        val dateFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val date =
            dateString?.let { dateFormatter.parse(it) } // Parse the string into a Date object
//        return date?.time ?: throw IllegalArgumentException("Invalid date string: $dateString")
        return date?.time ?: 0L
    }

    @SuppressLint("DefaultLocale")
    fun formatTwoDecimalValues(value: Double): String {
        return String.format("%.2f", value)
    }
}