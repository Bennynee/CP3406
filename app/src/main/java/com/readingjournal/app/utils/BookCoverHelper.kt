package com.readingjournal.app.utils

object BookCoverHelper {
    /**
     * Get book cover image URL from ISBN
     * Tries multiple sources for best reliability
     */
    fun getCoverImageUrl(isbn: String?): String? {
        if (isbn.isNullOrBlank()) return null
        
        // Try Open Library first (most reliable for ISBN-based lookups)
        return "https://covers.openlibrary.org/b/isbn/$isbn-L.jpg"
    }
    
    /**
     * Get Google Books thumbnail URL from volume ID
     */
    fun getGoogleBooksThumbnail(volumeId: String): String {
        return "https://books.google.com/books/content?id=$volumeId&printsec=frontcover&img=1&zoom=1&source=gbs_api"
    }
}

