package com.readingjournal.app.data.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import android.content.Context
import com.readingjournal.app.data.database.dao.AISuggestionDao
import com.readingjournal.app.data.database.dao.BookDao
import com.readingjournal.app.data.database.dao.JournalDao
import com.readingjournal.app.data.model.AISuggestion
import com.readingjournal.app.data.model.Book
import com.readingjournal.app.data.model.JournalEntry
import com.readingjournal.app.data.model.ReadingSession

@Database(
    entities = [
        Book::class,
        JournalEntry::class,
        ReadingSession::class,
        AISuggestion::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class ReadingJournalDatabase : RoomDatabase() {
    abstract fun bookDao(): BookDao
    abstract fun journalDao(): JournalDao
    abstract fun aiSuggestionDao(): AISuggestionDao

    companion object {
        @Volatile
        private var INSTANCE: ReadingJournalDatabase? = null

        fun getDatabase(context: Context): ReadingJournalDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ReadingJournalDatabase::class.java,
                    "reading_journal_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
