package dev.ohjiho.budgetify.data.model

import android.icu.util.Currency
import java.math.BigDecimal

interface Account {
    var uid: Int
    var name: String
    var color: String
    var startingAmount: BigDecimal
    var currentAmount: BigDecimal
    var currency: Currency
    var isFavourite: Boolean
    var isSavings: Boolean
}