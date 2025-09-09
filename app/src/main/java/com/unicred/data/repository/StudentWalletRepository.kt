package com.unicred.data.repository

import com.unicred.data.Credential
import com.unicred.data.remote.ApiService
import com.unicred.data.mappers.toDomainCredential // Ensure this mapper is correctly imported
import javax.inject.Inject
import javax.inject.Singleton

@Singleton // Good practice for repositories if they don't hold state tied to a specific ViewModel lifecycle
class StudentWalletRepository @Inject constructor(
    private val apiService: ApiService
) {

    suspend fun getStudentWalletCredentials(username: String): Result<List<Credential>> {
        return try {
            val response = apiService.getStudentCredentialsByUsername(username)
            if (response.isSuccessful) {
                val studentWalletApiResponse = response.body()
                if (studentWalletApiResponse != null) {
                    val studentName = studentWalletApiResponse.student.name
                    val mappedCredentials = studentWalletApiResponse.credentials.map {
                        // Assuming ApiCredentialEntry.toDomainCredential can handle a nullable studentName
                        // if studentWalletApiResponse.student.name could be null (though not in current ApiStudentWalletInfo def)
                        it.toDomainCredential(studentFullName = studentName) 
                    }
                    Result.success(mappedCredentials)
                } else {
                    Result.failure(Exception("Empty response body from API"))
                }
            } else {
                Result.failure(Exception("API call failed with error code: ${response.code()} - ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Network error or exception: ${e.message}", e))
        }
    }
}
