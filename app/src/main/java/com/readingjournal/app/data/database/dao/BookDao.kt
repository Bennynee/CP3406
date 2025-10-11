package com.readingjournal.app.data.database.dao

import androidx.room.*
import com.readingjournal.app.data.model.Book
import kotlinx.coroutines.flow.Flow

@Dao
interface BookDao {
    @Query("SELECT * FROM books ORDER BY dateAdded DESC")
    fun getAllBooks(): Flow<List<Book>>

    @Query("SELECT * FROM books WHERE isCompleted = 0 AND currentPage > 0 ORDER BY dateAdded DESC")
    fun getCurrentlyReadingBooks(): Flow<List<Book>>

    @Query("SELECT * FROM books WHERE isCompleted = 1 ORDER BY dateCompleted DESC")
    fun getCompletedBooks(): Flow<List<Book>>

    @Query("SELECT * FROM books WHERE id = :id")
    suspend fun getBookById(id: String): Book?

    @Query("SELECT * FROM books WHERE title LIKE '%' || :query || '%' OR author LIKE '%' || :query || '%'")
    fun searchBooks(query: String): Flow<List<Book>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBook(book: Book)

    @Update
    suspend fun updateBook(book: Book)

    @Delete
    suspend fun deleteBook(book: Book)

    @Query("SELECT COUNT(*) FROM books WHERE isCompleted = 1")
    fun getCompletedBooksCount(): Flow<Int>

    @Query("SELECT SUM(totalPages) FROM books WHERE isCompleted = 1")
    fun getTotalPagesRead(): Flow<Int?>

    @Query("SELECT AVG(rating) FROM books WHERE rating > 0")
    fun getAverageRating(): Flow<Float?>
}
