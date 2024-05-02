package dev.ohjiho.budgetify.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import dev.ohjiho.budgetify.data.local.entity.BudgetEntity
import dev.ohjiho.budgetify.data.model.Budget
import kotlinx.coroutines.flow.Flow
import java.time.YearMonth

@Dao
interface BudgetDao: BaseDao<BudgetEntity> {
    /**
     * Grabs all the budgets for a specified month and year
     */
    @Query("SELECT * FROM budgets WHERE yearMonth = :yearMonth")
    fun getBudget(yearMonth: YearMonth): Flow<List<Budget>>
}