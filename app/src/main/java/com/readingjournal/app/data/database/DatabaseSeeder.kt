package com.readingjournal.app.data.database

import android.content.Context
import android.content.SharedPreferences
import com.readingjournal.app.data.database.dao.AISuggestionDao
import com.readingjournal.app.data.database.dao.BookDao
import com.readingjournal.app.data.database.dao.JournalDao
import com.readingjournal.app.utils.BookCoverHelper
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DatabaseSeeder @Inject constructor(
    private val bookDao: BookDao,
    private val journalDao: JournalDao,
    private val aiSuggestionDao: AISuggestionDao,
    @ApplicationContext private val context: Context
) {
    
    private val SEEDED_KEY = "database_seeded"
    private val PREFS_NAME = "reading_journal_prefs"
    
    private val sharedPreferences: SharedPreferences
        get() = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    
    suspend fun seedIfNeeded() {
        val isSeeded = sharedPreferences.getBoolean(SEEDED_KEY, false)
        if (!isSeeded) {
            seedDatabase()
            sharedPreferences.edit().putBoolean(SEEDED_KEY, true).apply()
        } else {
            // Update existing book cover URLs if they have ISBNs
            updateBookCoverUrls()
        }
    }
    
    private suspend fun seedDatabase() {
        // Check if database is already populated
        val existingBooks = bookDao.getAllBooks().first()
        if (existingBooks.isNotEmpty()) {
            return
        }
        
        // Insert sample books
        SampleData.sampleBooks.forEach { book ->
            bookDao.insertBook(book)
        }
        
        // Insert sample journal entries
        SampleData.sampleJournalEntries.forEach { entry ->
            journalDao.insertEntry(entry)
        }
        
        // Insert sample AI suggestions
        SampleData.sampleAISuggestions.forEach { suggestion ->
            aiSuggestionDao.insertSuggestion(suggestion)
        }
    }
    
    private suspend fun updateBookCoverUrls() {
        val existingBooks = bookDao.getAllBooks().first()
        existingBooks.forEach { book ->
            // Update cover URL if book has ISBN and current URL is from old format
            if (book.isbn != null && book.coverImageUrl != null) {
                val oldUrlPattern = "books.google.com/books/publisher/content/images/frontcover"
                if (book.coverImageUrl.contains(oldUrlPattern)) {
                    val newCoverUrl = BookCoverHelper.getCoverImageUrl(book.isbn)
                    if (newCoverUrl != null) {
                        val updatedBook = book.copy(coverImageUrl = newCoverUrl)
                        bookDao.updateBook(updatedBook)
                    }
                }
            } else if (book.isbn != null && book.coverImageUrl.isNullOrBlank()) {
                // If book has ISBN but no cover URL, add one
                val newCoverUrl = BookCoverHelper.getCoverImageUrl(book.isbn)
                if (newCoverUrl != null) {
                    val updatedBook = book.copy(coverImageUrl = newCoverUrl)
                    bookDao.updateBook(updatedBook)
                }
            }
        }
    }
}

