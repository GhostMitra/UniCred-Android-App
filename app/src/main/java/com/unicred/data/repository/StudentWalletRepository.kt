package com.unicred.data.repository

// import com.unicred.data.Credential // No longer directly returned here
import com.unicred.data.remote.ApiService
import com.unicred.data.remote.responses.StudentWalletApiResponse // Import for return type
// import com.unicred.data.mappers.toDomainCredential // Mapping will be done in ViewModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StudentWalletRepository @Inject constructor(
    private val apiService: ApiService
) {

    suspend fun getStudentWalletCredentials(username: String): Result<StudentWalletApiResponse> {
        return try {
            val response = apiService.getStudentCredentialsByUsername(username)
            if (response.isSuccessful) {
                val studentWalletApiResponse = response.body()
                if (studentWalletApiResponse != null) {
                    Result.success(studentWalletApiResponse)
                } else {
                    Result.failure(Exception("Empty response body from student credentials API"))
                }
            } else {
                Result.failure(Exception("API call failed for student credentials with error code: ${response.code()} - ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Network error or exception for student credentials: ${e.message}", e))
        }
    }
}
