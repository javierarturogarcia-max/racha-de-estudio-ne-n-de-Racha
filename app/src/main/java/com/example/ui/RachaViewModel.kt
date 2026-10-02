package com.example.ui

import android.app.Application
import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.MotivationalQuote
import com.example.data.MotivationalQuotes
import com.example.data.db.RachaDatabase
import com.example.data.repository.StudyRepository
import com.example.util.DateUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class DayStatusItem(
    val dateIso: String,
    val dayOfWeek: String,
    val dayNumber: String,
    val isToday: Boolean,
    val isYesterday: Boolean,
    val isStudied: Boolean
)

data class RachaUiState(
    val currentStreak: Int = 0,
    val bestStreak: Int = 0,
    val totalDaysStudied: Int = 0,
    val isStudiedToday: Boolean = false,
    val last7Days: List<DayStatusItem> = emptyList(),
    val currentQuote: MotivationalQuote = MotivationalQuotes.quotes.first(),
    val quoteIndex: Int = 0,
    val celebrationTrigger: Long = 0L,
    val studiedDatesCountInLast7Days: Int = 0
)

class RachaViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: StudyRepository by lazy {
        val database = RachaDatabase.getInstance(application)
        StudyRepository(database.studyDayDao())
    }

    private val _uiState = MutableStateFlow(
        RachaUiState(
            currentQuote = MotivationalQuotes.getRandomQuote().first
        )
    )
    val uiState: StateFlow<RachaUiState> = _uiState.asStateFlow()

    init {
        // Pick an initial random quote on launch
        val initial = MotivationalQuotes.getRandomQuote()
        _uiState.update { it.copy(currentQuote = initial.first, quoteIndex = initial.second) }

        // Observe Room DB updates reactively
        repository.allStudyDays.onEach { entities ->
            val studiedSet = entities.map { it.dateIso }.toSet()
            val todayIso = DateUtils.getTodayIso()
            val isTodayStudied = studiedSet.contains(todayIso)

            val currentStreak = DateUtils.calculateCurrentStreak(studiedSet)
            val bestStreak = maxOf(currentStreak, DateUtils.calculateBestStreak(studiedSet))
            val totalDays = studiedSet.size

            val last7 = DateUtils.getLast7Days().map { dayInfo ->
                DayStatusItem(
                    dateIso = dayInfo.dateIso,
                    dayOfWeek = dayInfo.dayOfWeek,
                    dayNumber = dayInfo.dayNumber,
                    isToday = dayInfo.isToday,
                    isYesterday = dayInfo.isYesterday,
                    isStudied = studiedSet.contains(dayInfo.dateIso)
                )
            }

            val countIn7 = last7.count { it.isStudied }

            _uiState.update { current ->
                current.copy(
                    currentStreak = currentStreak,
                    bestStreak = bestStreak,
                    totalDaysStudied = totalDays,
                    isStudiedToday = isTodayStudied,
                    last7Days = last7,
                    studiedDatesCountInLast7Days = countIn7
                )
            }
        }.launchIn(viewModelScope)
    }

    /**
     * Primary action: "Hoy sí estudié"
     * Saves today's study date to phone database, triggers celebration and updates quote.
     */
    fun onStudyTodayClicked() {
        val currentState = _uiState.value
        if (currentState.isStudiedToday) {
            // Already logged today; provide gentle haptic and change quote
            triggerHaptic()
            nextQuote()
            return
        }

        viewModelScope.launch {
            repository.recordStudyToday()
            triggerHaptic(isMajor = true)
            val next = MotivationalQuotes.getRandomQuote(currentState.quoteIndex)
            _uiState.update {
                it.copy(
                    celebrationTrigger = System.currentTimeMillis(),
                    currentQuote = next.first,
                    quoteIndex = next.second
                )
            }
        }
    }

    /**
     * Undoes today's study record in case of misclick
     */
    fun onUndoTodayClicked() {
        viewModelScope.launch {
            repository.removeStudyToday()
            triggerHaptic()
        }
    }

    /**
     * Cycles to another motivational quote
     */
    fun nextQuote() {
        val next = MotivationalQuotes.getRandomQuote(_uiState.value.quoteIndex)
        _uiState.update {
            it.copy(
                currentQuote = next.first,
                quoteIndex = next.second
            )
        }
        triggerHaptic()
    }

    /**
     * Toggles a specific date from the 7-day list
     */
    fun toggleDay(dateIso: String) {
        viewModelScope.launch {
            repository.toggleStudyDay(dateIso)
            triggerHaptic()
        }
    }

    private fun triggerHaptic(isMajor: Boolean = false) {
        try {
            val context = getApplication<Application>()
            val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
                vibratorManager?.defaultVibrator
            } else {
                @Suppress("DEPRECATION")
                context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
            }

            if (vibrator != null && vibrator.hasVibrator()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    val effect = if (isMajor) {
                        VibrationEffect.createOneShot(80, VibrationEffect.DEFAULT_AMPLITUDE)
                    } else {
                        VibrationEffect.createOneShot(30, 150)
                    }
                    vibrator.vibrate(effect)
                } else {
                    @Suppress("DEPRECATION")
                    vibrator.vibrate(if (isMajor) 80 else 30)
                }
            }
        } catch (_: Exception) {
            // Ignore if vibration is restricted or unavailable
        }
    }
}
