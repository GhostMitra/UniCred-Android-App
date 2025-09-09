package com.unicred.ui.screens.student

import androidx.compose.foundation.layout.* // ktlint-disable no-wildcard-imports
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
import androidx.hilt.navigation.compose.hiltViewModel
import com.unicred.data.Credential
// StatusChip will be resolved from StudentCredentials.kt in the same package
// import com.unicred.data.CredentialStatus // Not needed if StatusChip handles it
import com.unicred.ui.viewmodel.StudentDashboardViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentDashboard(
    viewModel: StudentDashboardViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

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
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp), // Consistent content padding
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    // Welcome Card
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        ),
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp, vertical = 24.dp), // Enhanced padding
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Welcome back!",
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = uiState.user?.fullName ?: uiState.user?.username ?: "N/A",
                                style = MaterialTheme.typography.titleLarge, // Made more prominent
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                            if (uiState.user?.studentId != null) {
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Student ID: ${uiState.user?.studentId}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                                )
                            }
                        }
                    }
                }

                item {
                    // Stats Row - Already refined, ensure consistency
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(IntrinsicSize.Min),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        StatCard(
                            title = "Total Credentials",
                            value = uiState.totalCredentialsCount.toString(),
                            icon = Icons.Default.School,
                            modifier = Modifier.weight(1f).fillMaxHeight()
                        )
                        StatCard(
                            title = "Verified",
                            value = uiState.verifiedCredentialsCount.toString(),
                            icon = Icons.Default.VerifiedUser, // Changed icon for clarity
                            modifier = Modifier.weight(1f).fillMaxHeight()
                        )
                        StatCard(
                            title = "Pending",
                            value = uiState.pendingCredentialsCount.toString(),
                            icon = Icons.Default.PendingActions, // Changed icon for clarity
                            modifier = Modifier.weight(1f).fillMaxHeight()
                        )
                    }
                }

                item {
                    Text(
                        text = "Recent Credentials",
                        style = MaterialTheme.typography.headlineMedium, // Consistent section title
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 16.dp, bottom = 8.dp) // Consistent padding
                    )
                }

                if (uiState.credentials.isEmpty() && !uiState.isLoading) {
                    item {
                        Text(
                            text = "No credentials found.",
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth().padding(vertical = 24.dp)
                        )
                    }
                } else {
                    items(
                        items = uiState.credentials,
                        key = { credential -> credential.id } // Use credential.id as a stable key
                    ) { credential ->
                        CredentialCard(credential = credential)
                    }
                }
            }
        } else {
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
            containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(1.dp) // Consistent card color
        ),
        shape = RoundedCornerShape(16.dp), // Consistent shape
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize(), 
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center 
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(32.dp) // Slightly larger icon
            )
            Spacer(modifier = Modifier.height(12.dp)) // Increased space
            Text(
                text = value,
                style = MaterialTheme.typography.headlineMedium, // Adjusted for consistency
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun CredentialCard(credential: Credential) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp), // Consistent shape
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(1.dp)) // Consistent color
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp) // Consistent padding
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top 
            ) {
                Column(modifier = Modifier.weight(1f).padding(end = 8.dp)) {
                    Text(
                        text = credential.title,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        maxLines = 2, // Allow title to wrap if long
                        overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = credential.institution,
                        style = MaterialTheme.typography.titleSmall, 
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                    )
                }
                StatusChip(status = credential.status) // Uses shared StatusChip
            }

            Spacer(modifier = Modifier.height(16.dp)) 

            Row(verticalAlignment = Alignment.CenterVertically) {
                 Icon(
                    imageVector = Icons.Filled.CalendarToday,
                    contentDescription = "Date Issued",
                    modifier = Modifier.size(18.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Issued: ${credential.dateIssued}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                )
            }
        }
    }
}

// Removed local StatusChip definition to use the one from StudentCredentials.kt (same package)

