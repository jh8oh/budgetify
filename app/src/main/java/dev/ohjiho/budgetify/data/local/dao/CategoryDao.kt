package dev.ohjiho.budgetify.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import dev.ohjiho.budgetify.data.local.entity.CategoryEntity
import dev.ohjiho.budgetify.data.model.Category
import dev.ohjiho.budgetify.data.model.CategoryType
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for the categories table
 */
@Dao
interface CategoryDao : BaseDao<CategoryEntity> {
    /**
     * Returns all categories that fall under the categoryType parameter
     */
    @Query("SELECT * FROM categories WHERE categoryType = :categoryType")
    fun getCategories(categoryType: CategoryType): Flow<List<Category>>
}