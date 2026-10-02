package com.example.util

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

data class DayInfo(
    val dateIso: String,      // "2026-10-02"
    val dayOfWeek: String,    // "VIE"
    val dayNumber: String,    // "2"
    val isToday: Boolean,
    val isYesterday: Boolean
)

object DateUtils {
    private val isoFormat: SimpleDateFormat
        get() = SimpleDateFormat("yyyy-MM-dd", Locale.US)

    private val dayOfWeekFormat: SimpleDateFormat
        get() = SimpleDateFormat("EEE", Locale("es", "ES"))

    private val dayNumberFormat: SimpleDateFormat
        get() = SimpleDateFormat("d", Locale.US)

    fun getTodayIso(): String {
        return isoFormat.format(Date())
    }

    fun getYesterdayIso(): String {
        val cal = Calendar.getInstance()
        cal.add(Calendar.DAY_OF_YEAR, -1)
        return isoFormat.format(cal.time)
    }

    /**
     * Returns the last 7 days starting from 6 days ago up to Today (7 days total).
     */
    fun getLast7Days(): List<DayInfo> {
        val list = mutableListOf<DayInfo>()
        val todayCal = Calendar.getInstance()
        val todayIso = isoFormat.format(todayCal.time)

        val yesterdayCal = Calendar.getInstance()
        yesterdayCal.add(Calendar.DAY_OF_YEAR, -1)
        val yesterdayIso = isoFormat.format(yesterdayCal.time)

        for (i in 6 downTo 0) {
            val cal = Calendar.getInstance()
            cal.add(Calendar.DAY_OF_YEAR, -i)
            val date = cal.time
            val iso = isoFormat.format(date)
            val dow = dayOfWeekFormat.format(date).replace(".", "").uppercase(Locale("es", "ES"))
            val num = dayNumberFormat.format(date)

            list.add(
                DayInfo(
                    dateIso = iso,
                    dayOfWeek = dow,
                    dayNumber = num,
                    isToday = iso == todayIso,
                    isYesterday = iso == yesterdayIso
                )
            )
        }
        return list
    }

    /**
     * Checks if dateB is the immediate calendar day after dateA (dateB = dateA + 1 day).
     */
    fun isConsecutive(earlierIso: String, laterIso: String): Boolean {
        return try {
            val cal = Calendar.getInstance()
            val earlierDate = isoFormat.parse(earlierIso) ?: return false
            cal.time = earlierDate
            cal.add(Calendar.DAY_OF_YEAR, 1)
            val nextIso = isoFormat.format(cal.time)
            nextIso == laterIso
        } catch (_: Exception) {
            false
        }
    }

    /**
     * Calculates the active consecutive study streak ending on either today or yesterday.
     * If user studied today, streak counts backwards consecutive days including today.
     * If user hasn't studied today yet, but studied yesterday, streak is alive and counts backwards from yesterday.
     * If user neither studied today nor yesterday, streak is 0.
     */
    fun calculateCurrentStreak(studiedDatesSet: Set<String>): Int {
        if (studiedDatesSet.isEmpty()) return 0

        val todayIso = getTodayIso()
        val yesterdayIso = getYesterdayIso()

        val startFromToday = studiedDatesSet.contains(todayIso)
        val startFromYesterday = studiedDatesSet.contains(yesterdayIso)

        if (!startFromToday && !startFromYesterday) {
            return 0
        }

        var streak = 0
        val cal = Calendar.getInstance()
        if (!startFromToday) {
            // Start checking from yesterday
            cal.add(Calendar.DAY_OF_YEAR, -1)
        }

        while (true) {
            val checkIso = isoFormat.format(cal.time)
            if (studiedDatesSet.contains(checkIso)) {
                streak++
                cal.add(Calendar.DAY_OF_YEAR, -1)
            } else {
                break
            }
        }

        return streak
    }

    /**
     * Calculates the historical best streak of consecutive days.
     */
    fun calculateBestStreak(studiedDatesSet: Set<String>): Int {
        if (studiedDatesSet.isEmpty()) return 0
        val sortedDates = studiedDatesSet.toList().sorted()
        var maxStreak = 1
        var current = 1

        for (i in 1 until sortedDates.size) {
            if (isConsecutive(sortedDates[i - 1], sortedDates[i])) {
                current++
                if (current > maxStreak) {
                    maxStreak = current
                }
            } else if (sortedDates[i - 1] != sortedDates[i]) {
                current = 1
            }
        }
        return maxStreak
    }

    fun formatFriendlyDate(isoDate: String): String {
        return try {
            val date = isoFormat.parse(isoDate) ?: return isoDate
            val friendlyFormat = SimpleDateFormat("d 'de' MMMM", Locale("es", "ES"))
            friendlyFormat.format(date)
        } catch (_: Exception) {
            isoDate
        }
    }
}
