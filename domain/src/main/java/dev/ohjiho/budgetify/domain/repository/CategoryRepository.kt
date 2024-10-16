package dev.ohjiho.budgetify.domain.repository

import dev.ohjiho.budgetify.domain.model.Category
import dev.ohjiho.budgetify.domain.model.ExpenseCategory
import dev.ohjiho.budgetify.domain.model.IncomeCategory
import kotlinx.coroutines.flow.Flow

interface CategoryRepository: BaseRoomRepository<Category> {
    // Expense Categories
    suspend fun getExpenseCategory(uid: Int): ExpenseCategory
    fun getAllExpenseCategories(): Flow<List<ExpenseCategory>>

    // Income Categories
    suspend fun getIncomeCategory(uid: Int): IncomeCategory
    fun getAllIncomeCategories(): Flow<List<IncomeCategory>>
}