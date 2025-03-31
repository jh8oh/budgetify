package dev.ohjiho.budgetify.domain.repository

import dev.ohjiho.budgetify.domain.model.Budget

interface BudgetRepository : BaseRoomRepository<Budget> {
    suspend fun deleteCategory(categoryId: Int)
}