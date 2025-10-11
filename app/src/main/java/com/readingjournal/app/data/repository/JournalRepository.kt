package com.readingjournal.app.data.repository

import com.readingjournal.app.data.database.dao.JournalDao
import com.readingjournal.app.data.model.JournalEntry
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class JournalRepository @Inject constructor(
    private val journalDao: JournalDao
) {
    fun getAllEntries(): Flow<List<JournalEntry>> = journalDao.getAllEntries()
    
    fun getEntriesForBook(bookId: String): Flow<List<JournalEntry>> = journalDao.getEntriesForBook(bookId)
    
    suspend fun getEntryById(id: String): JournalEntry? = journalDao.getEntryById(id)
    
    fun getFavoriteEntries(): Flow<List<JournalEntry>> = journalDao.getFavoriteEntries()
    
    suspend fun insertEntry(entry: JournalEntry) = journalDao.insertEntry(entry)
    
    suspend fun updateEntry(entry: JournalEntry) = journalDao.updateEntry(entry)
    
    suspend fun deleteEntry(entry: JournalEntry) = journalDao.deleteEntry(entry)
    
    fun getEntriesCount(): Flow<Int> = journalDao.getEntriesCount()
}
