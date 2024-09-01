package dev.ohjiho.budgetify.data.repository

import androidx.annotation.WorkerThread
import dev.ohjiho.budgetify.data.room.dao.ExpenseCategoryDao
import dev.ohjiho.budgetify.data.room.dao.IncomeCategoryDao
import dev.ohjiho.budgetify.domain.model.Category
import dev.ohjiho.budgetify.domain.model.ExpenseCategory
import dev.ohjiho.budgetify.domain.model.IncomeCategory
import dev.ohjiho.budgetify.domain.repository.BaseRoomRepository
import dev.ohjiho.budgetify.domain.repository.CategoryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class CategoryRepositoryImpl @Inject constructor(
    private val expenseCategoryDao: ExpenseCategoryDao,
    private val incomeCategoryDao: IncomeCategoryDao,
) : BaseRoomRepository<Category>, CategoryRepository {
    @WorkerThread
    override suspend fun insert(entity: Category): Long {
        return when (entity) {
            is ExpenseCategory -> expenseCategoryDao.insert(entity)
            is IncomeCategory -> incomeCategoryDao.insert(entity)
        }
    }

    @WorkerThread
    override suspend fun update(entity: Category) {
        when (entity) {
            is ExpenseCategory -> expenseCategoryDao.update(entity)
            is IncomeCategory -> incomeCategoryDao.update(entity)
        }
    }

    @WorkerThread
    override suspend fun delete(vararg entity: Category) {
        for (c in entity) {
            when (c) {
                is ExpenseCategory -> expenseCategoryDao.delete(c)
                is IncomeCategory -> incomeCategoryDao.delete(c)
            }
        }
    }

    override fun getAllExpenseCategories(): Flow<List<ExpenseCategory>> = expenseCategoryDao.getAllExpenseCategories()
}