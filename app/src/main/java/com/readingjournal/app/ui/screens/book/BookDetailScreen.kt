package com.readingjournal.app.ui.screens.book

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
fun BookDetailScreen(
    bookId: String,
    onNavigateBack: () -> Unit,
    viewModel: BookDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    
    LaunchedEffect(bookId) {
        viewModel.loadBook(bookId)
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(uiState.book?.title ?: "Book Details") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    if (uiState.isEditMode) {
                        IconButton(onClick = { viewModel.cancelEdit() }) {
                            Icon(Icons.Default.Close, contentDescription = "Cancel")
                        }
                    } else {
                        IconButton(onClick = { viewModel.enableEdit() }) {
                            Icon(Icons.Default.Edit, contentDescription = "Edit")
                        }
                        IconButton(onClick = {
                            scope.launch {
                                viewModel.deleteBook()
                                onNavigateBack()
                            }
                        }) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete")
                        }
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        uiState.book?.let { book ->
            if (uiState.isEditMode) {
                BookEditForm(
                    book = book,
                    onSave = {
                        scope.launch {
                            viewModel.saveBook()
                            snackbarHostState.showSnackbar("Book saved successfully")
                        }
                    },
                    onCancel = { viewModel.cancelEdit() },
                    onBookUpdate = { updatedBook ->
                        viewModel.updateBook(updatedBook)
                    },
                    modifier = Modifier.padding(padding)
                )
            } else {
                BookDetailView(
                    book = book,
                    onMarkComplete = {
                        scope.launch {
                            viewModel.toggleCompletion()
                            snackbarHostState.showSnackbar(
                                if (book.isCompleted) "Marked as in progress" else "Marked as completed"
                            )
                        }
                    },
                    modifier = Modifier
                        .padding(padding)
                        .fillMaxSize()
                )
            }
        } ?: run {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }
}

@Composable
fun BookDetailView(
    book: com.readingjournal.app.data.model.Book,
    onMarkComplete: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Cover Image
        AsyncImage(
            model = book.coverImageUrl,
            contentDescription = book.title,
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            contentScale = ContentScale.Fit
        )
        
        // Title and Author
        Column {
            Text(
                text = book.title,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = book.author,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        
        // Progress Card
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Progress",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "${book.currentPage} / ${book.totalPages} pages",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                
                LinearProgressIndicator(
                    progress = book.progressPercentage / 100f,
                    modifier = Modifier.fillMaxWidth()
                )
                
                Text(
                    text = "${String.format("%.1f", book.progressPercentage)}% complete",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        
        // Book Info
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            if (book.genre != null) {
                InfoRow("Genre", book.genre)
            }
            if (book.isbn != null) {
                InfoRow("ISBN", book.isbn)
            }
            InfoRow("Date Added", book.dateAdded.toString().substringBefore("T"))
            if (book.dateCompleted != null) {
                InfoRow("Date Completed", book.dateCompleted.toString().substringBefore("T"))
            }
            if (book.rating > 0) {
                InfoRow("Rating", "${book.rating}/5")
            }
        }
        
        // Notes
        if (book.notes.isNotEmpty()) {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Notes",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = book.notes,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
        
        // Action Buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                onClick = onMarkComplete,
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    if (book.isCompleted) Icons.Default.Refresh else Icons.Default.Check,
                    contentDescription = null
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(if (book.isCompleted) "Mark In Progress" else "Mark Complete")
            }
        }
    }
}

@Composable
fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun BookEditForm(
    book: com.readingjournal.app.data.model.Book,
    onSave: () -> Unit,
    onCancel: () -> Unit,
    onBookUpdate: (com.readingjournal.app.data.model.Book) -> Unit,
    modifier: Modifier = Modifier
) {
    var title by remember { mutableStateOf(book.title) }
    var author by remember { mutableStateOf(book.author) }
    var totalPages by remember { mutableStateOf(book.totalPages.toString()) }
    var currentPage by remember { mutableStateOf(book.currentPage.toString()) }
    var rating by remember { mutableStateOf(book.rating.toString()) }
    var notes by remember { mutableStateOf(book.notes) }
    var genre by remember { mutableStateOf(book.genre ?: "") }
    
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text(stringResource(R.string.book_title)) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        
        OutlinedTextField(
            value = author,
            onValueChange = { author = it },
            label = { Text(stringResource(R.string.author)) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = totalPages,
                onValueChange = { totalPages = it },
                label = { Text(stringResource(R.string.total_pages)) },
                modifier = Modifier.weight(1f),
                singleLine = true
            )
            
            OutlinedTextField(
                value = currentPage,
                onValueChange = { currentPage = it },
                label = { Text(stringResource(R.string.pages_read)) },
                modifier = Modifier.weight(1f),
                singleLine = true
            )
        }
        
        OutlinedTextField(
            value = rating,
            onValueChange = { rating = it },
            label = { Text(stringResource(R.string.rating)) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            supportingText = { Text("0-5") }
        )
        
        OutlinedTextField(
            value = genre,
            onValueChange = { genre = it },
            label = { Text("Genre") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        
        OutlinedTextField(
            value = notes,
            onValueChange = { notes = it },
            label = { Text(stringResource(R.string.notes)) },
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            maxLines = 10
        )
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedButton(
                onClick = onCancel,
                modifier = Modifier.weight(1f)
            ) {
                Text(stringResource(R.string.cancel))
            }
            
            Button(
                onClick = {
                    val updatedBook = book.copy(
                        title = title,
                        author = author,
                        totalPages = totalPages.toIntOrNull() ?: book.totalPages,
                        currentPage = currentPage.toIntOrNull() ?: book.currentPage,
                        rating = rating.toFloatOrNull() ?: book.rating,
                        notes = notes,
                        genre = genre.ifEmpty { null }
                    )
                    onBookUpdate(updatedBook)
                    onSave()
                },
                modifier = Modifier.weight(1f)
            ) {
                Text(stringResource(R.string.save))
            }
        }
    }
}

