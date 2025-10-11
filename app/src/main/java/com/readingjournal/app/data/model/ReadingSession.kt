package com.readingjournal.app.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(
    tableName = "reading_sessions",
    foreignKeys = [
        ForeignKey(
            entity = Book::class,
            parentColumns = ["id"],
            childColumns = ["bookId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ReadingSession(
    @PrimaryKey
    val id: String,
    val bookId: String,
    val startTime: LocalDateTime,
    val endTime: LocalDateTime? = null,
    val pagesRead: Int = 0,
    val durationMinutes: Int = 0
)
