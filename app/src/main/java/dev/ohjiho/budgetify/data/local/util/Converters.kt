package dev.ohjiho.budgetify.data.local.util

import android.graphics.Color
import androidx.room.TypeConverter
import java.math.BigDecimal
import java.time.LocalDate
import java.time.YearMonth
import java.util.Currency

class Converters {
    // Big Decimal
    @TypeConverter
    fun bigDecimalToString(bigDecimal: BigDecimal?): String? {
        return bigDecimal?.toPlainString()
    }

    @TypeConverter
    fun stringToBigDecimal(string: String?): BigDecimal? {
        return string?.toBigDecimalOrNull()
    }

    // Color
    @TypeConverter
    fun colorToArgb(color: Color?): Int? {
        return color?.toArgb()
    }

    @TypeConverter
    fun argbToColor(argb: Int?): Color? {
        return argb?.let { Color.valueOf(argb) }
    }

    // Currency
    @TypeConverter
    fun currencyToString(currency: Currency?): String? {
        return currency?.currencyCode
    }

    @TypeConverter
    fun stringToCurrency(isoCode: String?): Currency? {
        return isoCode?.let { Currency.getInstance(it) }
    }

    // LocalDate
    @TypeConverter
    fun localDateToString(localDate: LocalDate?): String? {
        return localDate?.toString()
    }

    @TypeConverter
    fun stringToLocalDate(string: String?): LocalDate? {
        return string?.let { LocalDate.parse(string) }
    }

    // YearMonth
    @TypeConverter
    fun yearMonthToString(yearMonth: YearMonth?): String? {
        return yearMonth?.toString()
    }

    @TypeConverter
    fun stringToYearMonth(string: String?): YearMonth? {
        return string?.let { YearMonth.parse(it) }
    }
}