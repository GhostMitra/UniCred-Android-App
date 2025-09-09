package com.unicred.data.repository

import com.unicred.data.remote.ApiService // Import ApiService
import com.unicred.data.StudentMetricsResponse // Changed import
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StudentDashboardRepository @Inject constructor(
    private val apiService: ApiService // Inject ApiService
) {

    suspend fun getStudentDashboardData(studentId: String): StudentMetricsResponse? { // This will now resolve to com.unicred.data.StudentMetricsResponse?
        // Call the ApiService to get data from the backend
        val response = apiService.getStudentMetrics(studentId) // This call returns Response<com.unicred.data.StudentMetricsResponse>
        return if (response.isSuccessful) {
            response.body() // This is com.unicred.data.StudentMetricsResponse?
        } else {
            // Handle error, e.g., log error, throw exception, or return null
            // For now, returning null to match the nullable return type
            // You might want to add logging here: e.g., Log.e("StudentDashboardRepo", "Error fetching data: ${response.code()}")
            null
        }
    }
}
