package dev.ohjiho.budgetify.data.room.dao

import androidx.room.Dao
import androidx.room.Query
import dev.ohjiho.budgetify.domain.model.Category
import kotlinx.coroutines.flow.Flow

@Dao
internal interface CategoryDao : BaseDao<Category> {
    @Query("SELECT * FROM categories WHERE :uid = uid")
    suspend fun getCategory(uid: Int): Category

    // Transfer
    @Query("SELECT * FROM categories WHERE type = 'TRANSFER'")
    suspend fun getTransferCategory(): Category

    // Expense
    @Query("SELECT * FROM categories WHERE type = 'EXPENSE' ORDER BY isNeed DESC, name ASC")
    fun getAllExpenseCategories(): Flow<List<Category>>

    // Income
    @Query("SELECT * FROM categories WHERE type = 'INCOME' ORDER BY name ASC")
    fun getAllIncomeCategories(): Flow<List<Category>>
}