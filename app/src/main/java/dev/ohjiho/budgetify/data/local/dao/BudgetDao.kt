package dev.ohjiho.budgetify.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import dev.ohjiho.budgetify.data.local.entity.Budget
import kotlinx.coroutines.flow.Flow
import java.time.YearMonth

@Dao
interface BudgetDao: BaseDao<Budget> {

    @Query("SELECT * FROM budgets WHERE year_month = :yearMonth")
    fun getBudget(yearMonth: YearMonth): Flow<List<Budget>>
}