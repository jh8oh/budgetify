package dev.ohjiho.budgetify.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.math.BigDecimal
import java.time.LocalDate

/**
 * An exchange of money from/to accounts that falls under a category.
 *
 * @param fromAccountId The id of the account this transaction took money from
 * @param toAccountId The id of the account this transaction put money in (only used for transfers)
 * @param categoryId The id of the category this transaction falls under
 * @param amount The amount of money being transacted
 * @param localDate The date this transaction occurred
 * @param reoccurrence Info relating to this transaction if it reoccurs
 */
@Entity(tableName = "transactions")
data class Transaction(
    @PrimaryKey(autoGenerate = true) var uid: Int = 0,
    val fromAccountId: Int = 0,
    val toAccountId: Int? = null,
    val categoryId: Int = 0,
    val amount: BigDecimal = BigDecimal.ZERO,
    val localDate: LocalDate = LocalDate.now(),
    val reoccurrence: Reoccurrence? = null,
)
