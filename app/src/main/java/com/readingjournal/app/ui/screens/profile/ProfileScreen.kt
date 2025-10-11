package com.readingjournal.app.ui.screens.profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.readingjournal.app.R
import kotlinx.coroutines.launch
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.flow.collect

@Composable
fun ProfileScreen(
    onNavigateBack: () -> Unit = {},
    onNavigateToReadingPreferences: () -> Unit = {},
    onNavigateToAISuggestions: () -> Unit = {},
    onNavigateToSocialFeatures: () -> Unit = {},
    onNavigateToExportData: () -> Unit = {},
    onNavigateToPrivacySettings: () -> Unit = {},
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val scope = rememberCoroutineScope()
    var showAboutDialog by remember { mutableStateOf(false) }
    
    LaunchedEffect(Unit) {
        viewModel.navigationEvent.collect { destination ->
            when (destination) {
                "reading_preferences" -> onNavigateToReadingPreferences()
                "ai_suggestions" -> onNavigateToAISuggestions()
                "social_features" -> onNavigateToSocialFeatures()
                "export_data" -> onNavigateToExportData()
                "privacy_settings" -> onNavigateToPrivacySettings()
            }
        }
    }
    
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            // Profile Header
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Profile Picture Placeholder
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Profile Picture",
                            modifier = Modifier.size(40.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Text(
                        text = "Reading Enthusiast",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Text(
                        text = "Member since January 2024",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
        
        item {
            Text(
                text = "Settings",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium
            )
        }
        
        item {
            ProfileMenuCard(
                title = "Reading Preferences",
                description = "Customize your reading experience",
                icon = Icons.Default.Tune,
                onClick = { 
                    viewModel.navigateToReadingPreferences()
                }
            )
        }
        
        item {
            ProfileMenuCard(
                title = "AI Suggestions",
                description = "Manage AI-powered recommendations",
                icon = Icons.Default.Lightbulb,
                onClick = { 
                    viewModel.navigateToAISuggestions()
                }
            )
        }
        
        item {
            ProfileMenuCard(
                title = "Social Features",
                description = "Connect with friends and book clubs",
                icon = Icons.Default.Group,
                onClick = { 
                    viewModel.navigateToSocialFeatures()
                }
            )
        }
        
        item {
            Text(
                text = "Data & Privacy",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium
            )
        }
        
        item {
            ProfileMenuCard(
                title = "Export Data",
                description = "Download your reading data",
                icon = Icons.Default.Download,
                onClick = { 
                    viewModel.navigateToExportData()
                }
            )
        }
        
        item {
            ProfileMenuCard(
                title = "Privacy Settings",
                description = "Control your data sharing",
                icon = Icons.Default.PrivacyTip,
                onClick = { 
                    viewModel.navigateToPrivacySettings()
                }
            )
        }
        
        item {
            ProfileMenuCard(
                title = "About",
                description = "App version and information",
                icon = Icons.Default.Info,
                onClick = { showAboutDialog = true }
            )
        }
    }
    
    if (showAboutDialog) {
        AlertDialog(
            onDismissRequest = { showAboutDialog = false },
            title = { Text("About Reading Journal") },
            text = {
                Column {
                    Text("Version 1.0.0")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("A modern Android reading app with intuitive navigation, emphasis on personal journaling and progress tracking.")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Built with Jetpack Compose, Room, and Firebase.")
                }
            },
            confirmButton = {
                TextButton(onClick = { showAboutDialog = false }) {
                    Text("OK")
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileMenuCard(
    title: String,
    description: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(24.dp)
            )
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
