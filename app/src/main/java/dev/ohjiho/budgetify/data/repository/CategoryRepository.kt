package dev.ohjiho.budgetify.data.repository

import dev.ohjiho.budgetify.data.local.dao.CategoryDao
import dev.ohjiho.budgetify.data.local.entity.CategoryEntity
import dev.ohjiho.budgetify.data.model.Category
import dev.ohjiho.budgetify.data.model.CategoryType
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface CategoryRepository : BaseRepository<CategoryEntity> {
    fun getCategories(categoryType: CategoryType): Flow<List<Category>>
}

class CategoryRepositoryImpl @Inject constructor(
    private val dao: CategoryDao,
) : BaseRepositoryImpl<CategoryEntity, CategoryDao>(dao), CategoryRepository {
    override fun getCategories(categoryType: CategoryType) = dao.getCategories(categoryType)
}