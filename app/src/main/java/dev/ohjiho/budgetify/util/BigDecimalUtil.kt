package dev.ohjiho.budgetify.util

import java.math.BigDecimal
import java.text.NumberFormat
import java.util.Locale

fun BigDecimal.toCurrencyFormat(locale: Locale) = NumberFormat.getCurrencyInstance(locale).format(this)
