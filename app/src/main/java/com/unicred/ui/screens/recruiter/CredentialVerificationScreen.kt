package com.unicred.ui.screens.recruiter

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.unicred.data.CredentialVerificationResult
import com.unicred.data.User
import com.unicred.data.VerificationStatus
import com.unicred.data.VerifiedCredentialSummary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CredentialVerificationScreen(user: User) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedTab by remember { mutableStateOf("Search by Hash") } // "Search by Hash" or "Upload File"

    val recentVerifications = remember {
        listOf(
            VerifiedCredentialSummary("Bachelor of Computer Science", "John Doe", "STU001", "2024-01-15", VerificationStatus.VERIFIED),
            VerifiedCredentialSummary("Web Development Certificate", "Jane Smith", "STU002", "2024-01-14", VerificationStatus.VERIFIED),
            VerifiedCredentialSummary("Data Science Diploma", "Mike Johnson", "STU003", "2024-01-13", VerificationStatus.PENDING)
        )
    }

    var showVerificationDialog by remember { mutableStateOf(false) }
    var verificationResultData by remember { mutableStateOf<CredentialVerificationResult?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Credential Verification",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Verify the authenticity of student credentials",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            TabButton(
                text = "Search by Hash",
                icon = Icons.Default.Search,
                isSelected = selectedTab == "Search by Hash",
                onClick = { selectedTab = "Search by Hash" },
                modifier = Modifier.weight(1f)
            )
            TabButton(
                text = "Upload File",
                icon = Icons.Default.UploadFile,
                isSelected = selectedTab == "Upload File",
                onClick = { selectedTab = "Upload File" },
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        if (selectedTab == "Search by Hash") {
            Text(
                text = "Search by Credential Hash",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Enter credential hash or ID...") },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search Icon") },
                singleLine = true
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    verificationResultData = CredentialVerificationResult(
                        candidateName = "John Doe",
                        candidateId = "STU001",
                        candidateEmail = "john.doe@email.com",
                        credentialTitle = "Bachelor of Computer Science",
                        credentialType = "bachelor",
                        credentialInstitution = "Tech University",
                        credentialDateIssued = "2023-05-15",
                        credentialStatus = "verified",
                        verificationStatusDisplay = "Authentic",
                        verificationConfidence = "99%",
                        blockchainHash = "0x1234567890abcdef",
                        recruiterApproved = false
                    )
                    showVerificationDialog = true
                },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                Icon(Icons.Default.VerifiedUser, contentDescription = "Verify Icon", modifier = Modifier.size(ButtonDefaults.IconSize))
                Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                Text("Verify Credential", fontSize = 16.sp)
            }
        } else {
            Box(modifier = Modifier.fillMaxWidth().padding(16.dp), contentAlignment = Alignment.Center) {
                Text("Upload File feature is under development.", textAlign = TextAlign.Center)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Recent Verifications",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(recentVerifications) { verificationItem ->
                RecentVerificationCard(item = verificationItem)
            }
        }
    }

    if (showVerificationDialog && verificationResultData != null) {
        VerificationResultDialog(
            result = verificationResultData!!,
            onDismiss = { showVerificationDialog = false },
            onApproveVisibility = {
                verificationResultData = verificationResultData?.copy(recruiterApproved = true)
                // Consider if dialog should auto-dismiss on approve: showVerificationDialog = false
            }
        )
    }
}

@Composable
fun TabButton(
    text: String,
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
    val contentColor = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant

    Button(
        onClick = onClick,
        modifier = modifier.height(48.dp),
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor,
            contentColor = contentColor
        )
    ) {
        Icon(icon, contentDescription = text, modifier = Modifier.size(ButtonDefaults.IconSize))
        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
        Text(text, fontSize = 14.sp)
    }
}

@Composable
fun RecentVerificationCard(item: VerifiedCredentialSummary) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(item.title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "${item.studentName} - ${item.studentId}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                 Text(
                    text = "Verified: ${item.verifiedDate}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            StatusChip(status = item.status)
        }
    }
}

@Composable
fun StatusChip(status: VerificationStatus) {
    val (backgroundColor, textColor, text) = when (status) {
        VerificationStatus.VERIFIED -> Triple(Color(0xFF2E7D32).copy(alpha = 0.2f), Color(0xFF2E7D32), "Verified")
        VerificationStatus.PENDING -> Triple(Color(0xFFFFA000).copy(alpha = 0.2f), Color(0xFFFFA000), "Pending")
        VerificationStatus.FAILED -> Triple(MaterialTheme.colorScheme.errorContainer, MaterialTheme.colorScheme.onErrorContainer, "Failed")
    }

    Surface(
        shape = RoundedCornerShape(16.dp),
        color = backgroundColor,
        modifier = Modifier.height(28.dp)
    ) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)) {
            Text(
                text = text,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Medium,
                color = textColor
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VerificationResultDialog(
    result: CredentialVerificationResult,
    onDismiss: () -> Unit,
    onApproveVisibility: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        modifier = Modifier.fillMaxWidth(0.95f)
    ) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surface, // Or a specific dark color from screenshot
            tonalElevation = AlertDialogDefaults.TonalElevation // Adjust elevation as needed
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Verification Result",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close Dialog")
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))

                DialogSectionTitle("Candidate Information")
                DetailRow("Name:", result.candidateName)
                DetailRow("ID:", result.candidateId)
                DetailRow("Email:", result.candidateEmail)
                Spacer(modifier = Modifier.height(16.dp))

                DialogSectionTitle("Credential Details")
                DetailRow("Title:", result.credentialTitle)
                DetailRow("Type:", result.credentialType)
                DetailRow("Institution:", result.credentialInstitution)
                DetailRow("Date Issued:", result.credentialDateIssued)
                DetailRow("Status:", result.credentialStatus)
                Spacer(modifier = Modifier.height(16.dp))

                DialogSectionTitle("Verification Details")
                DetailRow("Status:", result.verificationStatusDisplay)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Confidence:", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold, modifier = Modifier.width(100.dp))
                    Text(result.verificationConfidence, style = MaterialTheme.typography.bodyMedium)
                    // Potentially add a chip for "Authentic" or status here if needed
                }
                Spacer(modifier = Modifier.height(4.dp))
                 Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Blockchain Hash:", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold, modifier = Modifier.width(100.dp))
                    Text(result.blockchainHash, style = MaterialTheme.typography.bodyMedium, maxLines = 1, overflow = TextOverflow.Ellipsis)
                }
                // Consider adding the green "Authentic" chip similar to StatusChip here if desired
                // For example, by adapting StatusChip or creating a new specific one.
                // e.g. if result.verificationStatusDisplay == "Authentic"
                //      VerificationStatusChip(label = "Authentic", color = Color.Green)

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = { /* TODO: Handle Download Report */ },
                        modifier = Modifier.weight(1f).height(48.dp),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(Icons.Default.Download, contentDescription = "Download Report")
                        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                        Text("Download Report")
                    }
                    Button(
                        onClick = onApproveVisibility,
                        enabled = !result.recruiterApproved,
                        modifier = Modifier.weight(1f).height(48.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (result.recruiterApproved) Color.LightGray else MaterialTheme.colorScheme.primary // Disabled look
                        )
                    ) {
                        Icon(Icons.Default.VerifiedUser, contentDescription = "Approve Visibility")
                        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                        Text(if (result.recruiterApproved) "Approved" else "Approve Visibility")
                    }
                }
            }
        }
    }
}

@Composable
fun DialogSectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(bottom = 8.dp)
    )
}

@Composable
fun DetailRow(label: String, value: String) {
    Row(modifier = Modifier.padding(vertical = 2.dp)) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.width(110.dp) // Adjusted width for better spacing
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.weight(1f) // Allow value to take remaining space
        )
    }
}