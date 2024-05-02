package dev.ohjiho.budgetify.data.local.entity

import androidx.room.Entity
import dev.ohjiho.budgetify.data.model.Budget
import java.math.BigDecimal
import java.time.YearMonth

@Entity(tableName = "budgets", primaryKeys = ["categoryId", "yearMonth"])
data class BudgetEntity(
    override var categoryId: Int,
    override var amount: BigDecimal,
    override var yearMonth: YearMonth,
) : Budget