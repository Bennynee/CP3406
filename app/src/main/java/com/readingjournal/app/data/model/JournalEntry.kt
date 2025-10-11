package com.readingjournal.app.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(
    tableName = "journal_entries",
    foreignKeys = [
        ForeignKey(
            entity = Book::class,
            parentColumns = ["id"],
            childColumns = ["bookId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class JournalEntry(
    @PrimaryKey
    val id: String,
    val bookId: String,
    val title: String,
    val content: String,
    val dateCreated: LocalDateTime = LocalDateTime.now(),
    val dateModified: LocalDateTime = LocalDateTime.now(),
    val mood: String? = null,
    val tags: List<String> = emptyList(),
    val isFavorite: Boolean = false
)
