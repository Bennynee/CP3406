package com.readingjournal.app.ui.screens.aisuggestions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.readingjournal.app.data.database.dao.AISuggestionDao
import com.readingjournal.app.data.model.AISuggestion
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AISuggestionsViewModel @Inject constructor(
    private val aiSuggestionDao: AISuggestionDao
) : ViewModel() {
    
    val uiState: StateFlow<AISuggestionsUiState> = aiSuggestionDao.getAllSuggestions()
        .map { suggestions ->
            AISuggestionsUiState(
                suggestions = suggestions.filter { !it.isRead }
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = AISuggestionsUiState()
        )
    
    fun dismissSuggestion(suggestionId: String) {
        viewModelScope.launch {
            aiSuggestionDao.markAsRead(suggestionId)
        }
    }
    
    fun handleSuggestionAction(suggestionId: String) {
        viewModelScope.launch {
            // Handle different suggestion types
            val suggestion = aiSuggestionDao.getAllSuggestions().first().find { it.id == suggestionId }
            suggestion?.let {
                when (it.type) {
                    com.readingjournal.app.data.model.SuggestionType.BOOK_RECOMMENDATION -> {
                        // Navigate to book search or detail
                    }
                    com.readingjournal.app.data.model.SuggestionType.READING_GOAL -> {
                        // Navigate to goal setting
                    }
                    com.readingjournal.app.data.model.SuggestionType.JOURNAL_PROMPT -> {
                        // Navigate to journal entry creation
                    }
                    com.readingjournal.app.data.model.SuggestionType.READING_HABIT -> {
                        // Show habit tips
                    }
                    com.readingjournal.app.data.model.SuggestionType.BOOK_CLUB -> {
                        // Navigate to book clubs
                    }
                }
                // Mark as read after action
                aiSuggestionDao.markAsRead(suggestionId)
            }
        }
    }
}

data class AISuggestionsUiState(
    val suggestions: List<AISuggestion> = emptyList()
)

