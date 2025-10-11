package com.readingjournal.app.data.api

import com.readingjournal.app.data.model.Book
import com.readingjournal.app.utils.IdGenerator
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.time.LocalDateTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BookRepository @Inject constructor(
    private val bookApiService: BookApiService
) {
    
    fun searchBooks(query: String): Flow<List<Book>> = flow {
        try {
            val response = bookApiService.searchBooks(query)
            if (response.isSuccessful) {
                val books = response.body()?.items?.map { bookItem ->
                    convertToBook(bookItem)
                } ?: emptyList()
                emit(books)
            } else {
                emit(emptyList())
            }
        } catch (e: Exception) {
            emit(emptyList())
        }
    }
    
    fun getBookByIsbn(isbn: String): Flow<Book?> = flow {
        try {
            val response = bookApiService.getBookByIsbn("isbn:$isbn")
            if (response.isSuccessful) {
                val bookItem = response.body()?.items?.firstOrNull()
                val book = bookItem?.let { convertToBook(it) }
                emit(book)
            } else {
                emit(null)
            }
        } catch (e: Exception) {
            emit(null)
        }
    }
    
    fun getBookById(volumeId: String): Flow<Book?> = flow {
        try {
            val response = bookApiService.getBookById(volumeId)
            if (response.isSuccessful) {
                val bookItem = response.body()
                val book = bookItem?.let { convertToBook(BookItem(it.id, it.volumeInfo)) }
                emit(book)
            } else {
                emit(null)
            }
        } catch (e: Exception) {
            emit(null)
        }
    }
    
    private fun convertToBook(bookItem: BookItem): Book {
        val volumeInfo = bookItem.volumeInfo
        val isbn = volumeInfo.industryIdentifiers?.find { it.type == "ISBN_13" }?.identifier
            ?: volumeInfo.industryIdentifiers?.find { it.type == "ISBN_10" }?.identifier
        
        return Book(
            id = IdGenerator.generateId(),
            title = volumeInfo.title,
            author = volumeInfo.authors?.joinToString(", ") ?: "Unknown Author",
            totalPages = volumeInfo.pageCount ?: 0,
            currentPage = 0,
            rating = volumeInfo.averageRating ?: 0f,
            notes = volumeInfo.description ?: "",
            isCompleted = false,
            coverImageUrl = volumeInfo.imageLinks?.thumbnail?.replace("http://", "https://")
                ?: volumeInfo.imageLinks?.smallThumbnail?.replace("http://", "https://")
                ?: volumeInfo.imageLinks?.medium?.replace("http://", "https://")
                ?: volumeInfo.imageLinks?.large?.replace("http://", "https://"),
            isbn = isbn,
            genre = volumeInfo.categories?.firstOrNull(),
            dateAdded = LocalDateTime.now()
        )
    }
}
