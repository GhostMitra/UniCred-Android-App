package com.unicred.ui.screens.student

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.unicred.data.Credential
import com.unicred.data.CredentialStatus
// import com.unicred.data.CredentialType // No longer directly used in mock data
import com.unicred.data.User
// You will need to create this ViewModel and its UiState data class
// import com.unicred.ui.viewmodel.StudentDashboardViewModel 
// import com.unicred.ui.viewmodel.StudentDashboardUiState

// Conceptual UiState data class (you'll need to define this, likely in your ViewModel file)
// data class StudentDashboardUiState(
//     val user: User? = null,
//     val credentials: List<Credential> = emptyList(),
//     val totalCredentialsCount: Int = 0,
//     val verifiedCredentialsCount: Int = 0,
//     val pendingCredentialsCount: Int = 0,
//     val isLoading: Boolean = true, // Start with loading true
//     val errorMessage: String? = null
// )

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentDashboard(
    // viewModel: StudentDashboardViewModel = hiltViewModel() // Uncomment when ViewModel is created
) {
    // val uiState by viewModel.uiState.collectAsState() // Uncomment when ViewModel is created

    // Using placeholder state until ViewModel is implemented
    // Replace this with actual ViewModel state once it's available.
    val placeholderUser = User(id = "user123", studentId = "STU001", username = "johndoe", fullName = "John Doe", email = "john.doe@example.com")
    val placeholderCredentials = listOf(
        Credential(
            id = "1",
            title = "Bachelor of Computer Science",
            type = com.unicred.data.CredentialType.BACHELOR,
            institution = "University of Technology",
            dateIssued = "2023-06-15",
            status = CredentialStatus.VERIFIED,
            studentId = placeholderUser.studentId ?: "STU001",
            studentName = placeholderUser.fullName ?: placeholderUser.username
        ),
        Credential(
            id = "2",
            title = "Machine Learning Certificate",
            type = com.unicred.data.CredentialType.CERTIFICATE,
            institution = "Tech Academy",
            dateIssued = "2023-08-20",
            status = CredentialStatus.PENDING,
            studentId = placeholderUser.studentId ?: "STU001",
            studentName = placeholderUser.fullName ?: placeholderUser.username
        )
    )
    val uiState = remember { // Replace with actual ViewModel state
        mutableStateOf(object {
            val user = placeholderUser
            val credentials = placeholderCredentials
            val totalCredentialsCount = placeholderCredentials.size
            val verifiedCredentialsCount = placeholderCredentials.count { it.status == CredentialStatus.VERIFIED }
            val pendingCredentialsCount = placeholderCredentials.count { it.status == CredentialStatus.PENDING }
            val isLoading = false
            val errorMessage: String? = null
        })
    }.value

    Box(modifier = Modifier.fillMaxSize()) {
        if (uiState.isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        } else if (uiState.errorMessage != null) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .align(Alignment.Center),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = uiState.errorMessage ?: "An unexpected error occurred.",
                    color = MaterialTheme.colorScheme.onErrorContainer,
                    modifier = Modifier.padding(16.dp).fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }
        } else if (uiState.user != null) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    // Welcome Card
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Welcome back!",
                                style = MaterialTheme.typography.headlineSmall.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = uiState.user.fullName ?: uiState.user.username ?: "N/A",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                            if (uiState.user.studentId != null) {
                                Text(
                                    text = "Student ID: ${uiState.user.studentId}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                                )
                            }
                        }
                    }
                }

                item {
                    // Stats Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        StatCard(
                            title = "Total Credentials",
                            value = uiState.totalCredentialsCount.toString(),
                            icon = Icons.Default.School,
                            modifier = Modifier.weight(1f)
                        )
                        StatCard(
                            title = "Verified",
                            value = uiState.verifiedCredentialsCount.toString(),
                            icon = Icons.Default.Verified,
                            modifier = Modifier.weight(1f)
                        )
                        StatCard(
                            title = "Pending",
                            value = uiState.pendingCredentialsCount.toString(),
                            icon = Icons.Default.Pending,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                item {
                    Text(
                        text = "Recent Credentials",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }

                if (uiState.credentials.isEmpty() && !uiState.isLoading) {
                    item {
                        Text(
                            text = "No credentials found.",
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp)
                        )
                    }
                } else {
                    items(uiState.credentials) { credential ->
                        CredentialCard(credential = credential)
                    }
                }
            }
        } else {
             // Fallback if user is null and not loading and no error (should ideally not happen if logic is correct)
            Text(
                text = "Unable to load dashboard. Please try again later.",
                modifier = Modifier.align(Alignment.Center).padding(16.dp),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun StatCard(
    title: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = title,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
            )
        }
    }
}

@Composable
fun CredentialCard(credential: Credential) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = credential.title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold
                    )
                )
                StatusChip(status = credential.status)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = credential.institution,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Text(
                text = "Issued: ${credential.dateIssued}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
            )
        }
    }
}

@Composable
fun StatusChip(status: CredentialStatus) {
    val (backgroundColor, textColor, text) = when (status) {
        CredentialStatus.VERIFIED -> Triple(
            MaterialTheme.colorScheme.primaryContainer,
            MaterialTheme.colorScheme.onPrimaryContainer,
            "Verified"
        )
        CredentialStatus.PENDING -> Triple(
            MaterialTheme.colorScheme.secondaryContainer,
            MaterialTheme.colorScheme.onSecondaryContainer,
            "Pending"
        )
        CredentialStatus.EXPIRED -> Triple(
            MaterialTheme.colorScheme.errorContainer,
            MaterialTheme.colorScheme.onErrorContainer,
            "Expired"
        )
        CredentialStatus.REVOKED -> Triple(
            MaterialTheme.colorScheme.errorContainer,
            MaterialTheme.colorScheme.onErrorContainer,
            "Revoked"
        )
        CredentialStatus.ANCHORED -> Triple(
            MaterialTheme.colorScheme.tertiaryContainer,
            MaterialTheme.colorScheme.onTertiaryContainer,
            "Anchored"
        )
        CredentialStatus.ACCEPTED -> Triple(
            MaterialTheme.colorScheme.primaryContainer,
            MaterialTheme.colorScheme.onPrimaryContainer,
            "Accepted"
        )
        CredentialStatus.UNKNOWN -> Triple(
            MaterialTheme.colorScheme.surfaceVariant,
            MaterialTheme.colorScheme.onSurfaceVariant,
            "Unknown"
        )
    }

    Surface(
        shape = RoundedCornerShape(16.dp),
        color = backgroundColor
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Medium
            ),
            color = textColor
        )
    }
}
