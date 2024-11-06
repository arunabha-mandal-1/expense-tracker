package com.arunabha.expensetracker.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transaction_table") // Generates a table name from the class name
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int?,
    val title: String,
    val amount: Double,
    val date: String,
    val category: String,
    val type: String
)
