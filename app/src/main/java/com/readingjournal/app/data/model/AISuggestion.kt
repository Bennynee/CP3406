package com.readingjournal.app.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "ai_suggestions")
data class AISuggestion(
    @PrimaryKey
    val id: String,
    val type: SuggestionType,
    val title: String,
    val description: String,
    val bookId: String? = null,
    val isRead: Boolean = false,
    val dateCreated: LocalDateTime = LocalDateTime.now(),
    val priority: Int = 0
)

enum class SuggestionType {
    BOOK_RECOMMENDATION,
    READING_GOAL,
    READING_HABIT,
    JOURNAL_PROMPT,
    BOOK_CLUB
}
