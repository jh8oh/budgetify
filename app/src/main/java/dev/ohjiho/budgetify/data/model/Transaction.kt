package dev.ohjiho.budgetify.data.model

import android.icu.math.BigDecimal
import android.icu.util.Currency
import java.time.LocalDate

interface Transaction {
    var uid: Int
    var categoryId: Int
    var amount: BigDecimal
    var currency: Currency
    var localDate: LocalDate
}