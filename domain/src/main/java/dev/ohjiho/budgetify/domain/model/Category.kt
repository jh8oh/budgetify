package dev.ohjiho.budgetify.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import dev.ohjiho.budgetify.theme.icon.Icon

enum class CategoryType {
    EXPENSE,
    INCOME,
    TRANSFER
}

/**
 * A category that a transaction will fall under.
 *
 * @property uid The unique identifier that refers to this category.
 * @property name The name of the category.
 * @property type The type of category (expense, income, or transfer)
 * @property icon The icon to be displayed by the category.
 * @property isNeed Whether or not this category falls under a necessity (otherwise, it's a want). [ONLY FOR EXPENSE CATEGORIES]
 */
@Entity(tableName = "categories")
data class Category(
    val name: String,
    val type: CategoryType,
    val icon: Icon,
    val isNeed: Boolean? = null,
) {
    @PrimaryKey(autoGenerate = true)
    var uid: Int = 0
}
