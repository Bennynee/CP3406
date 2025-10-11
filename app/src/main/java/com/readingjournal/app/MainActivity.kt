package com.readingjournal.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.google.firebase.auth.FirebaseAuth
import com.readingjournal.app.data.database.DatabaseSeeder
import com.readingjournal.app.ui.navigation.ReadingJournalNavigation
import com.readingjournal.app.ui.screens.auth.AuthScreen
import com.readingjournal.app.ui.theme.ReadingJournalTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    
    @Inject
    lateinit var firebaseAuth: FirebaseAuth
    
    @Inject
    lateinit var databaseSeeder: DatabaseSeeder
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        // Seed database with sample data on first launch
        kotlinx.coroutines.CoroutineScope(kotlinx.coroutines.Dispatchers.IO).launch {
            databaseSeeder.seedIfNeeded()
        }
        
        setContent {
            ReadingJournalTheme {
                var isAuthenticated by remember { mutableStateOf(firebaseAuth.currentUser != null) }
                
                if (isAuthenticated) {
                    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                        ReadingJournalNavigation(
                            modifier = Modifier.padding(innerPadding)
                        )
                    }
                } else {
                    AuthScreen(
                        onAuthSuccess = { isAuthenticated = true }
                    )
                }
            }
        }
    }
}
