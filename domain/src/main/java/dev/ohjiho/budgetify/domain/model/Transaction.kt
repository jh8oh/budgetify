package dev.ohjiho.budgetify.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.math.BigDecimal
import java.time.LocalDate

// TODO: Reoccurring transactions

@Entity(tableName = "transactions")
data class Transaction(
    val accountId: Int,
    val categoryId: Int,
    val amount: BigDecimal,
    val localDate: LocalDate,
) {
    @PrimaryKey(autoGenerate = true)
    var uid: Int = 0
}
