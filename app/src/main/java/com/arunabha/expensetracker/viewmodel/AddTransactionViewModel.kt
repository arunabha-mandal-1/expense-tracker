package com.arunabha.expensetracker.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.arunabha.expensetracker.data.TransactionDatabase
import com.arunabha.expensetracker.data.dao.TransactionDao
import com.arunabha.expensetracker.data.model.TransactionEntity

class AddTransactionViewModel(private val dao: TransactionDao): ViewModel() {
    suspend fun addTransaction(transactionEntity: TransactionEntity): Boolean{
        return try {
            dao.addTransaction(transactionEntity)
            true
        }catch (e: Throwable){
            false
        }
    }
}

class AddTransactionViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(AddTransactionViewModel::class.java)){
            val dao = TransactionDatabase.getDatabase(context).transactionDao()
            @Suppress("UNCHECKED_CAST")
            return AddTransactionViewModel(dao) as T
        }else{
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}