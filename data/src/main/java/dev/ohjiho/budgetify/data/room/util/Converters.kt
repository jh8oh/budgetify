package dev.ohjiho.budgetify.data.room.util

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dev.ohjiho.budgetify.domain.enums.Icon
import dev.ohjiho.budgetify.domain.model.AccountType
import dev.ohjiho.budgetify.domain.model.Budget
import dev.ohjiho.budgetify.domain.model.Reoccurrence
import dev.ohjiho.budgetify.domain.model.TransactionType
import java.math.BigDecimal
import java.time.LocalDate
import java.time.YearMonth
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

    // Budgets
    @TypeConverter
    fun budgetsToJson(budgets: List<Budget>?): String? {
        return Gson().toJson(budgets)
    }

    @TypeConverter
    fun jsonToBudgets(json: String?): List<Budget>? {
        return try {
            Gson().fromJson(json, object : TypeToken<List<Budget>>() {}.type)
        } catch (e: Exception) {
            null
        }
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

    // Icon
    @TypeConverter
    fun iconToString(icon: Icon?): String? {
        return icon?.name
    }

    @TypeConverter
    fun stringToIcon(name: String?): Icon? {
        return name?.let { Icon.valueOf(it) }
    }

    // LocalDate
    @TypeConverter
    fun localDateToString(localDate: LocalDate?): String? {
        return localDate?.toString()
    }

    @TypeConverter
    fun stringToLocalDate(string: String?): LocalDate? {
        return string?.let { LocalDate.parse(it) }
    }

    // Reoccurrence
    @TypeConverter
    fun reoccurrenceToJson(reoccurrence: Reoccurrence?): String? {
        return Gson().toJson(reoccurrence)
    }

    @TypeConverter
    fun jsonToReoccurrence(json: String?): Reoccurrence? {
        return try {
            Gson().fromJson(json, Reoccurrence::class.java)
        } catch (e: Exception) {
            null
        }
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