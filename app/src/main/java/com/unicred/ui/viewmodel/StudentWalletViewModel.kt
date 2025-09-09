package com.unicred.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unicred.data.Credential
import com.unicred.data.mappers.toDomainCredential // Import the mapper
import com.unicred.data.repository.StudentWalletRepository
// StudentWalletApiResponse is implicitly handled by the repository's return type
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
                .onSuccess { apiResponse -> // apiResponse is StudentWalletApiResponse
                    val studentNameFromApi = apiResponse.student.name
                    val mappedCredentials = apiResponse.credentials.map {
                        it.toDomainCredential(studentFullName = studentNameFromApi)
                    }
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            credentials = mappedCredentials,
                            studentName = studentNameFromApi
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
