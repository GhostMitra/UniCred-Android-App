package com.unicred.ui.screens.university

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.unicred.data.DashboardCredentialSummary
import com.unicred.data.DashboardMetrics
import com.unicred.data.User
import com.unicred.ui.screens.university.StudentWalletScreen // Added import

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UniversityPortal(
    user: User,
    onLogout: () -> Unit
) {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                val universityNavItems = listOf(
                    Triple("university_dashboard", Icons.Default.Dashboard, "Dashboard"),
                    Triple("university_student_directory", Icons.Default.Group, "Students"),
                    Triple("university_credential_management", Icons.Default.Assignment, "Credentials"),
                    Triple("university_settings", Icons.Default.Settings, "Settings")
                )

                universityNavItems.forEach { (route, icon, label) ->
                    NavigationBarItem(
                        icon = { Icon(icon, contentDescription = label) },
                        label = { Text(label) },
                        selected = currentDestination?.hierarchy?.any { it.route == route } == true,
                        onClick = {
                            navController.navigate(route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "university_dashboard",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("university_dashboard") {
                UniversityDashboardScreen(user = user)
            }
            composable("university_student_directory") {
                UniversityPortalStudentDirectoryPlaceholder() // Changed call
            }
            composable("university_credential_management") {
                StudentWalletScreen() // Changed from CredentialManagement()
            }
            composable("university_settings") {
                UniversitySettings(user = user, onLogout = onLogout)
            }
        }
    }
}

@Composable
fun UniversityDashboardScreen(user: User) {
    val mockMetrics = remember {
        DashboardMetrics(
            totalStudents = 2500,
            totalCredentials = 8000,
            verifiedCount = 6500,
            pendingCount = 1200,
            recentCredentials = listOf(
                DashboardCredentialSummary("cred_U001", "Master of Science in AI", "State University", "stu_U101", "Uni Student Alpha", "verified", "2024-07-20T10:00:00.000Z"),
                DashboardCredentialSummary("cred_U002", "Bachelor of Arts", "Liberal Arts College", "stu_U102", "Uni Student Beta", "pending", "2024-07-19T11:30:00.000Z"),
                DashboardCredentialSummary("cred_U003", "PhD in Neuroscience", "Research University", "stu_U103", "Uni Student Gamma", "verified", "2024-07-18T09:15:00.000Z")
            )
        )
    }

    LazyColumn(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "Welcome, ${user.fullName ?: user.username}!",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Platform Overview:",
                style = MaterialTheme.typography.titleMedium
            )
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                UniversityStatCard("Total Students", mockMetrics.totalStudents.toString(), Icons.Default.People, Modifier.weight(1f))
                UniversityStatCard("Total Credentials Issued", mockMetrics.totalCredentials.toString(), Icons.Default.Article, Modifier.weight(1f))
            }
        }
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                UniversityStatCard("Verified Credentials", mockMetrics.verifiedCount.toString(), Icons.Default.CheckCircle, Modifier.weight(1f), MaterialTheme.colorScheme.primaryContainer)
                UniversityStatCard("Pending Verifications", mockMetrics.pendingCount.toString(), Icons.Default.HourglassEmpty, Modifier.weight(1f), MaterialTheme.colorScheme.secondaryContainer)
            }
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Recently Issued Credentials",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }

        items(mockMetrics.recentCredentials) { credential ->
            UniversityRecentCredentialCard(credential = credential)
        }
    }
}

@Composable
fun UniversityStatCard(
    title: String,
    value: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    containerColor: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.surfaceVariant
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(imageVector = icon, contentDescription = title, modifier = Modifier.size(36.dp), tint = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = value, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            Text(text = title, style = MaterialTheme.typography.bodyMedium, textAlign = TextAlign.Center)
        }
    }
}

@Composable
fun UniversityRecentCredentialCard(credential: DashboardCredentialSummary) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = credential.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Institution: ${credential.institution}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            credential.studentName?.let {
                 Text(
                    text = "Student: $it (${credential.studentId ?: "N/A"})",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } ?: Text(
                    text = "Student ID: ${credential.studentId ?: "N/A"}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = if (credential.status.equals("verified", ignoreCase = true)) Icons.Filled.CheckCircle else Icons.Filled.HourglassEmpty,
                    contentDescription = credential.status,
                    tint = if (credential.status.equals("verified", ignoreCase = true)) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Status: ${credential.status.replaceFirstChar { it.uppercase() }}",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Medium
                )
            }
            Text(
                text = "Issued: ${credential.dateIssued.substringBefore("T")}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
            )
        }
    }
}

@Composable
fun UniversityPortalStudentDirectoryPlaceholder() { // Renamed function
    // Placeholder for Student Directory
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Student Directory Screen - Coming Soon!")
    }
}

// Note: If your existing StudentDirectory, or UniversitySettings
// composables require 'user' or 'onLogout' parameters or need to be adapted for
// this navigation structure, you might need to create wrapper composables or modify them.
