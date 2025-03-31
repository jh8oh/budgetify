package dev.ohjiho.budgetify.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import java.math.BigDecimal
import java.time.YearMonth

/**
 * Sets a limit on a category. The categoryId and yearMonth properties are the primary key for this entity.
 *
 * @property categoryId The id of the **expense** category to set the limit on.
 * @property yearMonth The month and the year the budget was created (Budget continues monthly until new budget is created).
 * @property amount The maximum limit of the budget.
 */
@Entity(tableName = "budgets", primaryKeys = ["category_id", "year_month"])
data class Budget(
    @ColumnInfo(name = "category_id") var categoryId: Int,
    @ColumnInfo(name = "year_month") var yearMonth: YearMonth,
    var amount: BigDecimal,
)
