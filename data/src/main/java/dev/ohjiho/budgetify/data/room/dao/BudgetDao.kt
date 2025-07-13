package dev.ohjiho.budgetify.data.room.dao

import androidx.room.Dao
import dev.ohjiho.budgetify.domain.model.Budget

@Dao
internal interface BudgetDao : BaseDao<Budget> {
}