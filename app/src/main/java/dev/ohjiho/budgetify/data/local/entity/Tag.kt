package dev.ohjiho.budgetify.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * A transaction can be tagged which will help to sort and filter them. These tags can be merchant name, transaction type, etc. Whatever the user desires.
 *
 * @param name - Name of the tag (Must be unique).
 */
@Entity(tableName = "tags")
data class Tag(
    @PrimaryKey var name: String
)
