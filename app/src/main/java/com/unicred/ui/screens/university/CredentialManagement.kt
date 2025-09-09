package com.unicred.ui.screens.university // Package can be refactored later if screen moves

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
import androidx.hilt.navigation.compose.hiltViewModel
import com.unicred.data.Credential
import com.unicred.data.CredentialStatus
// import com.unicred.data.CredentialType // Not directly used in top-level composable
import com.unicred.ui.screens.student.StatusChip // Using the one from student package
import com.unicred.ui.viewmodel.StudentWalletViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentWalletScreen( // Renamed composable
    viewModel: StudentWalletViewModel = hiltViewModel()
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
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "My Credentials", // Changed title
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold
                        )
                        // Removed FAB for creating credentials
                    }
                }

                // Removed Search Bar

                item {
                    // Stats Row - now based on uiState.credentials
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        CredentialStatChip(
                            label = "Total",
                            value = uiState.credentials.size.toString(),
                            modifier = Modifier.weight(1f)
                        )
                        CredentialStatChip(
                            label = "Verified",
                            value = uiState.credentials.count { it.status == CredentialStatus.VERIFIED }.toString(),
                            modifier = Modifier.weight(1f)
                        )
                        CredentialStatChip(
                            label = "Pending",
                            value = uiState.credentials.count { it.status == CredentialStatus.PENDING }.toString(),
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                if (uiState.credentials.isEmpty()) {
                    item {
                        Text(
                            text = "No credentials found in your wallet.",
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth().padding(vertical = 24.dp)
                        )
                    }
                } else {
                    items(uiState.credentials) { credential ->
                        CredentialManagementCard(credential = credential) // Reusing this card
                    }
                }
            }
        }
    }
    // Removed CreateCredentialDialog and its state
}

@Composable
fun CredentialStatChip(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
            )
        }
    }
}

@Composable
fun CredentialManagementCard(credential: Credential) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = credential.title,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = credential.studentName ?: "N/A", // Student name might be null
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                StatusChip(status = credential.status) // Uses imported StatusChip
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            CredentialDetailRow(
                icon = Icons.Default.School,
                label = "Type",
                value = credential.type.name.lowercase().replaceFirstChar { it.uppercase() }
            )
            
            CredentialDetailRow(
                icon = Icons.Default.Badge, // Changed icon for Student ID for variety
                label = "Student ID",
                value = credential.studentId ?: "N/A"
            )
            
            CredentialDetailRow(
                icon = Icons.Default.CalendarToday,
                label = "Date Issued",
                value = credential.dateIssued
            )

            if (credential.blockchainHash != null) {
                CredentialDetailRow(
                    icon = Icons.Default.Security,
                    label = "Blockchain Hash",
                    value = credential.blockchainHash.take(20) + "...",
                    isClickable = true // Placeholder for potential copy/view action
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Action Buttons - Kept for now, may need to be adapted for student wallet context
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = { /* TODO: Implement View details for student wallet */ },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Visibility,
                        contentDescription = "View",
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("View")
                }
                
                // Approve/Revoke buttons are university-centric, might not apply for student wallet view
                // Or could be re-purposed for actions like "Accept Credential" if applicable
                if (credential.status == CredentialStatus.PENDING) {
                    Button(
                        onClick = { /* TODO: Action for PENDING in student wallet? */ },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check, // Placeholder icon
                            contentDescription = "Action for Pending", // Placeholder
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Pending Action") // Placeholder
                    }
                } else if (credential.status == CredentialStatus.VERIFIED && credential.studentAccepted == false) {
                     Button(
                        onClick = { /* TODO: Implement Student Accept Credential */ },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ThumbUp,
                            contentDescription = "Accept",
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Accept")
                    }
                }
            }
        }
    }
}

@Composable
fun CredentialDetailRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String,
    isClickable: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            modifier = Modifier.size(20.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = "$label:",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.width(100.dp) // Adjusted width for potentially longer labels
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = if (isClickable) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.onSurface
            },
            maxLines = 1, // Ensure value doesn't wrap excessively if too long
            overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis // Add ellipsis for long values
        )
    }
}

// Removed local StatusChip definition, will use the one from com.unicred.ui.screens.student.StatusChip
// Removed CreateCredentialDialog composable
