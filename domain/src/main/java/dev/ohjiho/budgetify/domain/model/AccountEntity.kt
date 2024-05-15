package dev.ohjiho.budgetify.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.math.BigDecimal
import java.time.LocalDate
import java.util.Currency

enum class AccountType(val displayName: String) {
    CHEQUING("Chequing"),
    SAVINGS("Savings"),
    CREDIT("Credit"),
    CASH("Cash"),
    INVESTMENT("Investment"),
    LOAN("Loan"),
    BUSINESS("Business")
}

@Entity(tableName = "accounts")
data class AccountEntity(
    var name: String,
    var color: String,
    var accountType: AccountType,
    var startingAmount: BigDecimal,
    var currency: Currency,
    var startDate: LocalDate = LocalDate.now(),
) {
    @PrimaryKey(autoGenerate = true)
    var uid: Int = 0

    var currentAmount: BigDecimal = startingAmount
}