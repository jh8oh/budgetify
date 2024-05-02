package dev.ohjiho.budgetify.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import dev.ohjiho.budgetify.data.model.Category
import dev.ohjiho.budgetify.data.model.CategoryType

@Entity(tableName = "categories")
data class CategoryEntity(
    override var name: String,
    override var color: String,
    override var categoryType: CategoryType,
) : Category {
    @PrimaryKey(autoGenerate = true)
    override var uid: Int = 0
}