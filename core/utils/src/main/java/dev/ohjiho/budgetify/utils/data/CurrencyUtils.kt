@file:JvmName("CurrencyUtils")

package dev.ohjiho.budgetify.utils.data

import android.content.Context
import java.math.BigDecimal
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.Currency

fun Currency.getDecimalAmount(): Int {
    return when (currencyCode) {
        "CVE", "DJF", "GNF", "IDR", "JPY", "KMF", "KRW", "PYG", "RWF", "UGX", "VND", "VUV", "XAF", "XOF", "XPF" -> 0
        "BHD", "IQD", "JOD", "KWD", "LYD", "OMR", "TND" -> 3
        else -> 2
    }
}

fun BigDecimal.toCurrencyFormat(currency: Currency, context: Context? = null): String {
    val locale = context?.let { getLocale(it) } ?: getLocale()
    return (NumberFormat.getCurrencyInstance(locale) as DecimalFormat).run {
        this.currency = currency
        decimalFormatSymbols = decimalFormatSymbols.apply {
            currencySymbol = ""
        }
        format(this@toCurrencyFormat)
    }
}

fun Float.toCurrencyFormat(currency: Currency, context: Context? = null): String {
    return this.toBigDecimal().toCurrencyFormat(currency, context)
}

fun String.toCurrencyFormat(currency: Currency, context: Context? = null): String {
    if (isNullOrEmpty()) return "0"
    return toBigDecimalAfterSanitize().toCurrencyFormat(currency, context)
}