package dev.ohjiho.budgetify.domain.model

import androidx.room.Entity
import java.math.BigDecimal
import java.time.YearMonth

/**
 * Sets a limit on a category on a YearMonth.
 *
 * @property categoryId
 * @property yearMonth The month and the year the budget was created (Budget continues monthly until new budget is created).
 * @property amount The maximum limit of the budget.
 */
@Entity(tableName = "budgets", primaryKeys = ["categoryId", "yearMonth"])
data class Budget(
    val categoryId: Int,
    val yearMonth: YearMonth,
    val amount: BigDecimal
)