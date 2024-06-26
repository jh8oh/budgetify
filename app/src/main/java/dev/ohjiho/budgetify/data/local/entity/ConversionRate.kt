package dev.ohjiho.budgetify.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import java.math.BigDecimal
import java.time.LocalDate
import java.util.Currency

/**
 * The rate at which one currency can be exchanged to another
 *
 * @param toCurrency - The currency which is being converted to.
 * @param localDate - The day, month, and year which this rate was taken.
 * @param rate - The percentage rate of the conversion.
 */
@Entity(tableName = "conversion_rates", primaryKeys = ["to_iso_code", "date"])
data class ConversionRate(
    @ColumnInfo(name = "to_iso_code") var toCurrency: Currency,
    @ColumnInfo(name = "date") var localDate: LocalDate,
    var rate: BigDecimal
)
