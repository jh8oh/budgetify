package dev.ohjiho.budgetify.utils.data

fun String.toBigDecimalAfterSanitize() = replace("[^0-9-.]".toRegex(), "").toBigDecimal()