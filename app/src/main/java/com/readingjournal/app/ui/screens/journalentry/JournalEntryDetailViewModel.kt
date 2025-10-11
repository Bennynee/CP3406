package com.readingjournal.app.ui.screens.journalentry

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.readingjournal.app.data.model.JournalEntry
import com.readingjournal.app.data.repository.JournalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class JournalEntryDetailViewModel @Inject constructor(
    private val journalRepository: JournalRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(JournalEntryDetailUiState())
    val uiState: StateFlow<JournalEntryDetailUiState> = _uiState.asStateFlow()
    
    private var originalEntry: JournalEntry? = null
    
    fun loadEntry(entryId: String) {
        viewModelScope.launch {
            journalRepository.getEntryById(entryId)?.let { entry ->
                originalEntry = entry
                _uiState.update { 
                    it.copy(
                        entry = entry,
                        isEditMode = false
                    ) 
                }
            }
        }
    }
    
    fun enableEdit() {
        _uiState.update { it.copy(isEditMode = true) }
    }
    
    fun cancelEdit() {
        originalEntry?.let { entry ->
            _uiState.update { 
                it.copy(
                    entry = entry,
                    isEditMode = false
                ) 
            }
        }
    }
    
    fun updateEntry(updatedEntry: JournalEntry) {
        _uiState.update { it.copy(entry = updatedEntry) }
    }
    
    fun saveEntry() {
        viewModelScope.launch {
            _uiState.value.entry?.let { entry ->
                journalRepository.updateEntry(entry)
                originalEntry = entry
                _uiState.update { it.copy(isEditMode = false) }
            }
        }
    }
    
    fun toggleFavorite() {
        viewModelScope.launch {
            _uiState.value.entry?.let { entry ->
                val updatedEntry = entry.copy(isFavorite = !entry.isFavorite)
                journalRepository.updateEntry(updatedEntry)
                _uiState.update { it.copy(entry = updatedEntry) }
                originalEntry = updatedEntry
            }
        }
    }
    
    fun deleteEntry() {
        viewModelScope.launch {
            _uiState.value.entry?.let { entry ->
                journalRepository.deleteEntry(entry)
            }
        }
    }
}

data class JournalEntryDetailUiState(
    val entry: JournalEntry? = null,
    val isEditMode: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

