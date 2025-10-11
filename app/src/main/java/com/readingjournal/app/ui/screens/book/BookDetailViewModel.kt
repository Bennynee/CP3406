package com.readingjournal.app.ui.screens.book

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.readingjournal.app.data.model.Book
import com.readingjournal.app.data.repository.BookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class BookDetailViewModel @Inject constructor(
    private val bookRepository: BookRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(BookDetailUiState())
    val uiState: StateFlow<BookDetailUiState> = _uiState.asStateFlow()
    
    private var originalBook: Book? = null
    
    fun loadBook(bookId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                val book = bookRepository.getBookById(bookId)
                if (book != null) {
                    originalBook = book
                    _uiState.update { 
                        it.copy(
                            book = book,
                            isEditMode = false,
                            isLoading = false,
                            errorMessage = null
                        ) 
                    }
                } else {
                    _uiState.update { 
                        it.copy(
                            isLoading = false,
                            errorMessage = "Book not found"
                        ) 
                    }
                }
            } catch (e: Exception) {
                _uiState.update { 
                    it.copy(
                        isLoading = false,
                        errorMessage = "Failed to load book: ${e.message}"
                    ) 
                }
            }
        }
    }
    
    fun enableEdit() {
        _uiState.update { it.copy(isEditMode = true) }
    }
    
    fun cancelEdit() {
        originalBook?.let { book ->
            _uiState.update { 
                it.copy(
                    book = book,
                    isEditMode = false
                ) 
            }
        }
    }
    
    fun updateBook(updatedBook: Book) {
        _uiState.update { it.copy(book = updatedBook) }
    }
    
    fun saveBook() {
        viewModelScope.launch {
            _uiState.value.book?.let { book ->
                bookRepository.updateBook(book)
                originalBook = book
                _uiState.update { it.copy(isEditMode = false) }
            }
        }
    }
    
    fun toggleCompletion() {
        viewModelScope.launch {
            _uiState.value.book?.let { book ->
                val updatedBook = book.copy(
                    isCompleted = !book.isCompleted,
                    dateCompleted = if (!book.isCompleted) LocalDateTime.now() else null
                )
                bookRepository.updateBook(updatedBook)
                _uiState.update { it.copy(book = updatedBook) }
                originalBook = updatedBook
            }
        }
    }
    
    fun deleteBook() {
        viewModelScope.launch {
            _uiState.value.book?.let { book ->
                bookRepository.deleteBook(book)
            }
        }
    }
}

data class BookDetailUiState(
    val book: Book? = null,
    val isEditMode: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

