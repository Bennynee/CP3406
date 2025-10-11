package com.readingjournal.app.ui.screens.preferences

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import dagger.hilt.android.lifecycle.HiltViewModel

@HiltViewModel
class ReadingPreferencesViewModel @Inject constructor() : ViewModel() {
    
    private val _uiState = MutableStateFlow(ReadingPreferencesUiState())
    val uiState: StateFlow<ReadingPreferencesUiState> = _uiState.asStateFlow()
    
    fun updateBooksPerMonth(delta: Int) {
        _uiState.value = _uiState.value.copy(
            booksPerMonth = (_uiState.value.booksPerMonth + delta).coerceIn(1, 50)
        )
    }
    
    fun updatePagesPerDay(delta: Int) {
        _uiState.value = _uiState.value.copy(
            pagesPerDay = (_uiState.value.pagesPerDay + delta).coerceIn(1, 500)
        )
    }
    
    fun updateEnableReminders(enabled: Boolean) {
        _uiState.value = _uiState.value.copy(enableReminders = enabled)
    }
    
    fun updateShowProgressPercentage(enabled: Boolean) {
        _uiState.value = _uiState.value.copy(showProgressPercentage = enabled)
    }
    
    fun updateAutoSaveSessions(enabled: Boolean) {
        _uiState.value = _uiState.value.copy(autoSaveSessions = enabled)
    }
    
    fun updateEnableStatistics(enabled: Boolean) {
        _uiState.value = _uiState.value.copy(enableStatistics = enabled)
    }
    
    fun updateDailyReminder(enabled: Boolean) {
        _uiState.value = _uiState.value.copy(dailyReminder = enabled)
    }
    
    fun updateGoalNotifications(enabled: Boolean) {
        _uiState.value = _uiState.value.copy(goalNotifications = enabled)
    }
    
    fun updateRecommendationNotifications(enabled: Boolean) {
        _uiState.value = _uiState.value.copy(recommendationNotifications = enabled)
    }
    
    fun savePreferences() {
        viewModelScope.launch {
            // In a real app, save to SharedPreferences or database
            // For now, just update the state
        }
    }
}

data class ReadingPreferencesUiState(
    val booksPerMonth: Int = 3,
    val pagesPerDay: Int = 20,
    val enableReminders: Boolean = true,
    val showProgressPercentage: Boolean = true,
    val autoSaveSessions: Boolean = true,
    val enableStatistics: Boolean = true,
    val dailyReminder: Boolean = true,
    val goalNotifications: Boolean = true,
    val recommendationNotifications: Boolean = true
)

