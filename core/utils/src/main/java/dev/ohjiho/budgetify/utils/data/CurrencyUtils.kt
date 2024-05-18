package dev.ohjiho.budgetify.utils.data

import android.content.Context
import java.math.BigDecimal
import java.text.NumberFormat
import java.util.Currency

fun BigDecimal.toCurrencyFormat(currency: Currency, context: Context? = null): String {
    val locale = context?.let { getLocale(it) } ?: getLocale()
    return NumberFormat.getCurrencyInstance(locale).run {
        this.currency = currency
        format(this@toCurrencyFormat)
    }
}