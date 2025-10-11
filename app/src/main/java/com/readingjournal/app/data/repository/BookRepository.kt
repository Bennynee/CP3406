package com.readingjournal.app.data.repository

import com.readingjournal.app.data.database.dao.BookDao
import com.readingjournal.app.data.model.Book
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BookRepository @Inject constructor(
    private val bookDao: BookDao
) {
    fun getAllBooks(): Flow<List<Book>> = bookDao.getAllBooks()
    
    fun getCurrentlyReadingBooks(): Flow<List<Book>> = bookDao.getCurrentlyReadingBooks()
    
    fun getCompletedBooks(): Flow<List<Book>> = bookDao.getCompletedBooks()
    
    suspend fun getBookById(id: String): Book? = bookDao.getBookById(id)
    
    fun searchBooks(query: String): Flow<List<Book>> = bookDao.searchBooks(query)
    
    suspend fun insertBook(book: Book) = bookDao.insertBook(book)
    
    suspend fun updateBook(book: Book) = bookDao.updateBook(book)
    
    suspend fun deleteBook(book: Book) = bookDao.deleteBook(book)
    
    fun getCompletedBooksCount(): Flow<Int> = bookDao.getCompletedBooksCount()
    
    fun getTotalPagesRead(): Flow<Int?> = bookDao.getTotalPagesRead()
    
    fun getAverageRating(): Flow<Float?> = bookDao.getAverageRating()
}
