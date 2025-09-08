package com.unicred.data

import com.unicred.network.ApiService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val apiService: ApiService
) {

    suspend fun login(username: String, password: String, accessType: AccessType): Result<LoginResponse> {
        return try {
            val accessTypeString = when (accessType) {
                AccessType.STUDENT -> "student"
                AccessType.RECRUITER -> "recruiter"
                AccessType.UNIVERSITY -> "university"
                AccessType.ADMIN -> "admin" // Or handle as an error/different logic if ADMIN login isn't standard
            }
            val request = LoginRequest(
                id = username, // API expects the username in the "id" field for login
                password = password,
                accessType = accessTypeString
            )
            val response = apiService.login(request)

            if (response.isSuccessful && response.body() != null) {
                val loginResponse = response.body()!!
                // Populate the client-side role in the User object
                loginResponse.user.role = accessType
                Result.success(loginResponse)
            } else {
                Result.failure(Exception("Login failed: ${response.code()} ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Updated signup function to correctly handle SignupResponse and map to User
    // According to new API docs, POST /signup returns { message, user (full object) }
    // For now, assuming SignupData is correctly defined for the request.
    // The ApiService.signup should return Response<SignupResponse> (which would contain the user)
    suspend fun signup(signupData: SignupData): Result<User> { // Changed return type to User based on API
        return try {
            // This assumes ApiService.signup is updated to return Response<com.unicred.data.responses.SignupResponse>
            // where com.unicred.data.responses.SignupResponse includes a User field.
            // For now, I'm using the existing apiService.signup which returned Response<SignupResponse>
            // which itself was a User-like object. This needs to be aligned with the actual SignupResponse data class.
            val response = apiService.signup(signupData) // TODO: Ensure ApiService.signup and SignupResponse are aligned with new docs

            if (response.isSuccessful && response.body() != null) {
                val signupAPIResponse = response.body()!! // This is currently the old SignupResponse (User-like)
                // TODO: Adapt this if SignupResponse is a new class containing a User field and message.
                // For now, assuming signupAPIResponse has a nested .user object as per API docs comments.
                val user = User(
                    id = signupAPIResponse.user.id, // Accessing from nested user object
                    username = signupAPIResponse.user.username ?: signupData.username, // Accessing from nested user object
                    email = signupAPIResponse.user.email, // Accessing from nested user object
                    fullName = null, 
                    studentId = null 
                    // role will be set based on signupData.accessType or signupAPIResponse.role
                )
                // Convert string role from signupData to AccessType enum for the User object
                user.role = when (signupData.accessType.lowercase()) { // Changed from signupData.role
                    "student" -> AccessType.STUDENT
                    "recruiter" -> AccessType.RECRUITER
                    "university" -> AccessType.UNIVERSITY
                    "admin" -> AccessType.ADMIN
                    else -> null // Or a default/error
                }
                Result.success(user)
            } else {
                Result.failure(Exception("Signup failed: ${response.code()} ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
