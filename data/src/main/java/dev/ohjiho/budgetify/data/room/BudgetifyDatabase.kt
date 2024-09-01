package dev.ohjiho.budgetify.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import dev.ohjiho.budgetify.data.room.dao.AccountDao
import dev.ohjiho.budgetify.data.room.dao.ExpenseCategoryDao
import dev.ohjiho.budgetify.data.room.dao.IncomeCategoryDao
import dev.ohjiho.budgetify.data.room.util.Converters
import dev.ohjiho.budgetify.domain.model.Account
import dev.ohjiho.budgetify.domain.model.ExpenseCategory
import dev.ohjiho.budgetify.domain.model.IncomeCategory

@Database(
    entities = [Account::class, ExpenseCategory::class, IncomeCategory::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
internal abstract class BudgetifyDatabase : RoomDatabase() {
    abstract fun accountDao(): AccountDao
    abstract fun expenseCategoryDao(): ExpenseCategoryDao
    abstract fun incomeCategoryDao(): IncomeCategoryDao
}