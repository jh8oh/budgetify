package dev.ohjiho.budgetify.utils.ui

import kotlin.math.absoluteValue

fun Int.ordinal(): String {
    val iAbs = this.absoluteValue // if you want negative ordinals, or just use i

    return "$this" + if (iAbs % 100 in 11..13) "th" else when (iAbs % 10) {
        1 -> "st"
        2 -> "nd"
        3 -> "rd"
        else -> "th"
    }
}