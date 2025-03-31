package dev.ohjiho.budgetify.data.repository

import dev.ohjiho.budgetify.data.room.dao.BudgetDao
import dev.ohjiho.budgetify.domain.model.Budget
import dev.ohjiho.budgetify.domain.repository.BudgetRepository
import javax.inject.Inject

internal class BudgetRepositoryImpl @Inject constructor(private val dao: BudgetDao) :
    BaseRoomRepositoryImpl<Budget, BudgetDao>(dao), BudgetRepository {

    override suspend fun deleteCategory(categoryId: Int) {
        dao.deleteCategory(categoryId)
    }
}