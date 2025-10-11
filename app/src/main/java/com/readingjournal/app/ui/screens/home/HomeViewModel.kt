package com.readingjournal.app.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.readingjournal.app.data.model.AISuggestion
import com.readingjournal.app.data.model.Book
import com.readingjournal.app.data.repository.BookRepository
import com.readingjournal.app.data.repository.JournalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val bookRepository: BookRepository,
    private val journalRepository: JournalRepository,
    private val aiSuggestionDao: com.readingjournal.app.data.database.dao.AISuggestionDao
) : ViewModel() {
    
    val uiState: StateFlow<HomeUiState> = combine(
        bookRepository.getCurrentlyReadingBooks(),
        bookRepository.getCompletedBooksCount(),
        bookRepository.getTotalPagesRead(),
        generateAISuggestions()
    ) { currentlyReading, booksRead, pagesRead, suggestions ->
        HomeUiState(
            currentlyReadingBooks = currentlyReading,
            booksReadThisMonth = booksRead, // Simplified for demo
            pagesReadThisMonth = pagesRead ?: 0,
            readingStreak = 7, // Mock data
            aiSuggestions = suggestions
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = HomeUiState()
    )
    
    private fun generateAISuggestions(): Flow<List<AISuggestion>> = aiSuggestionDao.getUnreadSuggestions()
        .map { suggestions -> suggestions.take(5) } // Show top 5 suggestions
}

data class HomeUiState(
    val currentlyReadingBooks: List<Book> = emptyList(),
    val booksReadThisMonth: Int = 0,
    val pagesReadThisMonth: Int = 0,
    val readingStreak: Int = 0,
    val aiSuggestions: List<AISuggestion> = emptyList()
)
