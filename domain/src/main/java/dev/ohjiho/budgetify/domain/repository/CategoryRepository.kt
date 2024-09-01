package dev.ohjiho.budgetify.domain.repository

import dev.ohjiho.budgetify.domain.model.Category
import dev.ohjiho.budgetify.domain.model.ExpenseCategory
import kotlinx.coroutines.flow.Flow

interface CategoryRepository: BaseRoomRepository<Category> {
    fun getAllExpenseCategories(): Flow<List<ExpenseCategory>>
}