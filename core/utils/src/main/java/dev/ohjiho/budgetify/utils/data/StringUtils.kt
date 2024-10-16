package dev.ohjiho.budgetify.utils.data

import java.math.BigDecimal

fun String.toBigDecimalAfterSanitize(): BigDecimal {
    if (isNullOrBlank()) return BigDecimal.ZERO
    return replace("[^0-9-.]".toRegex(), "").toBigDecimal()
}