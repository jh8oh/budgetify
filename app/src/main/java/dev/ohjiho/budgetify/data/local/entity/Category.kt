package dev.ohjiho.budgetify.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * A category that a transaction will fall under
 *
 * @property uid - The unique ID that refers to this category.
 * @param name - The name of the category.
 * @param iconId - The ID of the icon to display (note that this is NOT a resource ID).
 * @param color - The color of the icon.
 * @param isExpense - Whether this category is an expense or an income.
 * @param isNeed - Whether this category is a "need" or a "want". Null if this category is an income.
 */
@Entity(tableName = "categories", indices = [Index(value = ["name"], unique = true)])
data class Category(
    var name: String,
    var iconId: Int,
    var color: Int,
    var isExpense: Boolean,
    var isNeed: Boolean?,
) {
    @PrimaryKey(autoGenerate = true)
    var uid: Int = 0
}
