package dev.ohjiho.budgetify.utils.primitive

import java.math.BigDecimal
import java.text.NumberFormat

fun String.toDecimal(decimal: Int): String {
    var result = ""
    var after = false
    var decimalCount = 0

    if (isNullOrBlank()) {
        result = "0"
    } else {
        if (first() == '.') result = "0"

        for (c in this) {
            if (c == '.') {
                if (after) continue
                after = true
            } else if (after) {
                decimalCount++
                if (decimalCount > decimal) {
                    return result
                }
            }

            result += c
        }
    }

    if (!after) {
        result += "."
    }
    if (decimalCount < decimal) {
        for (i in decimalCount..<decimal) {
            result += "0"
        }
    }

    return result
}

fun String.removeLeadingZeros(): String {
    return NumberFormat.getNumberInstance().format(BigDecimal(this.replace(",", "")))
}