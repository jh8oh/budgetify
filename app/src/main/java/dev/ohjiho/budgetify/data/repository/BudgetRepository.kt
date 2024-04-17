package dev.ohjiho.budgetify.data.repository

import dev.ohjiho.budgetify.data.local.dao.BudgetDao
import dev.ohjiho.budgetify.data.local.entity.Budget
import java.time.YearMonth
import javax.inject.Singleton

@Singleton
class BudgetRepository(private val dao: BudgetDao) : BaseRepository<Budget, BudgetDao>(dao) {
    fun getBudget(yearMonth: YearMonth) = dao.getBudget(yearMonth)
}