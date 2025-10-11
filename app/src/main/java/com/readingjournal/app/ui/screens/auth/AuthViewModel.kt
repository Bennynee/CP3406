package com.readingjournal.app.ui.screens.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.readingjournal.app.data.auth.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()
    
    init {
        // Check if user is already authenticated
        _uiState.update { it.copy(isAuthenticated = authRepository.isUserSignedIn()) }
    }
    
    fun updateEmail(email: String) {
        _uiState.update { it.copy(email = email, errorMessage = "") }
    }
    
    fun updatePassword(password: String) {
        _uiState.update { it.copy(password = password, errorMessage = "") }
    }
    
    fun updateDisplayName(displayName: String) {
        _uiState.update { it.copy(displayName = displayName, errorMessage = "") }
    }
    
    fun togglePasswordVisibility() {
        _uiState.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
    }
    
    fun toggleAuthMode() {
        _uiState.update { 
            it.copy(
                isSignUpMode = !it.isSignUpMode,
                errorMessage = "",
                displayName = ""
            ) 
        }
    }
    
    fun signIn() {
        val currentState = _uiState.value
        if (currentState.email.isBlank() || currentState.password.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Please fill in all fields") }
            return
        }
        
        _uiState.update { it.copy(isLoading = true, errorMessage = "") }
        
        viewModelScope.launch {
            val result = authRepository.signInWithEmailAndPassword(
                currentState.email,
                currentState.password
            )
            
            result.fold(
                onSuccess = { 
                    _uiState.update { it.copy(isAuthenticated = true, isLoading = false) }
                },
                onFailure = { exception ->
                    _uiState.update { 
                        it.copy(
                            isLoading = false,
                            errorMessage = exception.message ?: "Sign in failed"
                        ) 
                    }
                }
            )
        }
    }
    
    fun signUp() {
        val currentState = _uiState.value
        if (currentState.email.isBlank() || currentState.password.isBlank() || currentState.displayName.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Please fill in all fields") }
            return
        }
        
        if (currentState.password.length < 6) {
            _uiState.update { it.copy(errorMessage = "Password must be at least 6 characters") }
            return
        }
        
        _uiState.update { it.copy(isLoading = true, errorMessage = "") }
        
        viewModelScope.launch {
            val result = authRepository.createUserWithEmailAndPassword(
                currentState.email,
                currentState.password,
                currentState.displayName
            )
            
            result.fold(
                onSuccess = { 
                    _uiState.update { it.copy(isAuthenticated = true, isLoading = false) }
                },
                onFailure = { exception ->
                    _uiState.update { 
                        it.copy(
                            isLoading = false,
                            errorMessage = exception.message ?: "Sign up failed"
                        ) 
                    }
                }
            )
        }
    }
    
    fun resetPassword() {
        val currentState = _uiState.value
        if (currentState.email.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Please enter your email address") }
            return
        }
        
        viewModelScope.launch {
            val result = authRepository.resetPassword(currentState.email)
            result.fold(
                onSuccess = {
                    _uiState.update { it.copy(errorMessage = "Password reset email sent!") }
                },
                onFailure = { exception ->
                    _uiState.update { 
                        it.copy(errorMessage = exception.message ?: "Failed to send reset email") 
                    }
                }
            )
        }
    }
}

data class AuthUiState(
    val email: String = "",
    val password: String = "",
    val displayName: String = "",
    val isSignUpMode: Boolean = false,
    val isPasswordVisible: Boolean = false,
    val isLoading: Boolean = false,
    val isAuthenticated: Boolean = false,
    val errorMessage: String = ""
)
