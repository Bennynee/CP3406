package com.readingjournal.app.data.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface BookApiService {
    
    @GET("books/v1/volumes")
    suspend fun searchBooks(
        @Query("q") query: String,
        @Query("maxResults") maxResults: Int = 20
    ): Response<BookSearchResponse>
    
    @GET("books/v1/volumes/{volumeId}")
    suspend fun getBookById(
        @Path("volumeId") volumeId: String
    ): Response<BookDetailResponse>
    
    @GET("books/v1/volumes")
    suspend fun getBookByIsbn(
        @Query("q") isbn: String,
        @Query("maxResults") maxResults: Int = 1
    ): Response<BookSearchResponse>
}

data class BookSearchResponse(
    val items: List<BookItem>? = null,
    val totalItems: Int = 0
)

data class BookItem(
    val id: String,
    val volumeInfo: VolumeInfo
)

data class VolumeInfo(
    val title: String,
    val authors: List<String>? = null,
    val publisher: String? = null,
    val publishedDate: String? = null,
    val description: String? = null,
    val pageCount: Int? = null,
    val categories: List<String>? = null,
    val averageRating: Float? = null,
    val ratingsCount: Int? = null,
    val imageLinks: ImageLinks? = null,
    val industryIdentifiers: List<IndustryIdentifier>? = null
)

data class ImageLinks(
    val smallThumbnail: String? = null,
    val thumbnail: String? = null,
    val small: String? = null,
    val medium: String? = null,
    val large: String? = null
)

data class IndustryIdentifier(
    val type: String,
    val identifier: String
)

data class BookDetailResponse(
    val id: String,
    val volumeInfo: VolumeInfo
)
