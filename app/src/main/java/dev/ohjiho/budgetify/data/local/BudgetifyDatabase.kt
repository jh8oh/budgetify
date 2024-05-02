package dev.ohjiho.budgetify.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import dev.ohjiho.budgetify.data.local.dao.BudgetDao
import dev.ohjiho.budgetify.data.local.dao.CategoryDao
import dev.ohjiho.budgetify.data.local.dao.TransactionDao
import dev.ohjiho.budgetify.data.local.entity.BudgetEntity
import dev.ohjiho.budgetify.data.local.entity.CategoryEntity
import dev.ohjiho.budgetify.data.local.entity.TransactionEntity
import dev.ohjiho.budgetify.data.local.util.Converters

@Database(
    entities = [CategoryEntity::class, BudgetEntity::class, TransactionEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class BudgetifyDatabase : RoomDatabase() {
    abstract fun budgetDao(): BudgetDao
    abstract fun categoryDao(): CategoryDao
    abstract fun transactionDao(): TransactionDao
}