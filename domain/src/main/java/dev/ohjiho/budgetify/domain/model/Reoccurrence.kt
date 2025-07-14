package dev.ohjiho.budgetify.domain.model

import android.os.Parcelable
import dev.ohjiho.budgetify.utils.data.getLocale
import dev.ohjiho.budgetify.utils.ui.ordinal
import kotlinx.parcelize.Parcelize
import java.time.DayOfWeek
import java.time.format.TextStyle

/**
 * The days in which a transaction is repeated
 *
 * @property interval Interval.WEEKLY if repeated weekly, Interval.BIWEEKLY if repeated every 2 weeks, or Interval.MONTHLY if repeated monthly
 * @property indexOfRepetition A set of days when the transaction is repeated. If weekly or biweekly, 1->7 used for days of the weeks. If monthly, 1->31 used for days of the month.
 */
@Parcelize
data class Reoccurrence(val interval: Interval, val indexOfRepetition: Set<Int>) : Parcelable {

//    fun getRepetition(yearMonth: YearMonth): List<LocalDate> {
//        return when (interval) {
//            Interval.WEEKLY -> {
//                val repeatedDates = mutableListOf<LocalDate>()
//                val daysOfWeek = indexOfRepetition.map { DayOfWeek.of(it) }
//
//                for (dow in daysOfWeek) {
//                    var date = yearMonth.atDay(1).with(TemporalAdjusters.dayOfWeekInMonth(1, dow))
//                    do {
//                        repeatedDates.add(date)
//                        date = date.plusWeeks(1)
//                    } while (YearMonth.from(date).equals(yearMonth))
//                }
//
//                repeatedDates
//            }
//
//            Interval.MONTHLY -> {
//                indexOfRepetition.map { if (it > yearMonth.lengthOfMonth()) yearMonth.lengthOfMonth() else it }.distinct()
//                    .map { yearMonth.atDay(it) }
//            }
//        }
//    }

    override fun toString(): String {
        val timePeriodDisplayText = interval.toString()
        val requiresThe = if (interval == Interval.MONTHLY) "the " else ""
        val repeatedDays = if (interval == Interval.MONTHLY)
            indexOfRepetition.map { it.ordinal() }
        else indexOfRepetition.map {
            DayOfWeek.of(it).getDisplayName(TextStyle.SHORT, getLocale())
        }

        return "$timePeriodDisplayText on $requiresThe${repeatedDays.joinToString()}"
    }

    companion object {
        const val NEVER_REPEATED_DISPLAY_TEXT = "Never"

        fun with(interval: Interval, indexOfRepetition: Set<Int>): Reoccurrence {
            if (indexOfRepetition.isEmpty()) {
                throw  IllegalArgumentException("Index of Repetition cannot be empty")
            }

            when (interval) {
                Interval.WEEKLY, Interval.BIWEEKLY -> {
                    if (indexOfRepetition.any { it < 1 }) {
                        throw IllegalArgumentException("Cannot accept any indexes with values less than 1")
                    } else if (indexOfRepetition.any { it > 7 }) {
                        throw IllegalArgumentException("Cannot accept any indexes with values greater than 7")
                    }
                }

                Interval.MONTHLY -> {
                    if (indexOfRepetition.any { it < 1 }) {
                        throw IllegalArgumentException("Cannot accept any indexes with values less than 1")
                    } else if (indexOfRepetition.any { it > 31 }) {
                        throw IllegalArgumentException("Cannot accept any indexes with values greater than 31")
                    }
                }
            }

            return Reoccurrence(interval, indexOfRepetition)
        }
    }
}

enum class Interval {
    WEEKLY, BIWEEKLY, MONTHLY;

    override fun toString(): String {
        return when (this) {
            WEEKLY -> "Weekly"
            BIWEEKLY -> "Biweekly"
            MONTHLY -> "Monthly"
        }
    }
}