package dev.ohjiho.budgetify.domain.repository

import dev.ohjiho.budgetify.domain.model.Category
import kotlinx.coroutines.flow.Flow

interface CategoryRepository : BaseRoomRepository<Category> {
    suspend fun getCategory(uid: Int): Category?

    // Transfer
    suspend fun getTransferCategory(): Category

    // Expense
    fun getAllExpenseCategories(): Flow<List<Category>>

    // Income
    fun getAllIncomeCategories(): Flow<List<Category>>
}