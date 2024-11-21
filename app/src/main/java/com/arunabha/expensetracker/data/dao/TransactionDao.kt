package com.arunabha.expensetracker.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.arunabha.expensetracker.data.model.TransactionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {

    // Get all transactions
    @Query("SELECT * FROM transaction_table")
    fun getAllTransactions(): Flow<List<TransactionEntity>>

    // Delete all the transactions
    @Query("DELETE FROM transaction_table")
    suspend fun deleteAllTransactions()


    // Add a transaction
    @Insert
    suspend fun addTransaction(transaction: TransactionEntity)

    // Delete a transaction
    @Delete
    suspend fun deleteTransaction(transaction: TransactionEntity)

    // Update a transaction
    @Update
    suspend fun updateTransaction(transaction: TransactionEntity)

}