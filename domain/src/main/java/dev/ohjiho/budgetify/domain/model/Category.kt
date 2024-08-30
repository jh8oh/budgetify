package dev.ohjiho.budgetify.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import dev.ohjiho.budgetify.theme.icons.Icon

/**
 * A category that a transaction will fall under.
 */
interface Category {
    var uid: Int
    val name: String
    val icon: Icon
}

/**
 * A category that an expense transaction will fall under.
 *
 * @property uid The unique identifier that refers to this category.
 * @property name The name of the category.
 * @property icon The icon to be displayed by the category.
 * @property isNeed Whether or not this category falls under a necessity (otherwise, it's a want).
 */
@Entity(tableName = "expense_categories")
data class ExpenseCategory(
    override val name: String,
    override val icon: Icon,
    val isNeed: Boolean,
) : Category{
    @PrimaryKey(autoGenerate = true)
    override var uid: Int = 0
}

/**
 * A category that an income transaction will fall under.
 *
 * @property uid The unique identifier that refers to this category.
 * @property name The name of the category.
 * @property icon The icon to be displayed by the category.
 */
@Entity(tableName = "income_categories")
data class IncomeCategory(
    override val name: String,
    override val icon: Icon,
) : Category {
    @PrimaryKey(autoGenerate = true)
    override var uid: Int = 0
}
