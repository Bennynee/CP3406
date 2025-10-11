package com.readingjournal.app.ui.screens.book

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.readingjournal.app.data.model.Book
import com.readingjournal.app.data.repository.BookRepository
import com.readingjournal.app.utils.IdGenerator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class AddBookViewModel @Inject constructor(
    private val bookRepository: BookRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(AddBookUiState())
    val uiState: StateFlow<AddBookUiState> = _uiState.asStateFlow()
    
    fun updateTitle(title: String) {
        _uiState.update { it.copy(title = title) }
    }
    
    fun updateAuthor(author: String) {
        _uiState.update { it.copy(author = author) }
    }
    
    fun updateTotalPages(pages: String) {
        _uiState.update { it.copy(totalPages = pages) }
    }
    
    fun updateCurrentPage(page: String) {
        _uiState.update { it.copy(currentPage = page) }
    }
    
    fun updateRating(rating: String) {
        _uiState.update { it.copy(rating = rating) }
    }
    
    fun updateNotes(notes: String) {
        _uiState.update { it.copy(notes = notes) }
    }
    
    fun updateGenre(genre: String) {
        _uiState.update { it.copy(genre = genre) }
    }
    
    fun updateCoverImageUrl(url: String) {
        _uiState.update { it.copy(coverImageUrl = url) }
    }
    
    fun saveBook() {
        viewModelScope.launch {
            val state = _uiState.value
            if (state.title.isBlank() || state.author.isBlank()) {
                _uiState.update { it.copy(errorMessage = "Title and author are required") }
                return@launch
            }
            
            _uiState.update { it.copy(isLoading = true, errorMessage = "") }
            
            try {
                val book = Book(
                    id = IdGenerator.generateId(),
                    title = state.title.trim(),
                    author = state.author.trim(),
                    totalPages = state.totalPages.toIntOrNull() ?: 0,
                    currentPage = state.currentPage.toIntOrNull() ?: 0,
                    rating = state.rating.toFloatOrNull() ?: 0f,
                    notes = state.notes.trim(),
                    isCompleted = false,
                    coverImageUrl = state.coverImageUrl.ifEmpty { null },
                    isbn = null,
                    genre = state.genre.ifEmpty { null },
                    dateAdded = LocalDateTime.now()
                )
                
                bookRepository.insertBook(book)
                _uiState.update { 
                    it.copy(
                        isLoading = false,
                        title = "",
                        author = "",
                        totalPages = "",
                        currentPage = "",
                        rating = "",
                        notes = "",
                        genre = "",
                        coverImageUrl = ""
                    ) 
                }
            } catch (e: Exception) {
                _uiState.update { 
                    it.copy(
                        isLoading = false,
                        errorMessage = "Failed to save book: ${e.message}"
                    ) 
                }
            }
        }
    }
}

data class AddBookUiState(
    val title: String = "",
    val author: String = "",
    val totalPages: String = "",
    val currentPage: String = "0",
    val rating: String = "",
    val notes: String = "",
    val genre: String = "",
    val coverImageUrl: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String = ""
)

