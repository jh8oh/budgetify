package dev.ohjiho.budgetify.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.math.BigDecimal
import java.util.Currency

/**
 * An instance of an expense, income, or transfer between accounts
 *
 * @param categoryId - The category the transaction falls under (transfers between accounts is 0).
 * @param accountId - The account the transaction falls under.
 * @param amount - The amount the transaction was for.
 */

@Entity(tableName = "transactions")
data class Transaction(
    @ColumnInfo(name = "category_id") var categoryId: Int,
    @ColumnInfo(name = "account_id") var accountId: Int,
    var amount: BigDecimal,
    var currency: Currency,
) {
    @PrimaryKey(autoGenerate = true)
    var uid: Int = 0
}
