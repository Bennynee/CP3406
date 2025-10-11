package com.readingjournal.app.ui.screens.journalentry

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.readingjournal.app.R
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddJournalEntryScreen(
    bookId: String? = null,
    onNavigateBack: () -> Unit,
    onEntryAdded: () -> Unit,
    viewModel: AddJournalEntryViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    
    LaunchedEffect(bookId) {
        if (bookId != null) {
            viewModel.setBookId(bookId)
        }
        viewModel.loadBooks()
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("New Journal Entry") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        AddJournalEntryForm(
            title = uiState.title,
            content = uiState.content,
            selectedBookId = uiState.selectedBookId,
            books = uiState.books,
            mood = uiState.mood,
            tagsText = uiState.tagsText,
            onTitleChange = { viewModel.updateTitle(it) },
            onContentChange = { viewModel.updateContent(it) },
            onBookSelect = { viewModel.selectBook(it) },
            onMoodChange = { viewModel.updateMood(it) },
            onTagsChange = { viewModel.updateTags(it) },
            onSave = {
                scope.launch {
                    viewModel.saveEntry()
                    snackbarHostState.showSnackbar("Entry created successfully")
                    onEntryAdded()
                }
            },
            isLoading = uiState.isLoading,
            modifier = Modifier.padding(padding)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddJournalEntryForm(
    title: String,
    content: String,
    selectedBookId: String?,
    books: List<com.readingjournal.app.data.model.Book>,
    mood: String,
    tagsText: String,
    onTitleChange: (String) -> Unit,
    onContentChange: (String) -> Unit,
    onBookSelect: (String?) -> Unit,
    onMoodChange: (String) -> Unit,
    onTagsChange: (String) -> Unit,
    onSave: () -> Unit,
    isLoading: Boolean,
    modifier: Modifier = Modifier
) {
    var bookDropdownExpanded by remember { mutableStateOf(false) }
    
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OutlinedTextField(
            value = title,
            onValueChange = onTitleChange,
            label = { Text("Title") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            enabled = !isLoading
        )
        
        // Book Selection Dropdown
        ExposedDropdownMenuBox(
            expanded = bookDropdownExpanded,
            onExpandedChange = { bookDropdownExpanded = !bookDropdownExpanded }
        ) {
            OutlinedTextField(
                value = selectedBookId?.let { bookId ->
                    books.find { it.id == bookId }?.title ?: "None"
                } ?: "None",
                onValueChange = {},
                readOnly = true,
                label = { Text("Associated Book (optional)") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = bookDropdownExpanded) },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(),
                enabled = !isLoading
            )
            ExposedDropdownMenu(
                expanded = bookDropdownExpanded,
                onDismissRequest = { bookDropdownExpanded = false }
            ) {
                DropdownMenuItem(
                    text = { Text("None") },
                    onClick = {
                        onBookSelect(null)
                        bookDropdownExpanded = false
                    }
                )
                books.forEach { book ->
                    DropdownMenuItem(
                        text = { Text(book.title) },
                        onClick = {
                            onBookSelect(book.id)
                            bookDropdownExpanded = false
                        }
                    )
                }
            }
        }
        
        OutlinedTextField(
            value = mood,
            onValueChange = onMoodChange,
            label = { Text("Mood (optional)") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            enabled = !isLoading
        )
        
        OutlinedTextField(
            value = tagsText,
            onValueChange = onTagsChange,
            label = { Text("Tags (comma-separated)") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            supportingText = { Text("e.g., reflection, quotes, thoughts") },
            enabled = !isLoading
        )
        
        OutlinedTextField(
            value = content,
            onValueChange = onContentChange,
            label = { Text("Content") },
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            maxLines = 20,
            enabled = !isLoading
        )
        
        Button(
            onClick = onSave,
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading && title.isNotBlank() && content.isNotBlank()
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

