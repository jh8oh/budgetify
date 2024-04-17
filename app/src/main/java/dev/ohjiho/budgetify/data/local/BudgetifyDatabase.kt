package dev.ohjiho.budgetify.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import dev.ohjiho.budgetify.data.local.dao.AccountDao
import dev.ohjiho.budgetify.data.local.dao.BudgetDao
import dev.ohjiho.budgetify.data.local.dao.CategoryDao
import dev.ohjiho.budgetify.data.local.dao.ConversionRateDao
import dev.ohjiho.budgetify.data.local.dao.TagDao
import dev.ohjiho.budgetify.data.local.dao.TransactionDao
import dev.ohjiho.budgetify.data.local.dao.TransactionTagCrossRefDao
import dev.ohjiho.budgetify.data.local.entity.Account
import dev.ohjiho.budgetify.data.local.entity.Budget
import dev.ohjiho.budgetify.data.local.entity.Category
import dev.ohjiho.budgetify.data.local.entity.ConversionRate
import dev.ohjiho.budgetify.data.local.entity.Tag
import dev.ohjiho.budgetify.data.local.entity.Transaction
import dev.ohjiho.budgetify.data.local.entity.TransactionTagCrossRef
import dev.ohjiho.budgetify.data.local.util.Converters

@Database(
    entities = [Account::class, Budget::class, Category::class, ConversionRate::class, Tag::class, Transaction::class, TransactionTagCrossRef::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class BudgetifyDatabase : RoomDatabase() {
    abstract fun accountDao(): AccountDao
    abstract fun budgetDao(): BudgetDao
    abstract fun categoryDao(): CategoryDao
    abstract fun conversionRateDao(): ConversionRateDao
    abstract fun tagDao(): TagDao
    abstract fun transactionDao(): TransactionDao
    abstract fun transactionTagCrossRefDao(): TransactionTagCrossRefDao
}