package com.readingjournal.app.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "books")
data class Book(
    @PrimaryKey
    val id: String,
    val title: String,
    val author: String,
    val totalPages: Int,
    val currentPage: Int = 0,
    val rating: Float = 0f,
    val notes: String = "",
    val isCompleted: Boolean = false,
    val coverImageUrl: String? = null,
    val isbn: String? = null,
    val genre: String? = null,
    val dateAdded: LocalDateTime = LocalDateTime.now(),
    val dateCompleted: LocalDateTime? = null
) {
    val progressPercentage: Float
        get() = if (totalPages > 0) (currentPage.toFloat() / totalPages.toFloat()) * 100f else 0f
    
    val isCurrentlyReading: Boolean
        get() = !isCompleted && currentPage > 0
}
