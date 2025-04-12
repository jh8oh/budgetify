package dev.ohjiho.budgetify.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import dev.ohjiho.budgetify.domain.enums.Icon
import java.math.BigDecimal
import java.time.YearMonth

enum class TransactionType {
    EXPENSE,
    INCOME,
    TRANSFER
}

/**
 * Sets a limit on a category starting from a YearMonth.
 *
 * @property yearMonth The month and the year the budget was created (Budget continues monthly until new budget is created).
 * @property amount The maximum limit of the budget.
 */
data class Budget(
    var yearMonth: YearMonth,
    var amount: BigDecimal,
)

/**
 * A category that a transaction will fall under.
 *
 * @property uid The unique identifier that refers to this category.
 * @property name The name of the category.
 * @property type The type of transaction this category falls under (expense, income, or transfer)
 * @property icon The icon to be displayed by the category.
 * @property isNeed Whether or not this category falls under a necessity (otherwise, it's a want). [ONLY FOR EXPENSE CATEGORIES]
 * @property budgets List of budgets for this category [ONLY FOR EXPENSE CATEGORIES]
 */
@Entity(tableName = "categories")
data class Category(
    @PrimaryKey(autoGenerate = true) var uid: Int,
    val name: String,
    val type: TransactionType,
    val icon: Icon,
    val isNeed: Boolean? = null,
    val budgets: List<Budget>? = null,
)
