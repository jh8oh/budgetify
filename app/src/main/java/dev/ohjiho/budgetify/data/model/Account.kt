package dev.ohjiho.budgetify.data.model

import java.math.BigDecimal
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

interface Account {
    var uid: Int
    var name: String
    var color: String
    var startingAmount: BigDecimal
    var currentAmount: BigDecimal
    var currency: Currency
    var isFavourite: Boolean
    var accountType: AccountType
}