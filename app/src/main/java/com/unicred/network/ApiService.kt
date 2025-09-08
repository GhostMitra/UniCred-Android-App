package com.unicred.network

import com.unicred.data.AnchorCredentialResponse
import com.unicred.data.CreateCredentialRequest
import com.unicred.data.CreateStudentRequest
import com.unicred.data.Credential
import com.unicred.data.HealthResponse
import com.unicred.data.LedgerEvent
import com.unicred.data.LoginRequest
import com.unicred.data.LoginResponse
import com.unicred.data.MetricsOverviewResponse
import com.unicred.data.RecruiterApproveResponse
import com.unicred.data.RevokeCredentialResponse
import com.unicred.data.SearchCredentialsResponse
import com.unicred.data.SignupApiResponse
import com.unicred.data.SignupData
import com.unicred.data.Student
import com.unicred.data.StudentAcceptResponse
import com.unicred.data.StudentMetricsInfo
import com.unicred.data.StudentMetricsResponse
import com.unicred.data.StudentStats
import com.unicred.data.StudentWalletResponse
import com.unicred.data.VerifyCredentialResponse
import com.unicred.data.WalletJwk
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {

    // --- Health Endpoint ---
    @GET("/health")
    suspend fun getHealth(): Response<HealthResponse>

    // --- Authentication Endpoints ---
    @POST("/api/auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @POST("/api/auth/seed-demo")
    suspend fun seedDemoAuth(): Response<HealthResponse>

    // --- Optional Signup Endpoint ---
    @POST("/signup")
    suspend fun signup(@Body request: SignupData): Response<SignupApiResponse>

    // --- Credential Endpoints ---
    @GET("/api/credentials/")
    suspend fun getAllCredentials(): Response<List<Credential>>

    @POST("/api/credentials/")
    suspend fun issueCredential(@Body request: CreateCredentialRequest): Response<Credential> // 201 Created

    @GET("/api/credentials/verify/{hash}")
    suspend fun verifyCredentialByHash(@Path("hash") hash: String): Response<VerifyCredentialResponse>

    @GET("/api/credentials/search/{query}")
    suspend fun searchCredentials(@Path("query") query: String): Response<SearchCredentialsResponse>

    @POST("/api/credentials/{id}/anchor")
    suspend fun anchorCredential(@Path("id") credentialId: String): Response<AnchorCredentialResponse>

    @POST("/api/credentials/{id}/revoke")
    suspend fun revokeCredential(@Path("id") credentialId: String): Response<RevokeCredentialResponse>

    @POST("/api/credentials/{id}/recruiter-approve")
    suspend fun recruiterApproveCredential(@Path("id") credentialId: String): Response<RecruiterApproveResponse>

    @POST("/api/credentials/{id}/student-accept")
    suspend fun studentAcceptCredential(@Path("id") credentialId: String): Response<StudentAcceptResponse>

    @POST("/api/credentials/seed-mock")
    suspend fun seedMockCredentials(): Response<HealthResponse>

    // --- Student Endpoints ---
    @GET("/api/students/")
    suspend fun getAllStudents(): Response<List<Student>>

    @POST("/api/students/")
    suspend fun createStudent(@Body request: CreateStudentRequest): Response<Student> // 201 Created

    @GET("/api/students/{id}/wallet")
    suspend fun getStudentWallet(@Path("id") studentId: String): Response<StudentWalletResponse>

    @GET("/api/students/{id}/credentials")
    suspend fun getStudentCredentials(@Path("id") studentId: String): Response<List<Credential>>

    @GET("/api/students/username/{username}/credentials")
    suspend fun getStudentCredentialsByUsername(@Path("username") username: String): Response<List<Credential>>

    @GET("/api/students/debug/all")
    suspend fun getAllStudentsDebug(): Response<List<Student>> // Assuming List<Student> for raw dump

    // --- Metrics Endpoints ---
    @GET("/api/metrics/")
    suspend fun getMetricsOverview(): Response<MetricsOverviewResponse>

    @GET("/api/metrics/student/{id}")
    suspend fun getStudentMetrics(@Path("id") studentId: String): Response<StudentMetricsResponse>

    // --- Seed Endpoints (Dev Only) ---
    @POST("/api/seed/all")
    suspend fun seedAllData(): Response<HealthResponse>

    @POST("/api/seed/stu001-credentials")
    suspend fun seedStu001Credentials(): Response<HealthResponse>

}
