package dev.ohjiho.budgetify.data.repository

import dev.ohjiho.budgetify.data.local.dao.CategoryDao
import dev.ohjiho.budgetify.data.local.entity.Category
import javax.inject.Singleton

@Singleton
class CategoryRepository(
    private val dao: CategoryDao,
) : BaseRepository<Category, CategoryDao>(dao) {
    fun getCategories(isExpense: Boolean) = dao.getCategories(isExpense)
}