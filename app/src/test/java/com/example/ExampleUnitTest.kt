package com.example

import com.example.data.MotivationalQuotes
import com.example.util.DateUtils
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {
    @Test
    fun streak_emptyReturnsZero() {
        assertEquals(0, DateUtils.calculateCurrentStreak(emptySet()))
    }

    @Test
    fun streak_todayOnlyReturnsOne() {
        val today = DateUtils.getTodayIso()
        assertEquals(1, DateUtils.calculateCurrentStreak(setOf(today)))
    }

    @Test
    fun streak_yesterdayOnlyReturnsOne() {
        val yesterday = DateUtils.getYesterdayIso()
        assertEquals(1, DateUtils.calculateCurrentStreak(setOf(yesterday)))
    }

    @Test
    fun streak_todayAndYesterdayReturnsTwo() {
        val today = DateUtils.getTodayIso()
        val yesterday = DateUtils.getYesterdayIso()
        assertEquals(2, DateUtils.calculateCurrentStreak(setOf(today, yesterday)))
    }

    @Test
    fun last7Days_returnsSevenItems() {
        val list = DateUtils.getLast7Days()
        assertEquals(7, list.size)
        assertTrue(list.last().isToday)
    }

    @Test
    fun motivationalQuotes_randomReturnsDifferentOrValid() {
        val (quote1, idx1) = MotivationalQuotes.getRandomQuote()
        assertTrue(quote1.quote.isNotBlank())
        val (quote2, idx2) = MotivationalQuotes.getRandomQuote(idx1)
        assertNotEquals(idx1, idx2)
    }
}
