package com.unicred.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unicred.data.AccessType
import com.unicred.data.AuthRepository
import com.unicred.data.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AuthState(
    val user: User? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    
    private val _authState = MutableStateFlow(AuthState())
    val authState: StateFlow<AuthState> = _authState.asStateFlow()
    
    fun login(username: String, password: String, accessType: AccessType) {
        viewModelScope.launch {
            _authState.value = _authState.value.copy(isLoading = true, error = null)
            
            authRepository.login(username, password, accessType)
                .onSuccess { response ->
                    _authState.value = _authState.value.copy(
                        user = response.user,
                        isLoading = false,
                        error = null
                    )
                }
                .onFailure { exception ->
                    _authState.value = _authState.value.copy(
                        isLoading = false,
                        error = exception.message ?: "Login failed"
                    )
                }
        }
    }
    
    fun signup(signupData: com.unicred.data.SignupData) {
        viewModelScope.launch {
            _authState.value = _authState.value.copy(isLoading = true, error = null)
            
            authRepository.signup(signupData)
                .onSuccess { user ->
                    _authState.value = _authState.value.copy(
                        isLoading = false,
                        error = null
                    )
                }
                .onFailure { exception ->
                    _authState.value = _authState.value.copy(
                        isLoading = false,
                        error = exception.message ?: "Signup failed"
                    )
                }
        }
    }
    
    fun setUser(user: User) {
        _authState.value = _authState.value.copy(user = user)
    }
    
    fun logout() {
        _authState.value = AuthState()
    }
    
    fun clearError() {
        _authState.value = _authState.value.copy(error = null)
    }
}