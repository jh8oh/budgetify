package dev.ohjiho.budgetify.domain.model

import android.os.Parcelable
import dev.ohjiho.budgetify.utils.data.getLocale
import dev.ohjiho.budgetify.utils.ui.ordinal
import kotlinx.parcelize.Parcelize
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.time.temporal.TemporalAdjusters

enum class RepeatPeriod(val displayText: String) {
    WEEKLY("Weekly"),
    MONTHLY("Monthly")
}

sealed class Repetition: Parcelable {
    companion object {
        const val NEVER_REPEATED_DISPLAY_TEXT = "Never"

        operator fun invoke(timePeriod: RepeatPeriod, indexOfRepetition: List<Int>): Repetition {
            when (timePeriod) {
                RepeatPeriod.WEEKLY -> {
                    if (indexOfRepetition.any { it < 1 }) {
                        throw IllegalArgumentException("Cannot accept any indexes with values less than 1")
                    } else if (indexOfRepetition.any { it > 7 }) {
                        throw IllegalArgumentException("Cannot accept any indexes with values greater than 7")
                    }
                }

                RepeatPeriod.MONTHLY -> {
                    if (indexOfRepetition.any { it < 1 }) {
                        throw IllegalArgumentException("Cannot accept any indexes with values less than 1")
                    } else if (indexOfRepetition.any { it > 31 }) {
                        throw IllegalArgumentException("Cannot accept any indexes with values greater than 31")
                    }
                }
            }

            return Impl(timePeriod, indexOfRepetition.distinct())
        }
    }

    /**
     * The days in which a transaction is repeated
     *
     * @property timePeriod RepeatPeriod.WEEKLY if repeated weekly, or RepeatPeriod.MONTHLY if repeated monthly
     * @property indexOfRepetition A list of days when the transaction is repeated. If weekly, 1->7 used for days of the weeks. If monthly, 1->31 used for days of the month.
     */
    @Parcelize
    private data class Impl(val timePeriod: RepeatPeriod, val indexOfRepetition: List<Int>): Repetition() {
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
    }
}
