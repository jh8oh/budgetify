package dev.ohjiho.budgetify.data.model

import java.math.BigDecimal
import java.time.YearMonth

interface Budget {
    var categoryId: Int
    var amount: BigDecimal
    var yearMonth: YearMonth
}