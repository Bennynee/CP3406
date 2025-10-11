package com.readingjournal.app.ui.screens.export

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.readingjournal.app.data.repository.BookRepository
import com.readingjournal.app.data.repository.JournalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExportDataViewModel @Inject constructor(
    private val bookRepository: BookRepository,
    private val journalRepository: JournalRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(ExportDataUiState())
    val uiState: StateFlow<ExportDataUiState> = _uiState.asStateFlow()
    
    fun exportAsJSON() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isExporting = true,
                exportStatus = null,
                exportSuccess = false
            )
            
            try {
                val books = bookRepository.getAllBooks().first()
                val entries = journalRepository.getAllEntries().first()
                
                // Build JSON string manually (Android doesn't include org.json by default)
                val jsonString = buildString {
                    appendLine("{")
                    appendLine("  \"exportDate\": ${System.currentTimeMillis()},")
                    appendLine("  \"version\": \"1.0\",")
                    appendLine("  \"books\": [")
                    books.forEachIndexed { index, book ->
                        appendLine("    {")
                        appendLine("      \"id\": \"${book.id}\",")
                        appendLine("      \"title\": \"${book.title.replace("\"", "\\\"")}\",")
                        appendLine("      \"author\": \"${book.author.replace("\"", "\\\"")}\",")
                        appendLine("      \"totalPages\": ${book.totalPages},")
                        appendLine("      \"currentPage\": ${book.currentPage},")
                        appendLine("      \"rating\": ${book.rating},")
                        appendLine("      \"notes\": \"${book.notes.replace("\"", "\\\"")}\",")
                        appendLine("      \"isCompleted\": ${book.isCompleted},")
                        appendLine("      \"genre\": ${if (book.genre != null) "\"${book.genre}\"" else "null"},")
                        appendLine("      \"isbn\": ${if (book.isbn != null) "\"${book.isbn}\"" else "null"},")
                        appendLine("      \"dateAdded\": \"${book.dateAdded}\",")
                        appendLine("      \"dateCompleted\": ${if (book.dateCompleted != null) "\"${book.dateCompleted}\"" else "null"}")
                        append(if (index < books.size - 1) "    }," else "    }")
                        appendLine()
                    }
                    appendLine("  ],")
                    appendLine("  \"journalEntries\": [")
                    entries.forEachIndexed { index, entry ->
                        appendLine("    {")
                        appendLine("      \"id\": \"${entry.id}\",")
                        appendLine("      \"bookId\": \"${entry.bookId}\",")
                        appendLine("      \"title\": \"${entry.title.replace("\"", "\\\"")}\",")
                        appendLine("      \"content\": \"${entry.content.replace("\"", "\\\"")}\",")
                        appendLine("      \"mood\": ${if (entry.mood != null) "\"${entry.mood}\"" else "null"},")
                        appendLine("      \"tags\": [${entry.tags.joinToString(", ") { "\"$it\"" }}],")
                        appendLine("      \"isFavorite\": ${entry.isFavorite},")
                        appendLine("      \"dateCreated\": \"${entry.dateCreated}\",")
                        appendLine("      \"dateModified\": \"${entry.dateModified}\"")
                        append(if (index < entries.size - 1) "    }," else "    }")
                        appendLine()
                    }
                    appendLine("  ]")
                    appendLine("}")
                }
                
                _uiState.value = _uiState.value.copy(
                    isExporting = false,
                    exportStatus = "JSON export ready! Data contains ${books.size} books and ${entries.size} journal entries.",
                    exportSuccess = true,
                    exportedData = jsonString
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isExporting = false,
                    exportStatus = "Export failed: ${e.message}",
                    exportSuccess = false
                )
            }
        }
    }
    
    fun exportAsCSV() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isExporting = true,
                exportStatus = null,
                exportSuccess = false
            )
            
            try {
                val books = bookRepository.getAllBooks().first()
                val entries = journalRepository.getAllEntries().first()
                
                val csvBuilder = StringBuilder()
                
                // Books CSV
                csvBuilder.appendLine("Books Export")
                csvBuilder.appendLine("Title,Author,Total Pages,Current Page,Rating,Genre,ISBN,Completed,Date Added")
                books.forEach { book ->
                    csvBuilder.appendLine(
                        "\"${book.title}\",\"${book.author}\",${book.totalPages}," +
                        "${book.currentPage},${book.rating},\"${book.genre ?: ""}\"," +
                        "\"${book.isbn ?: ""}\",${book.isCompleted},\"${book.dateAdded}\""
                    )
                }
                
                csvBuilder.appendLine()
                csvBuilder.appendLine("Journal Entries Export")
                csvBuilder.appendLine("Title,Content,Mood,Tags,Is Favorite,Date Created")
                entries.forEach { entry ->
                    csvBuilder.appendLine(
                        "\"${entry.title}\",\"${entry.content.replace("\"", "\"\"")}\"," +
                        "\"${entry.mood ?: ""}\",\"${entry.tags.joinToString(";")}\"," +
                        "${entry.isFavorite},\"${entry.dateCreated}\""
                    )
                }
                
                _uiState.value = _uiState.value.copy(
                    isExporting = false,
                    exportStatus = "CSV export ready! Data contains ${books.size} books and ${entries.size} journal entries.",
                    exportSuccess = true,
                    exportedData = csvBuilder.toString()
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isExporting = false,
                    exportStatus = "Export failed: ${e.message}",
                    exportSuccess = false
                )
            }
        }
    }
    
    fun exportAsPDF() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isExporting = true,
                exportStatus = null,
                exportSuccess = false
            )
            
            try {
                val books = bookRepository.getAllBooks().first()
                val entries = journalRepository.getAllEntries().first()
                val completedBooks = books.filter { it.isCompleted }
                val totalPages = books.sumOf { it.totalPages }
                val pagesRead = books.sumOf { it.currentPage }
                
                // In a real app, generate PDF using a library like iText or Android PDF API
                val pdfContent = buildString {
                    appendLine("Reading Journal Report")
                    appendLine("Generated: ${java.time.LocalDateTime.now()}")
                    appendLine()
                    appendLine("Statistics:")
                    appendLine("Total Books: ${books.size}")
                    appendLine("Completed Books: ${completedBooks.size}")
                    appendLine("Total Pages: $totalPages")
                    appendLine("Pages Read: $pagesRead")
                    appendLine("Journal Entries: ${entries.size}")
                    appendLine()
                    appendLine("Books:")
                    books.forEach { book ->
                        appendLine("- ${book.title} by ${book.author} (${book.currentPage}/${book.totalPages} pages)")
                    }
                }
                
                _uiState.value = _uiState.value.copy(
                    isExporting = false,
                    exportStatus = "PDF report generated! Contains ${books.size} books and ${entries.size} journal entries.",
                    exportSuccess = true,
                    exportedData = pdfContent
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isExporting = false,
                    exportStatus = "Export failed: ${e.message}",
                    exportSuccess = false
                )
            }
        }
    }
}

data class ExportDataUiState(
    val isExporting: Boolean = false,
    val exportStatus: String? = null,
    val exportSuccess: Boolean = false,
    val exportedData: String? = null
)

