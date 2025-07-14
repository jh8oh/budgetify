package dev.ohjiho.budgetify.domain.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import dev.ohjiho.budgetify.domain.enums.Icon
import java.math.BigDecimal

/**
 * A category that a transaction will fall under.
 *
 * @property uid The unique identifier that refers to this category.
 * @property name The name of the category.
 * @property type The type of transaction this category falls under. [expense, income, or transfer]
 * @property icon The icon to be displayed by the category.
 * @property isNeed Whether or not this category falls under a necessity (otherwise, it's a want). [ONLY FOR EXPENSE CATEGORIES]
 * @property budgetInfo Information regarding the budget for this category. [ONLY FOR EXPENSE CATEGORIES]
 */
@Entity(tableName = "categories")
data class Category(
    @PrimaryKey(autoGenerate = true) var uid: Int = 0,
    val name: String = "",
    val type: TransactionType = TransactionType.EXPENSE,
    val icon: Icon = Icon.HOME,
    val isNeed: Boolean? = null,
    @Embedded val budgetInfo: BudgetInfo? = null,
)

/**
 * A monthly budget for a category
 *
 * @property budgetAmount The base monthly amount for a category's budget
 * @property rolloverAmount The minimum and maximum amount to rollover to next month (minimum can be negative)
 */
data class BudgetInfo(
    val budgetAmount: BigDecimal,
    @Embedded val rolloverAmount: RolloverAmount? = null,
)

data class RolloverAmount(
    val minRolloverAmount: BigDecimal,
    val maxRolloverAmount: BigDecimal
)
