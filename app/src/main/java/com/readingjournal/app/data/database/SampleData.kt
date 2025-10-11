package com.readingjournal.app.data.database

import com.readingjournal.app.data.model.AISuggestion
import com.readingjournal.app.data.model.Book
import com.readingjournal.app.data.model.JournalEntry
import com.readingjournal.app.data.model.SuggestionType
import com.readingjournal.app.utils.BookCoverHelper
import java.time.LocalDateTime

object SampleData {
    
    val sampleBooks = listOf(
        Book(
            id = "1",
            title = "The Silent Patient",
            author = "Alex Michaelides",
            totalPages = 352,
            currentPage = 180,
            rating = 4.5f,
            notes = "A gripping psychological thriller with unexpected twists. The narrative structure keeps you guessing until the very end.",
            isCompleted = false,
            genre = "Thriller",
            coverImageUrl = BookCoverHelper.getCoverImageUrl("9781250301697"),
            isbn = "9781250301697",
            dateAdded = LocalDateTime.now().minusDays(15)
        ),
        Book(
            id = "2",
            title = "Educated",
            author = "Tara Westover",
            totalPages = 334,
            currentPage = 334,
            rating = 5.0f,
            notes = "Powerful memoir about education and family. An inspiring story of resilience and the pursuit of knowledge.",
            isCompleted = true,
            genre = "Memoir",
            coverImageUrl = BookCoverHelper.getCoverImageUrl("9780399590504"),
            isbn = "9780399590504",
            dateAdded = LocalDateTime.now().minusDays(30),
            dateCompleted = LocalDateTime.now().minusDays(5)
        ),
        Book(
            id = "3",
            title = "Atomic Habits",
            author = "James Clear",
            totalPages = 320,
            currentPage = 120,
            rating = 4.8f,
            notes = "Learning about building good habits. The concept of habit stacking is brilliant and practical.",
            isCompleted = false,
            genre = "Self-Help",
            coverImageUrl = BookCoverHelper.getCoverImageUrl("9780735211292"),
            isbn = "9780735211292",
            dateAdded = LocalDateTime.now().minusDays(10)
        ),
        Book(
            id = "4",
            title = "The Seven Husbands of Evelyn Hugo",
            author = "Taylor Jenkins Reid",
            totalPages = 400,
            currentPage = 0,
            rating = 0f,
            notes = "Recommended by friends. Looking forward to reading this character-driven story.",
            isCompleted = false,
            genre = "Fiction",
            coverImageUrl = BookCoverHelper.getCoverImageUrl("9781501139239"),
            isbn = "9781501139239",
            dateAdded = LocalDateTime.now().minusDays(2)
        ),
        Book(
            id = "5",
            title = "Project Hail Mary",
            author = "Andy Weir",
            totalPages = 496,
            currentPage = 250,
            rating = 4.9f,
            notes = "Amazing science fiction! The scientific accuracy combined with humor makes this a page-turner.",
            isCompleted = false,
            genre = "Science Fiction",
            coverImageUrl = BookCoverHelper.getCoverImageUrl("9780593135204"),
            isbn = "9780593135204",
            dateAdded = LocalDateTime.now().minusDays(8)
        ),
        Book(
            id = "6",
            title = "The Midnight Library",
            author = "Matt Haig",
            totalPages = 304,
            currentPage = 304,
            rating = 4.7f,
            notes = "A beautiful exploration of life's possibilities. Thought-provoking and heartwarming.",
            isCompleted = true,
            genre = "Fiction",
            coverImageUrl = BookCoverHelper.getCoverImageUrl("9780525559474"),
            isbn = "9780525559474",
            dateAdded = LocalDateTime.now().minusDays(20),
            dateCompleted = LocalDateTime.now().minusDays(3)
        ),
        Book(
            id = "7",
            title = "Dune",
            author = "Frank Herbert",
            totalPages = 688,
            currentPage = 450,
            rating = 5.0f,
            notes = "Epic science fiction masterpiece. The world-building is incredible.",
            isCompleted = false,
            genre = "Science Fiction",
            coverImageUrl = BookCoverHelper.getCoverImageUrl("9780441013593"),
            isbn = "9780441013593",
            dateAdded = LocalDateTime.now().minusDays(12)
        ),
        Book(
            id = "8",
            title = "The Psychology of Money",
            author = "Morgan Housel",
            totalPages = 256,
            currentPage = 256,
            rating = 4.6f,
            notes = "Great insights on wealth, greed, and happiness. Changed my perspective on money.",
            isCompleted = true,
            genre = "Finance",
            coverImageUrl = BookCoverHelper.getCoverImageUrl("9780857197689"),
            isbn = "9780857197689",
            dateAdded = LocalDateTime.now().minusDays(25),
            dateCompleted = LocalDateTime.now().minusDays(7)
        ),
        Book(
            id = "9",
            title = "Klara and the Sun",
            author = "Kazuo Ishiguro",
            totalPages = 320,
            currentPage = 80,
            rating = 4.5f,
            notes = "Beautiful and melancholic. Ishiguro's writing is always thought-provoking.",
            isCompleted = false,
            genre = "Literary Fiction",
            coverImageUrl = BookCoverHelper.getCoverImageUrl("9780593318171"),
            isbn = "9780593318171",
            dateAdded = LocalDateTime.now().minusDays(5)
        ),
        Book(
            id = "10",
            title = "Sapiens",
            author = "Yuval Noah Harari",
            totalPages = 464,
            currentPage = 0,
            rating = 0f,
            notes = "Want to understand human history from a unique perspective.",
            isCompleted = false,
            genre = "History",
            coverImageUrl = BookCoverHelper.getCoverImageUrl("9780062316097"),
            isbn = "9780062316097",
            dateAdded = LocalDateTime.now().minusDays(1)
        )
    )
    
    val sampleJournalEntries = listOf(
        JournalEntry(
            id = "1",
            bookId = "1",
            title = "Mind-blowing twist!",
            content = "I just finished chapter 15 and I can't believe what happened. The plot twist was completely unexpected. This book is keeping me on the edge of my seat. The way the author weaves the narrative is masterful.",
            dateCreated = LocalDateTime.now().minusDays(2),
            mood = "Excited",
            tags = listOf("thriller", "plot-twist", "page-turner"),
            isFavorite = true
        ),
        JournalEntry(
            id = "2",
            bookId = "2",
            title = "Finished Educated",
            content = "What an incredible journey. Tara's story of overcoming obstacles to pursue education is both heartbreaking and inspiring. The writing is beautiful and the message is powerful. This book made me reflect on my own educational journey.",
            dateCreated = LocalDateTime.now().minusDays(5),
            mood = "Inspired",
            tags = listOf("memoir", "education", "inspiration"),
            isFavorite = true
        ),
        JournalEntry(
            id = "3",
            bookId = "3",
            title = "Habit stacking concept",
            content = "The idea of stacking new habits onto existing ones is brilliant. I'm going to try implementing this with my morning routine. Starting small with just 2 minutes of reading. Already seeing progress!",
            dateCreated = LocalDateTime.now().minusHours(3),
            mood = "Motivated",
            tags = listOf("habits", "productivity", "self-improvement"),
            isFavorite = false
        ),
        JournalEntry(
            id = "4",
            bookId = "5",
            title = "Rocky and Grace",
            content = "The friendship between Rocky and Grace is so well-written. The scientific accuracy combined with humor makes this book a joy to read. Can't wait to see how they solve the problem!",
            dateCreated = LocalDateTime.now().minusDays(1),
            mood = "Curious",
            tags = listOf("science-fiction", "friendship", "humor"),
            isFavorite = true
        ),
        JournalEntry(
            id = "5",
            bookId = "6",
            title = "The Midnight Library",
            content = "The concept of exploring different lives is fascinating. It's making me think about the choices I've made and the paths not taken. Very philosophical and thought-provoking.",
            dateCreated = LocalDateTime.now().minusDays(4),
            mood = "Reflective",
            tags = listOf("philosophy", "life-choices", "reflection"),
            isFavorite = false
        ),
        JournalEntry(
            id = "6",
            bookId = "7",
            title = "The world of Arrakis",
            content = "The world-building in Dune is absolutely incredible. Frank Herbert created such a rich, detailed universe. The politics, ecology, and culture are all so well thought out. This is truly epic science fiction.",
            dateCreated = LocalDateTime.now().minusDays(6),
            mood = "Amazed",
            tags = listOf("world-building", "epic", "science-fiction"),
            isFavorite = true
        ),
        JournalEntry(
            id = "7",
            bookId = "8",
            title = "Wealth and happiness",
            content = "The chapter on wealth vs. being wealthy really resonated with me. It's not about how much you have, but about having enough. This perspective shift is valuable.",
            dateCreated = LocalDateTime.now().minusDays(8),
            mood = "Thoughtful",
            tags = listOf("finance", "philosophy", "wealth"),
            isFavorite = false
        )
    )
    
    val sampleAISuggestions = listOf(
        AISuggestion(
            id = "1",
            type = SuggestionType.BOOK_RECOMMENDATION,
            title = "Recommended: 'The Seven Husbands of Evelyn Hugo'",
            description = "Based on your love for character-driven stories, you might enjoy this compelling novel about old Hollywood",
            priority = 3
        ),
        AISuggestion(
            id = "2",
            type = SuggestionType.READING_GOAL,
            title = "Set a monthly reading goal",
            description = "You've been reading consistently! Set a goal to read 3-4 books this month to stay motivated",
            priority = 2
        ),
        AISuggestion(
            id = "3",
            type = SuggestionType.JOURNAL_PROMPT,
            title = "Daily reflection prompt",
            description = "What was the most interesting idea or quote from your reading today? Consider journaling about it",
            priority = 1
        ),
        AISuggestion(
            id = "4",
            type = SuggestionType.READING_HABIT,
            title = "Build a reading habit",
            description = "Try reading for 20 minutes before bed. Small, consistent sessions lead to big progress!",
            priority = 2
        ),
        AISuggestion(
            id = "5",
            type = SuggestionType.BOOK_RECOMMENDATION,
            title = "Explore: 'The Psychology of Money'",
            description = "Since you enjoyed 'Atomic Habits', you might like this practical guide to wealth and happiness",
            priority = 3
        )
    )
}
