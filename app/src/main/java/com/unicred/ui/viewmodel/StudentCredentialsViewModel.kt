package com.unicred.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unicred.data.Credential
import com.unicred.data.remote.responses.ApiStudentWalletInfo // For student details
import com.unicred.data.repository.StudentWalletRepository
import com.unicred.data.mappers.toDomainCredential // Mapper for ApiCredentialEntry
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class StudentCredentialsUiState(
    val isLoading: Boolean = true,
    val credentials: List<Credential> = emptyList(),
    val studentInfo: ApiStudentWalletInfo? = null,
    val errorMessage: String? = null
)

@HiltViewModel
class StudentCredentialsViewModel @Inject constructor(
    private val studentWalletRepository: StudentWalletRepository
    // TODO: Inject AuthRepository or UserSessionManager to get the actual logged-in student's username
) : ViewModel() {

    private val _uiState = MutableStateFlow(StudentCredentialsUiState())
    val uiState: StateFlow<StudentCredentialsUiState> = _uiState.asStateFlow()

    init {
        // TODO: Replace "STU001" with actual username from AuthRepository/SessionManager
        // This ViewModel assumes it's fetching credentials for a specific student, e.g., the logged-in one.
        fetchStudentCredentials("STU001")
    }

    fun fetchStudentCredentials(username: String) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(isLoading = true, errorMessage = null)
            }
            studentWalletRepository.getStudentWalletCredentials(username)
                .onSuccess { apiResponse -> // apiResponse is StudentWalletApiResponse
                    val studentName = apiResponse.student.name
                    val mappedCredentials = apiResponse.credentials.map {
                        it.toDomainCredential(studentFullName = studentName)
                    }
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            studentInfo = apiResponse.student,
                            credentials = mappedCredentials
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
