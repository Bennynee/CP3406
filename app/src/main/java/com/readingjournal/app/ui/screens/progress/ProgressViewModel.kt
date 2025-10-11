package com.readingjournal.app.ui.screens.progress

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.readingjournal.app.data.repository.BookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProgressViewModel @Inject constructor(
    private val bookRepository: BookRepository
) : ViewModel() {
    
    val uiState: StateFlow<ProgressUiState> = combine(
        bookRepository.getCompletedBooksCount(),
        bookRepository.getTotalPagesRead(),
        bookRepository.getAverageRating()
    ) { totalBooks, totalPages, averageRating ->
        ProgressUiState(
            totalBooks = totalBooks,
            totalPagesRead = totalPages ?: 0,
            averageRating = averageRating ?: 0f,
            totalReadingTime = 45 // Mock data - in real app, calculate from reading sessions
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ProgressUiState()
    )
}

data class ProgressUiState(
    val totalBooks: Int = 0,
    val totalPagesRead: Int = 0,
    val averageRating: Float = 0f,
    val totalReadingTime: Int = 0
)
