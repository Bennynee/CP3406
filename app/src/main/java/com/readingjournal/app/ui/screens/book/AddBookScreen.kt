package com.readingjournal.app.ui.screens.book

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.readingjournal.app.R
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddBookScreen(
    onNavigateBack: () -> Unit,
    onBookAdded: () -> Unit,
    viewModel: AddBookViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add Book") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        AddBookForm(
            title = uiState.title,
            author = uiState.author,
            totalPages = uiState.totalPages,
            currentPage = uiState.currentPage,
            rating = uiState.rating,
            notes = uiState.notes,
            genre = uiState.genre,
            coverImageUrl = uiState.coverImageUrl,
            onTitleChange = { viewModel.updateTitle(it) },
            onAuthorChange = { viewModel.updateAuthor(it) },
            onTotalPagesChange = { viewModel.updateTotalPages(it) },
            onCurrentPageChange = { viewModel.updateCurrentPage(it) },
            onRatingChange = { viewModel.updateRating(it) },
            onNotesChange = { viewModel.updateNotes(it) },
            onGenreChange = { viewModel.updateGenre(it) },
            onCoverImageUrlChange = { viewModel.updateCoverImageUrl(it) },
            onSave = {
                scope.launch {
                    viewModel.saveBook()
                    snackbarHostState.showSnackbar("Book added successfully")
                    onBookAdded()
                }
            },
            isLoading = uiState.isLoading,
            modifier = Modifier.padding(padding)
        )
    }
}

@Composable
fun AddBookForm(
    title: String,
    author: String,
    totalPages: String,
    currentPage: String,
    rating: String,
    notes: String,
    genre: String,
    coverImageUrl: String,
    onTitleChange: (String) -> Unit,
    onAuthorChange: (String) -> Unit,
    onTotalPagesChange: (String) -> Unit,
    onCurrentPageChange: (String) -> Unit,
    onRatingChange: (String) -> Unit,
    onNotesChange: (String) -> Unit,
    onGenreChange: (String) -> Unit,
    onCoverImageUrlChange: (String) -> Unit,
    onSave: () -> Unit,
    isLoading: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        if (coverImageUrl.isNotEmpty()) {
            AsyncImage(
                model = coverImageUrl,
                contentDescription = "Book cover",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentScale = ContentScale.Fit
            )
        }
        
        OutlinedTextField(
            value = title,
            onValueChange = onTitleChange,
            label = { Text(stringResource(R.string.book_title)) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            enabled = !isLoading
        )
        
        OutlinedTextField(
            value = author,
            onValueChange = onAuthorChange,
            label = { Text(stringResource(R.string.author)) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            enabled = !isLoading
        )
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = totalPages,
                onValueChange = onTotalPagesChange,
                label = { Text(stringResource(R.string.total_pages)) },
                modifier = Modifier.weight(1f),
                singleLine = true,
                enabled = !isLoading
            )
            
            OutlinedTextField(
                value = currentPage,
                onValueChange = onCurrentPageChange,
                label = { Text(stringResource(R.string.pages_read)) },
                modifier = Modifier.weight(1f),
                singleLine = true,
                enabled = !isLoading
            )
        }
        
        OutlinedTextField(
            value = rating,
            onValueChange = onRatingChange,
            label = { Text(stringResource(R.string.rating)) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            supportingText = { Text("0-5") },
            enabled = !isLoading
        )
        
        OutlinedTextField(
            value = genre,
            onValueChange = onGenreChange,
            label = { Text("Genre") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            enabled = !isLoading
        )
        
        OutlinedTextField(
            value = coverImageUrl,
            onValueChange = onCoverImageUrlChange,
            label = { Text("Cover Image URL (optional)") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            enabled = !isLoading
        )
        
        OutlinedTextField(
            value = notes,
            onValueChange = onNotesChange,
            label = { Text(stringResource(R.string.notes)) },
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            maxLines = 10,
            enabled = !isLoading
        )
        
        Button(
            onClick = onSave,
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading && title.isNotBlank() && author.isNotBlank()
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = MaterialTheme.colorScheme.onPrimary
                )
                Spacer(modifier = Modifier.width(8.dp))
            }
            Text(stringResource(R.string.save))
        }
    }
}

