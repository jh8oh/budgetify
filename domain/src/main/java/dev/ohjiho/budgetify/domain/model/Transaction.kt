package dev.ohjiho.budgetify.domain.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.math.BigDecimal
import java.time.DayOfWeek
import java.time.LocalDate

enum class Interval {
    WEEKLY,
    BIWEEKLY,
    MONTHLY
}

/**
 * Information about a transaction's reoccurrence
 *
 * @param interval The time interval this transactions repeats after (weekly, biweekly, or monthly)
 * @param dayOfWeek The day of the week this transaction occurs (Used for weekly and biweekly transactions)
 * @param dayOfMonth The day of the month this transaction occurs (Used for monthly transactions)
 */
data class Reoccurrence(
    val interval: Interval,
    val dayOfWeek: DayOfWeek?,
    val dayOfMonth: Int?,
)

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
    @PrimaryKey(autoGenerate = true) var uid: Int,
    val fromAccountId: Int,
    val toAccountId: Int?,
    val categoryId: Int,
    val amount: BigDecimal,
    val localDate: LocalDate,
    @Embedded val reoccurrence: Reoccurrence?,
)
