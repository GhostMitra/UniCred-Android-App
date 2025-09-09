package com.unicred.data.remote

import com.unicred.data.remote.responses.StudentMetricsResponse // Added import
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    @GET("api/metrics/student/{studentId}") // Updated endpoint
    suspend fun getStudentMetrics(@Path("studentId") studentId: String): StudentMetricsResponse // Updated return type and method name for clarity
}
