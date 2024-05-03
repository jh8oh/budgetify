package dev.ohjiho.budgetify.data.model

import java.math.BigDecimal
import android.icu.util.Currency

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