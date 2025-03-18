package dev.ohjiho.budgetify.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import dev.ohjiho.budgetify.domain.enums.Icon

enum class TransactionType {
    EXPENSE,
    INCOME,
    TRANSFER
}

/**
 * A category that a transaction will fall under.
 *
 * @property uid The unique identifier that refers to this category.
 * @property name The name of the category.
 * @property type The type of transaction this category falls under (expense, income, or transfer)
 * @property icon The icon to be displayed by the category.
 * @property isNeed Whether or not this category falls under a necessity (otherwise, it's a want). [ONLY FOR EXPENSE CATEGORIES]
 */
@Entity(tableName = "categories")
data class Category(
    @PrimaryKey(autoGenerate = true) var uid: Int,
    val name: String,
    val type: TransactionType,
    val icon: Icon,
    val isNeed: Boolean? = null,
)
