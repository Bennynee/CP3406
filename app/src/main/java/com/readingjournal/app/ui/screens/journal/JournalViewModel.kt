package com.readingjournal.app.ui.screens.journal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.readingjournal.app.data.model.JournalEntry
import com.readingjournal.app.data.repository.JournalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class JournalViewModel @Inject constructor(
    private val journalRepository: JournalRepository
) : ViewModel() {
    
    val uiState: StateFlow<JournalUiState> = journalRepository.getAllEntries()
        .map { entries -> JournalUiState(journalEntries = entries) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = JournalUiState()
        )
    
    fun toggleFavorite(entryId: String) {
        viewModelScope.launch {
            val entry = journalRepository.getEntryById(entryId)
            entry?.let {
                val updatedEntry = it.copy(isFavorite = !it.isFavorite)
                journalRepository.updateEntry(updatedEntry)
            }
        }
    }
}

data class JournalUiState(
    val journalEntries: List<JournalEntry> = emptyList()
)
