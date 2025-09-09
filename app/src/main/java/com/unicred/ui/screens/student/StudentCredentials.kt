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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.unicred.data.Credential
import com.unicred.data.CredentialStatus
import com.unicred.ui.viewmodel.StudentCredentialsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentCredentials(
    viewModel: StudentCredentialsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        when {
            uiState.isLoading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
            uiState.errorMessage != null -> {
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
            }
            else -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item {
                        Text(
                            text = "My Credentials",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 8.dp, top = 8.dp) // Added top padding for consistency
                        )
                    }

                    if (uiState.credentials.isEmpty()) {
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
                            DetailedCredentialCard(credential = credential)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DetailedCredentialCard(credential: Credential) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(1.dp))
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)
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
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = credential.institution,
                        style = MaterialTheme.typography.titleSmall, 
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                StatusChip(status = credential.status) 
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f), thickness = 0.5.dp) // Changed to HorizontalDivider
            Spacer(modifier = Modifier.height(16.dp))

            CredentialDetailRow(
                icon = Icons.Filled.VpnKey, 
                label = "Credential ID",
                value = credential.id
            )
            
            CredentialDetailRow(
                icon = Icons.Default.School,
                label = "Type",
                value = credential.type.name.lowercase().replaceFirstChar { it.uppercase() }
            )
            
            CredentialDetailRow(
                icon = Icons.Default.CalendarToday,
                label = "Date Issued",
                value = credential.dateIssued 
            )

            credential.studentName?.let {
                CredentialDetailRow(
                    icon = Icons.Default.PersonOutline,
                    label = "Student Name",
                    value = it
                )
            }
            credential.studentId?.let {
                 CredentialDetailRow(
                    icon = Icons.Default.Badge,
                    label = "Student ID",
                    value = it
                )
            }
            
            credential.blockchainHash?.let {
                CredentialDetailRow(
                    icon = Icons.Default.Security,
                    label = "Blockchain Hash",
                    value = it.take(20) + "...", 
                    isClickable = true 
                )
            }
            
            Spacer(modifier = Modifier.height(20.dp)) 
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp) 
            ) {
                OutlinedButton(
                    onClick = { /* TODO: Share credential */ },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "Share",
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Share")
                }
                
                if (credential.status == CredentialStatus.VERIFIED) {
                    FilledTonalButton( // Changed to FilledTonalButton
                        onClick = { /* TODO: Download PDF */ },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Download,
                            contentDescription = "Download",
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Download")
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
            .padding(vertical = 6.dp), // Adjusted vertical padding
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            modifier = Modifier.size(20.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f) 
        )
        Spacer(modifier = Modifier.width(16.dp)) 
        Text(
            text = "$label:",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.width(115.dp) // Adjusted label width
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold, 
            color = if (isClickable) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun StatusChip(status: CredentialStatus?) {
    val currentStatus = status ?: CredentialStatus.UNKNOWN
    val (backgroundColor, textColor, text) = when (currentStatus) {
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
        color = backgroundColor,
        tonalElevation = 1.dp 
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp), 
            style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.Medium
            ),
            color = textColor
        )
    }
}
