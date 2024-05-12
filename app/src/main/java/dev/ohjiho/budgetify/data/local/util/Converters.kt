package dev.ohjiho.budgetify.data.local.util

import androidx.room.TypeConverter
import dev.ohjiho.budgetify.data.model.AccountType
import dev.ohjiho.budgetify.data.model.CategoryType
import java.math.BigDecimal
import java.time.LocalDate
import java.time.YearMonth
import java.util.Currency

class Converters {
    // Account Type
    @TypeConverter
    fun accountTypeToString(accountType: AccountType?): String? {
        return accountType?.name
    }

    @TypeConverter
    fun stringToAccountType(string: String?): AccountType? {
        return string?.let { enumValueOf<AccountType>(it) }
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
    fun categoryTypeToString(categoryType: CategoryType?): String? {
        return categoryType?.name
    }

    @TypeConverter
    fun stringToCategoryType(string: String?): CategoryType? {
        return string?.let { enumValueOf<CategoryType>(it) }
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