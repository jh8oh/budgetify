package dev.ohjiho.budgetify.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import java.time.YearMonth
import java.util.Date

/**
 * Sets a limit on a category
 *
 * @param categoryId - The category to set the limit on.
 * @param yearMonth - The month and the year the budget was created (Budget continues monthly until new budget is created).
 * @param amount - The maximum limit of the budget.
 */
@Entity(tableName = "budgets", primaryKeys = ["category_id", "year_month"])
data class Budget(
    @ColumnInfo(name = "category_id") var categoryId: Int,
    @ColumnInfo(name = "year_month") var yearMonth: YearMonth,
    var amount: Int,
)
