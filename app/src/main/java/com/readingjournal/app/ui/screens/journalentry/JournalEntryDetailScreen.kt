package com.readingjournal.app.ui.screens.journalentry

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.readingjournal.app.R
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JournalEntryDetailScreen(
    entryId: String,
    onNavigateBack: () -> Unit,
    viewModel: JournalEntryDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    
    LaunchedEffect(entryId) {
        viewModel.loadEntry(entryId)
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(uiState.entry?.title ?: "Journal Entry") },
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
                        IconButton(
                            onClick = {
                                scope.launch {
                                    viewModel.toggleFavorite()
                                    snackbarHostState.showSnackbar(
                                        if (uiState.entry?.isFavorite == true) "Removed from favorites" else "Added to favorites"
                                    )
                                }
                            }
                        ) {
                            Icon(
                                if (uiState.entry?.isFavorite == true) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                contentDescription = "Favorite"
                            )
                        }
                        IconButton(onClick = { viewModel.enableEdit() }) {
                            Icon(Icons.Default.Edit, contentDescription = "Edit")
                        }
                        IconButton(
                            onClick = {
                                scope.launch {
                                    viewModel.deleteEntry()
                                    onNavigateBack()
                                }
                            }
                        ) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete")
                        }
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        uiState.entry?.let { entry ->
            if (uiState.isEditMode) {
                JournalEntryEditForm(
                    entry = entry,
                    onSave = {
                        scope.launch {
                            viewModel.saveEntry()
                            snackbarHostState.showSnackbar("Entry saved successfully")
                        }
                    },
                    onCancel = { viewModel.cancelEdit() },
                    onEntryUpdate = { updatedEntry ->
                        viewModel.updateEntry(updatedEntry)
                    },
                    modifier = Modifier.padding(padding)
                )
            } else {
                JournalEntryView(
                    entry = entry,
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JournalEntryView(
    entry: com.readingjournal.app.data.model.JournalEntry,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = entry.title,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )
            if (entry.isFavorite) {
                Icon(
                    Icons.Default.Favorite,
                    contentDescription = "Favorite",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
        
        // Date Info
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                InfoRow("Created", entry.dateCreated.toString().substringBefore("T"))
                if (entry.dateModified != entry.dateCreated) {
                    InfoRow("Last Modified", entry.dateModified.toString().substringBefore("T"))
                }
                if (entry.mood != null) {
                    InfoRow("Mood", entry.mood)
                }
                if (entry.tags.isNotEmpty()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Tags: ",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        entry.tags.forEach { tag ->
                            FilterChip(
                                selected = true,
                                onClick = { },
                                label = { Text(tag) }
                            )
                        }
                    }
                }
            }
        }
        
        // Content
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Content",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = entry.content,
                    style = MaterialTheme.typography.bodyLarge
                )
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
fun JournalEntryEditForm(
    entry: com.readingjournal.app.data.model.JournalEntry,
    onSave: () -> Unit,
    onCancel: () -> Unit,
    onEntryUpdate: (com.readingjournal.app.data.model.JournalEntry) -> Unit,
    modifier: Modifier = Modifier
) {
    var title by remember { mutableStateOf(entry.title) }
    var content by remember { mutableStateOf(entry.content) }
    var mood by remember { mutableStateOf(entry.mood ?: "") }
    var tagsText by remember { mutableStateOf(entry.tags.joinToString(", ")) }
    
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
            label = { Text("Title") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        
        OutlinedTextField(
            value = mood,
            onValueChange = { mood = it },
            label = { Text("Mood (optional)") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        
        OutlinedTextField(
            value = tagsText,
            onValueChange = { tagsText = it },
            label = { Text("Tags (comma-separated)") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            supportingText = { Text("e.g., reflection, quotes, thoughts") }
        )
        
        OutlinedTextField(
            value = content,
            onValueChange = { content = it },
            label = { Text("Content") },
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            maxLines = 20
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
                    val tags = tagsText.split(",").map { it.trim() }.filter { it.isNotEmpty() }
                    val updatedEntry = entry.copy(
                        title = title.trim(),
                        content = content.trim(),
                        mood = mood.ifEmpty { null },
                        tags = tags,
                        dateModified = java.time.LocalDateTime.now()
                    )
                    onEntryUpdate(updatedEntry)
                    onSave()
                },
                modifier = Modifier.weight(1f),
                enabled = title.isNotBlank() && content.isNotBlank()
            ) {
                Text(stringResource(R.string.save))
            }
        }
    }
}

