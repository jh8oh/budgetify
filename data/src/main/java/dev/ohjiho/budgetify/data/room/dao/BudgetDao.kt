package dev.ohjiho.budgetify.data.room.dao

import androidx.room.Dao
import androidx.room.Query
import dev.ohjiho.budgetify.domain.model.Budget

@Dao
internal interface BudgetDao : BaseDao<Budget> {
    @Query("DELETE FROM budgets WHERE category_id = :categoryId")
    suspend fun deleteCategory(categoryId: Int)
}