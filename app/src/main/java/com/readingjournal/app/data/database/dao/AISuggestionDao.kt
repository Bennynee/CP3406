package com.readingjournal.app.data.database.dao

import androidx.room.*
import com.readingjournal.app.data.model.AISuggestion
import kotlinx.coroutines.flow.Flow

@Dao
interface AISuggestionDao {
    @Query("SELECT * FROM ai_suggestions ORDER BY priority DESC, dateCreated DESC")
    fun getAllSuggestions(): Flow<List<AISuggestion>>

    @Query("SELECT * FROM ai_suggestions WHERE isRead = 0 ORDER BY priority DESC, dateCreated DESC")
    fun getUnreadSuggestions(): Flow<List<AISuggestion>>

    @Query("SELECT * FROM ai_suggestions WHERE type = :type ORDER BY priority DESC, dateCreated DESC")
    fun getSuggestionsByType(type: String): Flow<List<AISuggestion>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSuggestion(suggestion: AISuggestion)

    @Update
    suspend fun updateSuggestion(suggestion: AISuggestion)

    @Delete
    suspend fun deleteSuggestion(suggestion: AISuggestion)

    @Query("UPDATE ai_suggestions SET isRead = 1 WHERE id = :id")
    suspend fun markAsRead(id: String)
}
