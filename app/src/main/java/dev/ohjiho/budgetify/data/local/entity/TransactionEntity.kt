package dev.ohjiho.budgetify.data.local.entity

import android.icu.math.BigDecimal
import androidx.room.Entity
import androidx.room.PrimaryKey
import dev.ohjiho.budgetify.data.model.Transaction
import java.time.LocalDate

@Entity(tableName = "transactions")
data class TransactionEntity(
    override var accountId: Int,
    override var categoryId: Int,
    override var amount: BigDecimal,
    override var localDate: LocalDate,
) : Transaction {
    @PrimaryKey(autoGenerate = true)
    override var uid: Int = 0
}