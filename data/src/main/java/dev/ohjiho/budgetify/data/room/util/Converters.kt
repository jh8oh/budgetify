package dev.ohjiho.budgetify.data.room.util

import androidx.room.TypeConverter
import dev.ohjiho.budgetify.domain.model.AccountType
import dev.ohjiho.budgetify.theme.icons.Icon
import java.math.BigDecimal
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

    // Currency
    @TypeConverter
    fun currencyToString(currency: Currency?): String? {
        return currency?.currencyCode
    }

    @TypeConverter
    fun stringToCurrency(isoCode: String?): Currency? {
        return isoCode?.let { Currency.getInstance(it) }
    }

    // Icon
    @TypeConverter
    fun iconToOrdinal(icon: Icon?): Int? {
        return icon?.ordinal
    }

    @TypeConverter
    fun ordinalToIcon(ordinal: Int?): Icon? {
        return ordinal?.let { Icon.entries[it] }
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