package com.unicred.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unicred.data.Credential
import com.unicred.data.User
import com.unicred.data.repository.StudentDashboardRepository
import com.unicred.data.mappers.toStudentDashboardUiState
// TODO: If you create an AuthRepository, you would inject it here
// import com.unicred.data.repository.AuthRepository 
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

// UI State data class - this should be fine as is
data class StudentDashboardUiState(
    val user: User? = null,
    val credentials: List<Credential> = emptyList(),
    val totalCredentialsCount: Int = 0,
    val verifiedCredentialsCount: Int = 0,
    val pendingCredentialsCount: Int = 0,
    val isLoading: Boolean = true,
    val errorMessage: String? = null
)

@HiltViewModel
class StudentDashboardViewModel @Inject constructor(
    private val studentRepository: StudentDashboardRepository
    // TODO: Inject your AuthRepository or UserSessionManager here if you create one
    // private val authRepository: AuthRepository // Example
) : ViewModel() {

    private val _uiState = MutableStateFlow(StudentDashboardUiState())
    val uiState: StateFlow<StudentDashboardUiState> = _uiState.asStateFlow()

    init {
        fetchDashboardData()
    }

    fun fetchDashboardData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                // Get the current logged-in user (using placeholder for now)
                val currentUser: User? = getCurrentLoggedInUser()

                if (currentUser?.studentId == null) {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = "User not logged in or student ID not found. Ensure getCurrentLoggedInUser() is correctly implemented."
                        )
                    }
                    return@launch
                }

                // Perform the actual API call
                val dashboardResponse = studentRepository.getStudentDashboardData(currentUser.studentId)
                
                if (dashboardResponse != null) {
                    // Map the successful API response to the UI state
                    _uiState.update { dashboardResponse.toStudentDashboardUiState(currentUser) }
                } else {
                    // Handle null response from repository (e.g., API error not resulting in exception)
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = "Failed to load dashboard: No data received from server."
                        )
                    }
                }

            } catch (e: Exception) {
                // Handle any errors during the API call or mapping
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Failed to load dashboard: ${e.message} (Check Logcat for API request/response details)"
                    )
                }
                e.printStackTrace() // Print stack trace for debugging
            }
        }
    }

    // THIS IS A TEMPORARY PLACEHOLDER IMPLEMENTATION.
    // TODO: Replace this with your actual logic to get the logged-in user.
    // This might involve calling a method on an injected AuthRepository or UserSessionManager.
    private fun getCurrentLoggedInUser(): User? {
        println("WARNING: Using placeholder user in StudentDashboardViewModel. Implement getCurrentLoggedInUser() with actual session logic!")
        return User(
            id = "cmexa6mti0000v2vlqpypu8un", 
            username = "STU001",             
            accessType = "student",          
            email = "student1@example.edu",  
            fullName = "Student One (Placeholder)", 
            studentId = "cmeyt1zxl000dy5ie2yc1zr93" // This ID is used for the API call
        )
    }
}
