package com.unicred.data.repository

import com.unicred.data.remote.ApiService // Import ApiService
import com.unicred.data.remote.responses.StudentMetricsResponse // Import the response type
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StudentDashboardRepository @Inject constructor(
    private val apiService: ApiService // Inject ApiService
) {

    suspend fun getStudentDashboardData(studentId: String): StudentMetricsResponse {
        // Call the ApiService to get data from the backend
        return apiService.getStudentMetrics(studentId)
    }
}
