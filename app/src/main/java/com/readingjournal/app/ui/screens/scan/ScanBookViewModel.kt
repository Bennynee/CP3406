package com.readingjournal.app.ui.screens.scan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.readingjournal.app.data.api.BookRepository as ApiBookRepository
import com.readingjournal.app.data.model.Book
import com.readingjournal.app.data.repository.BookRepository as DatabaseBookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScanBookViewModel @Inject constructor(
    private val apiBookRepository: ApiBookRepository,
    private val databaseBookRepository: DatabaseBookRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(ScanBookUiState())
    val uiState: StateFlow<ScanBookUiState> = _uiState.asStateFlow()
    
    private var searchJob: kotlinx.coroutines.Job? = null
    
    fun updateSearchQuery(query: String) {
        _uiState.update { it.copy(searchQuery = query, errorMessage = "") }
        
        // Debounce search
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            if (query.isNotBlank()) {
                kotlinx.coroutines.delay(500) // Wait 500ms before searching
                searchBooks(query)
            } else {
                _uiState.update { it.copy(searchResults = emptyList()) }
            }
        }
    }
    
    fun startBarcodeScan() {
        // This will be handled by the composable that launches the scanner
        _uiState.update { it.copy(shouldStartScan = true) }
    }
    
    fun onScanComplete() {
        _uiState.update { it.copy(shouldStartScan = false) }
    }
    
    fun onBarcodeScanned(isbn: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = "") }
            
            apiBookRepository.getBookByIsbn(isbn).collect { book ->
                if (book != null) {
                    _uiState.update { 
                        it.copy(
                            searchResults = listOf(book),
                            isLoading = false
                        ) 
                    }
                } else {
                    _uiState.update { 
                        it.copy(
                            isLoading = false,
                            errorMessage = "Book not found. Please try manual search."
                        ) 
                    }
                }
            }
        }
    }
    
    fun addBookToLibrary(book: Book) {
        viewModelScope.launch {
            try {
                databaseBookRepository.insertBook(book)
            } catch (e: Exception) {
                _uiState.update { 
                    it.copy(errorMessage = "Failed to add book: ${e.message}") 
                }
            }
        }
    }
    
    private fun searchBooks(query: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = "") }
            
            apiBookRepository.searchBooks(query).collect { books ->
                _uiState.update { 
                    it.copy(
                        searchResults = books,
                        isLoading = false
                    ) 
                }
            }
        }
    }
}

data class ScanBookUiState(
    val searchQuery: String = "",
    val searchResults: List<Book> = emptyList(),
    val isLoading: Boolean = false,
    val shouldStartScan: Boolean = false,
    val errorMessage: String = ""
)
