package com.readingjournal.app.ui.screens.journalentry

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.readingjournal.app.data.model.JournalEntry
import com.readingjournal.app.data.repository.BookRepository
import com.readingjournal.app.data.repository.JournalRepository
import com.readingjournal.app.utils.IdGenerator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class AddJournalEntryViewModel @Inject constructor(
    private val journalRepository: JournalRepository,
    private val bookRepository: BookRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(AddJournalEntryUiState())
    val uiState: StateFlow<AddJournalEntryUiState> = _uiState.asStateFlow()
    
    init {
        loadBooks()
    }
    
    fun setBookId(bookId: String) {
        _uiState.update { it.copy(selectedBookId = bookId) }
    }
    
    fun loadBooks() {
        viewModelScope.launch {
            bookRepository.getAllBooks().collect { books ->
                _uiState.update { it.copy(books = books) }
            }
        }
    }
    
    fun updateTitle(title: String) {
        _uiState.update { it.copy(title = title) }
    }
    
    fun updateContent(content: String) {
        _uiState.update { it.copy(content = content) }
    }
    
    fun selectBook(bookId: String?) {
        _uiState.update { it.copy(selectedBookId = bookId) }
    }
    
    fun updateMood(mood: String) {
        _uiState.update { it.copy(mood = mood) }
    }
    
    fun updateTags(tags: String) {
        _uiState.update { it.copy(tagsText = tags) }
    }
    
    fun saveEntry() {
        viewModelScope.launch {
            val state = _uiState.value
            if (state.title.isBlank() || state.content.isBlank()) {
                _uiState.update { it.copy(errorMessage = "Title and content are required") }
                return@launch
            }
            
            // Book selection is optional - use a default book if none selected
            val bookId = state.selectedBookId ?: state.books.firstOrNull()?.id
            if (bookId == null && state.books.isNotEmpty()) {
                _uiState.update { it.copy(errorMessage = "Please select a book") }
                return@launch
            }
            
            if (bookId == null) {
                _uiState.update { it.copy(errorMessage = "No books available. Please add a book first.") }
                return@launch
            }
            
            _uiState.update { it.copy(isLoading = true, errorMessage = "") }
            
            try {
                val tags = state.tagsText.split(",").map { it.trim() }.filter { it.isNotEmpty() }
                val now = LocalDateTime.now()
                
                val entry = JournalEntry(
                    id = IdGenerator.generateId(),
                    bookId = bookId,
                    title = state.title.trim(),
                    content = state.content.trim(),
                    dateCreated = now,
                    dateModified = now,
                    mood = state.mood.ifEmpty { null },
                    tags = tags,
                    isFavorite = false
                )
                
                journalRepository.insertEntry(entry)
                _uiState.update { 
                    it.copy(
                        isLoading = false,
                        title = "",
                        content = "",
                        mood = "",
                        tagsText = "",
                        selectedBookId = null
                    ) 
                }
            } catch (e: Exception) {
                _uiState.update { 
                    it.copy(
                        isLoading = false,
                        errorMessage = "Failed to save entry: ${e.message}"
                    ) 
                }
            }
        }
    }
}

data class AddJournalEntryUiState(
    val title: String = "",
    val content: String = "",
    val selectedBookId: String? = null,
    val books: List<com.readingjournal.app.data.model.Book> = emptyList(),
    val mood: String = "",
    val tagsText: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String = ""
)

