package com.unicred.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unicred.data.Credential
import com.unicred.data.repository.StudentWalletRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class StudentWalletUiState(
    val isLoading: Boolean = true,
    val credentials: List<Credential> = emptyList(),
    val errorMessage: String? = null,
    // Optional: Student name could be sourced from a general User session state or a different API call
    // For now, not directly populated by fetchWalletCredentials as the repo returns List<Credential>
    val studentName: String? = null 
)

@HiltViewModel
class StudentWalletViewModel @Inject constructor(
    private val studentWalletRepository: StudentWalletRepository
    // TODO: Inject AuthRepository or UserSessionManager to get the actual logged-in username
) : ViewModel() {

    private val _uiState = MutableStateFlow(StudentWalletUiState())
    val uiState: StateFlow<StudentWalletUiState> = _uiState.asStateFlow()

    init {
        // TODO: Replace "STU001" with actual username from AuthRepository/SessionManager
        fetchWalletCredentials("STU001") 
    }

    fun fetchWalletCredentials(username: String) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(isLoading = true, errorMessage = null)
            }
            studentWalletRepository.getStudentWalletCredentials(username)
                .onSuccess { fetchedCredentials ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            credentials = fetchedCredentials,
                            // studentName can be set here if the repository call also returned student info
                            // or if we fetch it separately based on the username.
                        )
                    }
                }
                .onFailure { exception ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = exception.message ?: "An unknown error occurred."
                        )
                    }
                }
        }
    }
}
