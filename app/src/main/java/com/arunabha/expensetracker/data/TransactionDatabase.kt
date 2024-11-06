package com.arunabha.expensetracker.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.arunabha.expensetracker.Utils
import com.arunabha.expensetracker.data.dao.TransactionDao
import com.arunabha.expensetracker.data.model.TransactionEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [TransactionEntity::class], version = 1, exportSchema = false)
abstract class TransactionDatabase : RoomDatabase() {
    abstract fun transactionDao(): TransactionDao

    companion object {
        private const val DATABASE_NAME = "transaction_db"

        @JvmStatic
        fun getDatabase(context: Context): TransactionDatabase {
            return Room.databaseBuilder(
                context,
                TransactionDatabase::class.java,
                DATABASE_NAME
            ).addCallback(object : Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    InitBasicData(context)
                }
                fun InitBasicData(context: Context){
                    CoroutineScope(Dispatchers.IO).launch {
                        val transactionDao = getDatabase(context).transactionDao()

                        // Populating database with basic dummy data
                        transactionDao.addTransaction(TransactionEntity(1, "Pizza", 100.00, Utils.formatDateToHumanReadableForm(System.currentTimeMillis()), "Food", "Expense"))
                        transactionDao.addTransaction(TransactionEntity(2, "Upwork", 1000.00, Utils.formatDateToHumanReadableForm(System.currentTimeMillis()), "Salary", "Income"))
                        transactionDao.addTransaction(TransactionEntity(3, "Netflix", 399.00, Utils.formatDateToHumanReadableForm(System.currentTimeMillis()), "Entertainment", "Expense"))
                        transactionDao.addTransaction(TransactionEntity(4, "Salary", 5000.00, Utils.formatDateToHumanReadableForm(System.currentTimeMillis()), "Salary", "Income"))
                    }
                }
            }).build()
        }
    }
}