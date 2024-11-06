package com.arunabha.expensetracker

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.Locale

object Utils {
    fun formatDateToHumanReadableForm(dateInMillis: Long): String {
        val dateFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return dateFormatter.format(dateInMillis)
    }

    @SuppressLint("DefaultLocale")
    fun formatTwoDecimalValues(value: Double): String {
        return String.format("%.2f", value)
    }
}