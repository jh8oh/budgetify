package dev.ohjiho.budgetify.data.repository

import dev.ohjiho.budgetify.data.room.dao.CategoryDao
import dev.ohjiho.budgetify.domain.model.Category
import dev.ohjiho.budgetify.domain.repository.CategoryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class CategoryRepositoryImpl @Inject constructor(private val dao: CategoryDao) :
    BaseRoomRepositoryImpl<Category, CategoryDao>(dao), CategoryRepository {

    override suspend fun getCategory(uid: Int): Category? = dao.getCategory(uid)

    // Transfer
    override suspend fun getTransferCategory(): Category = dao.getTransferCategory()

    // Expense
    override fun getAllExpenseCategories(): Flow<List<Category>> = dao.getAllExpenseCategories()

    // Income
    override fun getAllIncomeCategories(): Flow<List<Category>> = dao.getAllIncomeCategories()
}