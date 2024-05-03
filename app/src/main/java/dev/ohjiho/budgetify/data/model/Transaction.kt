package dev.ohjiho.budgetify.data.model

import java.math.BigDecimal
import java.time.LocalDate

interface Transaction {
    var uid: Int
    var accountId: Int
    var categoryId: Int
    var amount: BigDecimal
    var localDate: LocalDate
}