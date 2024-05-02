package dev.ohjiho.budgetify.data.local.util

import android.graphics.Color
import androidx.room.TypeConverter
import dev.ohjiho.budgetify.data.model.CategoryType
import java.math.BigDecimal
import java.time.YearMonth

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

    // Category Types
    @TypeConverter
    fun categoryTypeToString(categoryType: CategoryType?): String? {
        return categoryType?.name
    }

    @TypeConverter
    fun stringToCategoryType(string: String?): CategoryType? {
        return string?.let { enumValueOf<CategoryType>(string) }
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