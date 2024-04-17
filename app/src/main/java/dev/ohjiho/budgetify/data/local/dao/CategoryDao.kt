package dev.ohjiho.budgetify.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import dev.ohjiho.budgetify.data.local.entity.Category
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for the categories table
 */
@Dao
interface CategoryDao : BaseDao<Category> {
    /**
     * Returns either a list of expense categories or income categories
     */
    @Query("SELECT * FROM categories WHERE isExpense = :isExpense")
    fun getCategories(isExpense: Boolean): Flow<List<Category>>
}