package dev.ohjiho.budgetify.data.room.util

import androidx.room.TypeConverter
import dev.ohjiho.budgetify.domain.model.AccountType
import dev.ohjiho.budgetify.domain.model.Interval
import dev.ohjiho.budgetify.domain.model.TransactionType
import dev.ohjiho.budgetify.icons.Icon
import java.math.BigDecimal
import java.time.DayOfWeek
import java.time.LocalDate
import java.util.Currency

internal class Converters {
    // Account Type
    @TypeConverter
    fun accountTypeToString(accountType: AccountType?): String? {
        return accountType?.name
    }

    @TypeConverter
    fun stringToAccountType(string: String?): AccountType? {
        return string?.let { AccountType.valueOf(it) }
    }

    // Big Decimal
    @TypeConverter
    fun bigDecimalToString(bigDecimal: BigDecimal?): String? {
        return bigDecimal?.toPlainString()
    }

    @TypeConverter
    fun stringToBigDecimal(string: String?): BigDecimal? {
        return string?.toBigDecimalOrNull()
    }

    // Category Type
    @TypeConverter
    fun categoryTypeToString(transactionType: TransactionType?): String? {
        return transactionType?.name
    }

    @TypeConverter
    fun stringToCategoryType(name: String?): TransactionType? {
        return name?.let { TransactionType.valueOf(it) }
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

    // Day of Week
    @TypeConverter
    fun dayOfWeekToInt(dayOfWeek: DayOfWeek?): Int? {
        return dayOfWeek?.value
    }

    @TypeConverter
    fun intToDayOfWeek(numberedDayOfWeek: Int?): DayOfWeek? {
        return numberedDayOfWeek?.let { DayOfWeek.of(numberedDayOfWeek) }
    }

    // Icon
    @TypeConverter
    fun iconToString(icon: Icon?): String? {
        return icon?.name
    }

    @TypeConverter
    fun stringToIcon(name: String?): Icon? {
        return name?.let { Icon.valueOf(it) }
    }

    // Interval
    @TypeConverter
    fun intervalToString(interval: Interval?): String? {
        return interval?.name
    }

    @TypeConverter
    fun stringToInterval(name: String?): Interval? {
        return name?.let { Interval.valueOf(it) }
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
}