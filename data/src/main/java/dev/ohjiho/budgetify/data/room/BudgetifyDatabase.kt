package dev.ohjiho.budgetify.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import dev.ohjiho.budgetify.data.room.dao.AccountDao
import dev.ohjiho.budgetify.data.room.dao.CategoryDao
import dev.ohjiho.budgetify.data.room.dao.TransactionDao
import dev.ohjiho.budgetify.data.room.util.Converters
import dev.ohjiho.budgetify.domain.model.Account
import dev.ohjiho.budgetify.domain.model.Category
import dev.ohjiho.budgetify.domain.model.Transaction

@Database(
    entities = [Account::class, Category::class, Transaction::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
internal abstract class BudgetifyDatabase : RoomDatabase() {
    abstract fun accountDao(): AccountDao
    abstract fun categoryDao(): CategoryDao
    abstract fun transactionDao(): TransactionDao
}