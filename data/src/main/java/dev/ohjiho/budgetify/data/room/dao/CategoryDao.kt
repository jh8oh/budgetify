package dev.ohjiho.budgetify.data.room.dao

import androidx.room.Dao
import androidx.room.Query
import dev.ohjiho.budgetify.domain.model.ExpenseCategory
import dev.ohjiho.budgetify.domain.model.IncomeCategory
import kotlinx.coroutines.flow.Flow

@Dao
internal interface ExpenseCategoryDao : BaseDao<ExpenseCategory> {
    @Query("SELECT * FROM expense_categories WHERE :uid = uid")
    suspend fun getExpenseCategory(uid: Int): ExpenseCategory

    @Query("SELECT * FROM expense_categories ORDER BY isNeed DESC, name ASC")
    fun getAllExpenseCategories(): Flow<List<ExpenseCategory>>
}

@Dao
internal interface IncomeCategoryDao : BaseDao<IncomeCategory> {
    @Query("SELECT * FROM income_categories WHERE :uid = uid")
    suspend fun getIncomeCategory(uid: Int): IncomeCategory

    @Query("SELECT * FROM income_categories ORDER BY name ASC")
    fun getAllIncomeCategories(): Flow<List<IncomeCategory>>
}