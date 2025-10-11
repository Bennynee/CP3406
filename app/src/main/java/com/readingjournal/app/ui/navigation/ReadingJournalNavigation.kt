package com.readingjournal.app.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.readingjournal.app.R
import com.readingjournal.app.ui.screens.book.AddBookScreen
import com.readingjournal.app.ui.screens.book.BookDetailScreen
import com.readingjournal.app.ui.screens.home.HomeScreen
import com.readingjournal.app.ui.screens.journal.JournalScreen
import com.readingjournal.app.ui.screens.journalentry.AddJournalEntryScreen
import com.readingjournal.app.ui.screens.journalentry.JournalEntryDetailScreen
import com.readingjournal.app.ui.screens.preferences.ReadingPreferencesScreen
import com.readingjournal.app.ui.screens.progress.ProgressScreen
import com.readingjournal.app.ui.screens.profile.ProfileScreen
import com.readingjournal.app.ui.screens.scan.ScanBookScreen
import com.readingjournal.app.ui.screens.social.SocialFeaturesScreen
import com.readingjournal.app.ui.screens.aisuggestions.AISuggestionsScreen
import com.readingjournal.app.ui.screens.export.ExportDataScreen
import com.readingjournal.app.ui.screens.privacy.PrivacySettingsScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReadingJournalNavigation(
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()
    
    Scaffold(
        modifier = modifier,
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                
                listOf(
                    Screen.Home,
                    Screen.Scan,
                    Screen.Journal,
                    Screen.Progress,
                    Screen.Profile
                ).forEach { screen ->
                    NavigationBarItem(
                        icon = { Icon(screen.icon, contentDescription = screen.route) },
                        label = { Text(stringResource(screen.resourceId)) },
                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) {
                HomeScreen(
                    onNavigateToBookDetail = { bookId ->
                        navController.navigate("book_detail/$bookId")
                    },
                    onNavigateToAddBook = {
                        navController.navigate(Screen.AddBook.route)
                    }
                )
            }
            composable(Screen.Scan.route) {
                ScanBookScreen(
                    onBookScanned = { bookId ->
                        navController.navigate("book_detail/$bookId")
                    },
                    onNavigateToManualAdd = {
                        navController.navigate(Screen.AddBook.route)
                    }
                )
            }
            composable(Screen.Journal.route) {
                JournalScreen(
                    onNavigateToEntryDetail = { entryId ->
                        navController.navigate("journal_entry_detail/$entryId")
                    },
                    onNavigateToAddEntry = {
                        navController.navigate(Screen.AddJournalEntry.route)
                    }
                )
            }
            composable(Screen.Progress.route) {
                ProgressScreen()
            }
            composable(Screen.Profile.route) {
                ProfileScreen(
                    onNavigateBack = {
                        if (!navController.popBackStack()) {
                            navController.navigate(Screen.Home.route) {
                                popUpTo(Screen.Home.route) { inclusive = true }
                            }
                        }
                    },
                    onNavigateToReadingPreferences = {
                        navController.navigate("reading_preferences")
                    },
                    onNavigateToAISuggestions = {
                        navController.navigate("ai_suggestions")
                    },
                    onNavigateToSocialFeatures = {
                        navController.navigate("social_features")
                    },
                    onNavigateToExportData = {
                        navController.navigate("export_data")
                    },
                    onNavigateToPrivacySettings = {
                        navController.navigate("privacy_settings")
                    }
                )
            }
            
            // Reading Preferences Screen
            composable("reading_preferences") {
                ReadingPreferencesScreen(
                    onNavigateBack = { navController.popBackStack() }
                )
            }
            
            // AI Suggestions Screen
            composable("ai_suggestions") {
                AISuggestionsScreen(
                    onNavigateBack = { navController.popBackStack() }
                )
            }
            
            // Social Features Screen
            composable("social_features") {
                SocialFeaturesScreen(
                    onNavigateBack = { navController.popBackStack() }
                )
            }
            
            // Export Data Screen
            composable("export_data") {
                ExportDataScreen(
                    onNavigateBack = { navController.popBackStack() }
                )
            }
            
            // Privacy Settings Screen
            composable("privacy_settings") {
                PrivacySettingsScreen(
                    onNavigateBack = { navController.popBackStack() }
                )
            }
            
            // Book detail screen
            composable("book_detail/{bookId}") { backStackEntry ->
                val bookId = backStackEntry.arguments?.getString("bookId") ?: ""
                BookDetailScreen(
                    bookId = bookId,
                    onNavigateBack = { navController.popBackStack() }
                )
            }
            
            // Add book screen
            composable(Screen.AddBook.route) {
                AddBookScreen(
                    onNavigateBack = { navController.popBackStack() },
                    onBookAdded = { navController.popBackStack() }
                )
            }
            
            // Journal entry detail screen
            composable("journal_entry_detail/{entryId}") { backStackEntry ->
                val entryId = backStackEntry.arguments?.getString("entryId") ?: ""
                JournalEntryDetailScreen(
                    entryId = entryId,
                    onNavigateBack = { navController.popBackStack() }
                )
            }
            
            // Add journal entry screen
            composable(Screen.AddJournalEntry.route) {
                AddJournalEntryScreen(
                    bookId = null,
                    onNavigateBack = { navController.popBackStack() },
                    onEntryAdded = { navController.popBackStack() }
                )
            }
        }
    }
}

sealed class Screen(val route: String, val icon: ImageVector, val resourceId: Int) {
    object Home : Screen("home", Icons.Filled.Home, R.string.home)
    object Scan : Screen("scan", Icons.Filled.QrCodeScanner, R.string.add_new_book)
    object Journal : Screen("journal", Icons.Filled.Book, R.string.journal)
    object Progress : Screen("progress", Icons.Filled.TrendingUp, R.string.progress)
    object Profile : Screen("profile", Icons.Filled.Person, R.string.profile)
    object AddBook : Screen("add_book", Icons.Filled.Add, R.string.add_new_book)
    object AddJournalEntry : Screen("add_journal_entry", Icons.Filled.Edit, R.string.new_entry)
}
