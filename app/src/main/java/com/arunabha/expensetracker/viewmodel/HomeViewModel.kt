package com.arunabha.expensetracker.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.arunabha.expensetracker.Utils
import com.arunabha.expensetracker.data.TransactionDatabase
import com.arunabha.expensetracker.data.dao.TransactionDao
import com.arunabha.expensetracker.data.model.TransactionEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class HomeViewModel(private val dao: TransactionDao) : ViewModel() {
    val transactions = dao.getAllTransactions().map {transactionList ->
        transactionList.sortedByDescending { it.date }
    }

    // Get total balance
    fun getBalance(list: List<TransactionEntity>): String {
        var total = 0.00
        list.forEach {
            if (it.type == "Income") {
                total += it.amount
            } else {
                total -= it.amount
            }
        }
        return "$ ${Utils.formatTwoDecimalValues(total)}"
    }

    // Get total income
    fun getTotalIncome(list: List<TransactionEntity>): String {
        var total = 0.00
        list.forEach {
            if (it.type == "Income") {
                total += it.amount
            }
        }
        return "$ ${Utils.formatTwoDecimalValues(total)}"
    }

    // Get total expense
    fun getTotalExpense(list: List<TransactionEntity>): String {
        var total = 0.00
        list.forEach {
            if (it.type == "Expense") {
                total += it.amount
            }
        }
        return "$ ${Utils.formatTwoDecimalValues(total)}"
    }

    // Reset everything
    suspend fun deleteTransactions(): Boolean {
        return try {
            dao.deleteAllTransactions()
            true
        } catch (e: Throwable) {
            false
        }
    }


    fun getItemIcon() {
        // Get icon based on type
        // Return icon
    }
    // Later we can add more functionalities like reset after a certain time etc.
}


class HomeViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            val dao = TransactionDatabase.getDatabase(context).transactionDao()
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(dao) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}