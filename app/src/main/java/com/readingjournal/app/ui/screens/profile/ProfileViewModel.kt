package com.readingjournal.app.ui.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.readingjournal.app.data.repository.BookRepository
import com.readingjournal.app.data.repository.JournalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val bookRepository: BookRepository,
    private val journalRepository: JournalRepository
) : ViewModel() {
    
    val uiState = kotlinx.coroutines.flow.MutableStateFlow(ProfileUiState())
    
    private val _navigationEvent = kotlinx.coroutines.flow.MutableSharedFlow<String>()
    val navigationEvent = _navigationEvent.asSharedFlow()
    
    fun navigateToReadingPreferences() {
        viewModelScope.launch {
            _navigationEvent.emit("reading_preferences")
        }
    }
    
    fun navigateToAISuggestions() {
        viewModelScope.launch {
            _navigationEvent.emit("ai_suggestions")
        }
    }
    
    fun navigateToSocialFeatures() {
        viewModelScope.launch {
            _navigationEvent.emit("social_features")
        }
    }
    
    fun navigateToExportData() {
        viewModelScope.launch {
            _navigationEvent.emit("export_data")
        }
    }
    
    fun navigateToPrivacySettings() {
        viewModelScope.launch {
            _navigationEvent.emit("privacy_settings")
        }
    }
    
    fun exportData() {
        viewModelScope.launch {
            // In a real app, this would export data to a file
            // For now, just log that export was requested
            try {
                val books = bookRepository.getAllBooks()
                val entries = journalRepository.getAllEntries()
                // TODO: Implement actual data export (CSV, JSON, etc.)
                uiState.value = uiState.value.copy(exportStatus = "Data export feature coming soon!")
            } catch (e: Exception) {
                uiState.value = uiState.value.copy(exportStatus = "Export failed: ${e.message}")
            }
        }
    }
}

data class ProfileUiState(
    val userName: String = "Reading Enthusiast",
    val memberSince: String = "January 2024",
    val profileImageUrl: String? = null,
    val exportStatus: String? = null
)
