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
     * Returns a list of all categories in the database
     */
    @Query("SELECT * FROM categories")
    fun getAllCategories(): Flow<List<Category>>
}