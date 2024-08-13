@file:JvmName("CurrencyUtils")

package dev.ohjiho.budgetify.utils.data

import android.content.Context
import java.math.BigDecimal
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.Currency

fun BigDecimal.toCurrencyFormat(currency: Currency, showCurrencyCode: Boolean, context: Context? = null): String {
    val locale = context?.let { getLocale(it) } ?: getLocale()
    return (NumberFormat.getCurrencyInstance(locale) as DecimalFormat).run {
        this.currency = currency
        this.decimalFormatSymbols = decimalFormatSymbols.apply {
            currencySymbol = if (showCurrencyCode) "${currency.currencyCode} " else ""
        }
        format(this@toCurrencyFormat)
    }
}

fun String.reformat(currency: Currency, context: Context? = null): String {
    return toBigDecimalAfterSanitize().toCurrencyFormat(currency, false, context)
}