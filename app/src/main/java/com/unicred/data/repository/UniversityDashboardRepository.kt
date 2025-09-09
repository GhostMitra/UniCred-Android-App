package com.unicred.data.repository

import com.unicred.data.DashboardMetrics
import com.unicred.data.remote.ApiService
import com.unicred.data.mappers.toDomainDashboardMetrics // Ensure this is imported correctly
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UniversityDashboardRepository @Inject constructor(
    private val apiService: ApiService
) {

    suspend fun getUniversityDashboardData(): Result<DashboardMetrics> {
        return try {
            val response = apiService.getMetricsOverview()
            if (response.isSuccessful) {
                val metricsOverviewResponse = response.body()
                if (metricsOverviewResponse != null) {
                    Result.success(metricsOverviewResponse.toDomainDashboardMetrics())
                } else {
                    Result.failure(Exception("Empty response body from API for metrics overview"))
                }
            } else {
                Result.failure(Exception("API call failed for metrics overview with error code: ${response.code()} - ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Network error or exception during metrics overview fetch: ${e.message}", e))
        }
    }
}
