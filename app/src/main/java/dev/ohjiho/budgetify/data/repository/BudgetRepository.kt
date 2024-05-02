package dev.ohjiho.budgetify.data.repository

import dev.ohjiho.budgetify.data.local.dao.BudgetDao
import dev.ohjiho.budgetify.data.local.entity.BudgetEntity
import dev.ohjiho.budgetify.data.model.Budget
import kotlinx.coroutines.flow.Flow
import java.time.YearMonth
import javax.inject.Inject

interface BudgetRepository : BaseRepository<BudgetEntity> {
    fun getBudget(yearMonth: YearMonth): Flow<List<Budget>>
}

class BudgetRepositoryImpl @Inject constructor(
    private val dao: BudgetDao,
) : BaseRepositoryImpl<BudgetEntity, BudgetDao>(dao), BudgetRepository {
    override fun getBudget(yearMonth: YearMonth): Flow<List<Budget>> = dao.getBudget(yearMonth)
}