package dev.ohjiho.budgetify.data.room.dao

import androidx.room.Dao
import androidx.room.Query
import dev.ohjiho.budgetify.domain.model.ExpenseCategory
import dev.ohjiho.budgetify.domain.model.IncomeCategory
import kotlinx.coroutines.flow.Flow

@Dao
internal interface ExpenseCategoryDao : BaseDao<ExpenseCategory> {
    @Query("SELECT * FROM expense_categories ORDER BY isNeed DESC, name ASC")
    fun getAllExpenseCategories(): Flow<List<ExpenseCategory>>
}

@Dao
internal interface IncomeCategoryDao : BaseDao<IncomeCategory> {

}