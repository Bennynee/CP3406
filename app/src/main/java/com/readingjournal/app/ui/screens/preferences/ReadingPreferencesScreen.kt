package com.readingjournal.app.ui.screens.preferences

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReadingPreferencesScreen(
    onNavigateBack: () -> Unit,
    viewModel: ReadingPreferencesViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Reading Preferences") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Reading Goals Section
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Reading Goals",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    
                    PreferenceItem(
                        title = "Books per month",
                        value = "${uiState.booksPerMonth} books",
                        onValueChange = { viewModel.updateBooksPerMonth(it) }
                    )
                    
                    PreferenceItem(
                        title = "Pages per day",
                        value = "${uiState.pagesPerDay} pages",
                        onValueChange = { viewModel.updatePagesPerDay(it) }
                    )
                    
                    SwitchPreference(
                        title = "Enable reading reminders",
                        checked = uiState.enableReminders,
                        onCheckedChange = { viewModel.updateEnableReminders(it) }
                    )
                }
            }
            
            // Reading Experience Section
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Reading Experience",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    
                    SwitchPreference(
                        title = "Show progress percentage",
                        checked = uiState.showProgressPercentage,
                        onCheckedChange = { viewModel.updateShowProgressPercentage(it) }
                    )
                    
                    SwitchPreference(
                        title = "Auto-save reading sessions",
                        checked = uiState.autoSaveSessions,
                        onCheckedChange = { viewModel.updateAutoSaveSessions(it) }
                    )
                    
                    SwitchPreference(
                        title = "Enable reading statistics",
                        checked = uiState.enableStatistics,
                        onCheckedChange = { viewModel.updateEnableStatistics(it) }
                    )
                }
            }
            
            // Notifications Section
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Notifications",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    
                    SwitchPreference(
                        title = "Daily reading reminder",
                        checked = uiState.dailyReminder,
                        onCheckedChange = { viewModel.updateDailyReminder(it) }
                    )
                    
                    SwitchPreference(
                        title = "Goal achievement notifications",
                        checked = uiState.goalNotifications,
                        onCheckedChange = { viewModel.updateGoalNotifications(it) }
                    )
                    
                    SwitchPreference(
                        title = "New book recommendations",
                        checked = uiState.recommendationNotifications,
                        onCheckedChange = { viewModel.updateRecommendationNotifications(it) }
                    )
                }
            }
            
            // Save Button
            Button(
                onClick = {
                    viewModel.savePreferences()
                    onNavigateBack()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save Preferences")
            }
        }
    }
}

@Composable
private fun PreferenceItem(
    title: String,
    value: String,
    onValueChange: (Int) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
        ) {
            IconButton(onClick = { onValueChange(-1) }) {
                Text("-", style = MaterialTheme.typography.titleLarge)
            }
            Text(
                text = value,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium
            )
            IconButton(onClick = { onValueChange(1) }) {
                Text("+", style = MaterialTheme.typography.titleLarge)
            }
        }
    }
}

@Composable
private fun SwitchPreference(
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge
        )
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
    }
}

