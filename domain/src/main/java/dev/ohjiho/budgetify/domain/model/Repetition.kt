package dev.ohjiho.budgetify.domain.model

import dev.ohjiho.budgetify.utils.data.getLocale
import dev.ohjiho.budgetify.utils.ui.ordinal
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.time.temporal.TemporalAdjusters

enum class RepeatPeriod(val displayText: String) {
    WEEKLY("Weekly"),
    MONTHLY("Monthly")
}

/**
 * The days in which a transaction is repeated
 *
 * @property timePeriod RepeatPeriod.WEEKLY if repeated weekly, or RepeatPeriod.MONTHLY if repeated monthly
 * @property indexOfRepetition A list of days when the transaction is repeated. If weekly, 1->7 used for days of the weeks. If monthly, 1->31 used for days of the month.
 */
class Repetition(
    val timePeriod: RepeatPeriod,
) {
    var indexOfRepetition: List<Int> = emptyList()
        set(value) {
            when (timePeriod) {
                RepeatPeriod.WEEKLY -> {
                    if (value.any { it < 1 }) {
                        throw IllegalArgumentException("Cannot accept any indexes with values less than 1")
                    } else if (value.any { it > 7 }) {
                        throw IllegalArgumentException("Cannot accept any indexes with values greater than 7")
                    } else {
                        field = value.distinct()
                    }
                }

                RepeatPeriod.MONTHLY -> {
                    if (value.any { it < 1 }) {
                        throw IllegalArgumentException("Cannot accept any indexes with values less than 1")
                    } else if (value.any { it > 31 }) {
                        throw IllegalArgumentException("Cannot accept any indexes with values greater than 31")
                    } else {
                        field = value.distinct()
                    }
                }
            }
        }

    fun isRepeated() = indexOfRepetition.isNotEmpty()

    fun getRepetition(yearMonth: YearMonth): List<LocalDate> {
        if (!isRepeated()) return emptyList()

        return when (timePeriod) {
            RepeatPeriod.WEEKLY -> {
                val repeatedDates = mutableListOf<LocalDate>()
                val daysOfWeek = indexOfRepetition.map { DayOfWeek.of(it) }

                for (dow in daysOfWeek) {
                    var date = yearMonth.atDay(1).with(TemporalAdjusters.dayOfWeekInMonth(1, dow))
                    do {
                        repeatedDates.add(date)
                        date = date.plusWeeks(1)
                    } while (YearMonth.from(date).equals(yearMonth))
                }

                repeatedDates
            }

            RepeatPeriod.MONTHLY -> {
                indexOfRepetition.map { if (it > yearMonth.lengthOfMonth()) yearMonth.lengthOfMonth() else it }.distinct()
                    .map { yearMonth.atDay(it) }
            }
        }
    }

    override fun toString(): String {
        if (!isRepeated()) return NEVER_REPEATED_DISPLAY_TEXT

        val timePeriodDisplayText = timePeriod.displayText
        val requiresThe = if (timePeriod == RepeatPeriod.MONTHLY) "the" else ""
        val repeatedDays = if (timePeriod == RepeatPeriod.MONTHLY)
            indexOfRepetition.map { it.ordinal() }
        else indexOfRepetition.map {
            DayOfWeek.of(it).getDisplayName(TextStyle.SHORT, getLocale())
        }

        return "$timePeriodDisplayText on $requiresThe ${repeatedDays.joinToString()}"
    }

    companion object {
        const val NEVER_REPEATED_DISPLAY_TEXT = "Never"
    }
}
