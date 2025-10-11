package com.readingjournal.app.data.database.dao

import androidx.room.*
import com.readingjournal.app.data.model.JournalEntry
import kotlinx.coroutines.flow.Flow

@Dao
interface JournalDao {
    @Query("SELECT * FROM journal_entries ORDER BY dateCreated DESC")
    fun getAllEntries(): Flow<List<JournalEntry>>

    @Query("SELECT * FROM journal_entries WHERE bookId = :bookId ORDER BY dateCreated DESC")
    fun getEntriesForBook(bookId: String): Flow<List<JournalEntry>>

    @Query("SELECT * FROM journal_entries WHERE id = :id")
    suspend fun getEntryById(id: String): JournalEntry?

    @Query("SELECT * FROM journal_entries WHERE isFavorite = 1 ORDER BY dateCreated DESC")
    fun getFavoriteEntries(): Flow<List<JournalEntry>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEntry(entry: JournalEntry)

    @Update
    suspend fun updateEntry(entry: JournalEntry)

    @Delete
    suspend fun deleteEntry(entry: JournalEntry)

    @Query("SELECT COUNT(*) FROM journal_entries")
    fun getEntriesCount(): Flow<Int>
}
