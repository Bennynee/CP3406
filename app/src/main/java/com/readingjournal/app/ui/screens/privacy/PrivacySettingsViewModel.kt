package com.readingjournal.app.ui.screens.privacy

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import dagger.hilt.android.lifecycle.HiltViewModel

@HiltViewModel
class PrivacySettingsViewModel @Inject constructor() : ViewModel() {
    
    private val _uiState = MutableStateFlow(PrivacySettingsUiState())
    val uiState: StateFlow<PrivacySettingsUiState> = _uiState.asStateFlow()
    
    fun updateShareStatistics(enabled: Boolean) {
        _uiState.update { it.copy(shareStatistics = enabled) }
    }
    
    fun updateShareRatings(enabled: Boolean) {
        _uiState.update { it.copy(shareRatings = enabled) }
    }
    
    fun updateShareProgress(enabled: Boolean) {
        _uiState.update { it.copy(shareProgress = enabled) }
    }
    
    fun updatePrivateProfile(enabled: Boolean) {
        _uiState.update { it.copy(privateProfile = enabled) }
    }
    
    fun updateHideActivity(enabled: Boolean) {
        _uiState.update { it.copy(hideActivity = enabled) }
    }
    
    fun downloadData() {
        viewModelScope.launch {
            // In a real app, trigger data download
            _uiState.update { it.copy(downloadStatus = "Data download initiated") }
        }
    }
    
    fun showDeleteAccountDialog() {
        _uiState.update { it.copy(showDeleteDialog = true) }
    }
    
    fun hideDeleteAccountDialog() {
        _uiState.update { it.copy(showDeleteDialog = false) }
    }
    
    fun deleteAccount() {
        viewModelScope.launch {
            // In a real app, delete account and all data
            _uiState.update { it.copy(deleteStatus = "Account deletion initiated") }
        }
    }
    
    fun saveSettings() {
        viewModelScope.launch {
            // In a real app, save to SharedPreferences or database
        }
    }
}

data class PrivacySettingsUiState(
    val shareStatistics: Boolean = false,
    val shareRatings: Boolean = true,
    val shareProgress: Boolean = true,
    val privateProfile: Boolean = false,
    val hideActivity: Boolean = false,
    val showDeleteDialog: Boolean = false,
    val downloadStatus: String? = null,
    val deleteStatus: String? = null
)

