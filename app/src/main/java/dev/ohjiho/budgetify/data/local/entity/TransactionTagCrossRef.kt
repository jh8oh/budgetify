package dev.ohjiho.budgetify.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity

/**
 * Used to cross reference transactions and tags. The transaction and the tags are now linked together.
 *
 * @param transactionId - Unique ID of the transaction
 * @param tagName - Unique name of the tag
 */
@Entity(tableName = "transaction_tag_cross_refs", primaryKeys = ["transaction_id", "tag_name"])
data class TransactionTagCrossRef(
    @ColumnInfo(name = "transaction_id") var transactionId: Int,
    @ColumnInfo(name = "tag_name") var tagName: String,
)
