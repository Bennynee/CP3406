package com.readingjournal.app.ui.screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.readingjournal.app.R
import com.readingjournal.app.ui.components.AISuggestionCard
import com.readingjournal.app.ui.components.BookCard
import com.readingjournal.app.ui.components.EmptyState
import com.readingjournal.app.ui.components.QuickStatsCard
import com.readingjournal.app.ui.components.WelcomeCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToBookDetail: (String) -> Unit = {},
    onNavigateToAddBook: () -> Unit = {},
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            WelcomeCard()
        }
        
        item {
            QuickStatsCard(
                booksRead = uiState.booksReadThisMonth,
                pagesRead = uiState.pagesReadThisMonth,
                readingStreak = uiState.readingStreak
            )
        }
        
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.currently_reading),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                FloatingActionButton(
                    onClick = onNavigateToAddBook,
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = stringResource(R.string.add_new_book)
                    )
                }
            }
        }
        
        item {
            if (uiState.currentlyReadingBooks.isEmpty()) {
                EmptyState(
                    title = "No books in progress",
                    message = "Start reading a book and track your progress here!",
                    modifier = Modifier.fillMaxWidth()
                )
            } else {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(uiState.currentlyReadingBooks) { book ->
                        BookCard(
                            book = book,
                            onClick = { onNavigateToBookDetail(book.id) }
                        )
                    }
                }
            }
        }
        
        item {
            Text(
                text = stringResource(R.string.ai_suggestions),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
        }
        
        item {
            if (uiState.aiSuggestions.isEmpty()) {
                EmptyState(
                    title = "No suggestions yet",
                    message = "Keep reading and we'll provide personalized recommendations!",
                    modifier = Modifier.fillMaxWidth()
                )
            } else {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(uiState.aiSuggestions) { suggestion ->
                        AISuggestionCard(
                            suggestion = suggestion,
                            onDismiss = { /* Handle dismiss in home */ },
                            onLearnMore = { /* Handle learn more in home */ }
                        )
                    }
                }
            }
        }
    }
}
