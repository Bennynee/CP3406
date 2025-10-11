package com.readingjournal.app.ui.screens.social

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import dagger.hilt.android.lifecycle.HiltViewModel

@HiltViewModel
class SocialFeaturesViewModel @Inject constructor() : ViewModel() {
    
    private val _uiState = MutableStateFlow(SocialFeaturesUiState())
    val uiState: StateFlow<SocialFeaturesUiState> = _uiState.asStateFlow()
    
    init {
        // Load sample data
        _uiState.value = _uiState.value.copy(
            friends = listOf(
                Friend("1", "Sarah Johnson", 12),
                Friend("2", "Mike Chen", 8),
                Friend("3", "Emma Davis", 15)
            ),
            bookClubs = listOf(
                BookClub("1", "Sci-Fi Enthusiasts", 24, "Dune"),
                BookClub("2", "Literary Fiction Lovers", 18, "Klara and the Sun")
            )
        )
    }
    
    fun showAddFriendDialog() {
        _uiState.update { it.copy(showAddFriendDialog = true) }
    }
    
    fun hideAddFriendDialog() {
        _uiState.update { it.copy(showAddFriendDialog = false, friendSearchQuery = "") }
    }
    
    fun updateFriendSearchQuery(query: String) {
        _uiState.update { it.copy(friendSearchQuery = query) }
    }
    
    fun addFriend() {
        val query = _uiState.value.friendSearchQuery
        if (query.isNotBlank()) {
            // In a real app, this would make an API call
            _uiState.update { state ->
                state.copy(
                    friends = state.friends + Friend(
                        id = System.currentTimeMillis().toString(),
                        name = query,
                        booksRead = 0
                    ),
                    showAddFriendDialog = false,
                    friendSearchQuery = ""
                )
            }
        }
    }
    
    fun removeFriend(friendId: String) {
        _uiState.update { state ->
            state.copy(
                friends = state.friends.filter { it.id != friendId }
            )
        }
    }
    
    fun showJoinBookClubDialog() {
        _uiState.update { it.copy(showJoinBookClubDialog = true) }
    }
    
    fun hideJoinBookClubDialog() {
        _uiState.update { it.copy(showJoinBookClubDialog = false) }
    }
    
    fun leaveBookClub(clubId: String) {
        _uiState.update { state ->
            state.copy(
                bookClubs = state.bookClubs.filter { it.id != clubId }
            )
        }
    }
}

data class SocialFeaturesUiState(
    val friends: List<Friend> = emptyList(),
    val bookClubs: List<BookClub> = emptyList(),
    val showAddFriendDialog: Boolean = false,
    val showJoinBookClubDialog: Boolean = false,
    val friendSearchQuery: String = ""
)

