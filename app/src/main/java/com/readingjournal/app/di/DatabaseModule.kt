package com.readingjournal.app.di

import android.content.Context
import androidx.room.Room
import com.readingjournal.app.data.database.ReadingJournalDatabase
import com.readingjournal.app.data.database.dao.AISuggestionDao
import com.readingjournal.app.data.database.dao.BookDao
import com.readingjournal.app.data.database.dao.JournalDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideReadingJournalDatabase(@ApplicationContext context: Context): ReadingJournalDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            ReadingJournalDatabase::class.java,
            "reading_journal_database"
        ).build()
    }

    @Provides
    fun provideBookDao(database: ReadingJournalDatabase): BookDao {
        return database.bookDao()
    }

    @Provides
    fun provideJournalDao(database: ReadingJournalDatabase): JournalDao {
        return database.journalDao()
    }

    @Provides
    fun provideAISuggestionDao(database: ReadingJournalDatabase): AISuggestionDao {
        return database.aiSuggestionDao()
    }
}
