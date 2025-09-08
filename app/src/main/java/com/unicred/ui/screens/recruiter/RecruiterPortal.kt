package com.unicred.ui.screens.recruiter

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.unicred.data.DashboardCredentialSummary
import com.unicred.data.DashboardMetrics
import com.unicred.data.User

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecruiterPortal(
    user: User,
    onLogout: () -> Unit
) {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                val recruiterNavItems = listOf(
                    Triple("recruiter_dashboard", Icons.Default.Dashboard, "Dashboard"),
                    Triple("recruiter_credential_verification", Icons.Default.VerifiedUser, "Verification"),
                    Triple("recruiter_profile", Icons.Default.Settings, "Settings") // Icon changed to Settings
                )

                recruiterNavItems.forEach { (route, icon, label) ->
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
            startDestination = "recruiter_dashboard",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("recruiter_dashboard") {
                RecruiterDashboardScreen(user = user)
            }
            composable("recruiter_credential_verification") {
                CredentialVerificationScreen(user = user)
            }
            composable("recruiter_profile") {
                RecruiterProfileScreen(user = user, onLogout = onLogout)
            }
        }
    }
}

@Composable
fun RecruiterDashboardScreen(user: User) {
    val mockMetrics = remember {
        DashboardMetrics(
            totalStudents = 150,
            totalCredentials = 450,
            verifiedCount = 300,
            pendingCount = 100,
            recentCredentials = listOf(
                DashboardCredentialSummary("cred_001", "Bachelor of Engineering", "Tech University", "stu_123", "Alice Wonderland", "verified", "2024-07-15T10:00:00.000Z"),
                DashboardCredentialSummary("cred_002", "Master of Business", "Commerce College", "stu_124", "Bob The Builder", "pending", "2024-07-14T11:30:00.000Z"),
                DashboardCredentialSummary("cred_003", "Certificate in Cloud Computing", "Online Institute", "stu_125", "Carol Danvers", "verified", "2024-07-13T09:15:00.000Z"),
                DashboardCredentialSummary("cred_004", "Diploma in Digital Marketing", "Marketing School", "stu_126", "David Copperfield", "verified", "2024-07-12T16:45:00.000Z"),
                DashboardCredentialSummary("cred_005", "PhD in Physics", "Science University", "stu_127", "Eve Harrington", "pending", "2024-07-11T14:00:00.000Z")
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
                text = "Here's a summary of the platform:",
                style = MaterialTheme.typography.titleMedium
            )
        }
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                StatCard("Total Students", mockMetrics.totalStudents.toString(), Icons.Default.People, Modifier.weight(1f))
                StatCard("Total Credentials", mockMetrics.totalCredentials.toString(), Icons.Default.School, Modifier.weight(1f))
            }
        }
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                StatCard("Verified Credentials", mockMetrics.verifiedCount.toString(), Icons.Default.CheckCircle, Modifier.weight(1f), MaterialTheme.colorScheme.primaryContainer)
                StatCard("Pending Credentials", mockMetrics.pendingCount.toString(), Icons.Default.HourglassEmpty, Modifier.weight(1f), MaterialTheme.colorScheme.secondaryContainer)
            }
        }
        item {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Recent Credentials",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }
        items(mockMetrics.recentCredentials) { credential ->
            RecentCredentialCard(credential = credential)
        }
    }
}

@Composable
fun StatCard(
    title: String,
    value: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.surfaceVariant
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
            Text(text = title, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
fun RecentCredentialCard(credential: DashboardCredentialSummary) {
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
                style = MaterialTheme.typography.bodyMedium,
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
                    imageVector = if (credential.status.equals("verified", ignoreCase = true)) Icons.Filled.CheckCircle else Icons.Filled.Info,
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
fun RecruiterProfileScreen(user: User, onLogout: () -> Unit) {
    var pushNotificationsEnabled by remember { mutableStateOf(true) }
    var emailAlertsEnabled by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
    ) {
        item { // Added item for the main "Settings" title
            Text(
                text = "Settings",
                style = MaterialTheme.typography.headlineMedium, // Or headlineSmall, adjust as needed
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 20.dp, bottom = 12.dp) // Added padding
            )
        }
        item {
            SettingsHeader(user = user, onEditProfileClick = { /* TODO: Navigate to Edit Profile screen */ })
        }

        item { SettingsSectionTitle("Account") }
        item {
            SettingsNavigationItem(
                icon = Icons.Default.PersonOutline,
                title = "Edit Profile",
                subtitle = "Update your personal information",
                onClick = { /* TODO: Navigate to Edit Profile screen */ }
            )
        }
        item {
            SettingsNavigationItem(
                icon = Icons.Default.Key,
                title = "Change Password",
                subtitle = "Update your password",
                onClick = { /* TODO: Navigate to Change Password screen */ }
            )
        }
        item {
            SettingsNavigationItem(
                icon = Icons.Default.PrivacyTip,
                title = "Privacy Settings",
                subtitle = "Manage your privacy preferences",
                onClick = { /* TODO: Navigate to Privacy Settings screen */ }
            )
        }

        item { SettingsSectionTitle("Preferences") }
        item {
            SettingsToggleItem(
                icon = Icons.Default.Notifications,
                title = "Push Notifications",
                subtitle = "Receive notifications about verifications",
                checked = pushNotificationsEnabled,
                onCheckedChange = { pushNotificationsEnabled = it }
            )
        }
        item {
            SettingsToggleItem(
                icon = Icons.Default.Email,
                title = "Email Alerts",
                subtitle = "Receive email notifications",
                checked = emailAlertsEnabled,
                onCheckedChange = { emailAlertsEnabled = it }
            )
        }

        item { SettingsSectionTitle("Support") }
        item {
            SettingsNavigationItem(
                icon = Icons.Default.HelpOutline,
                title = "Help Center",
                subtitle = "Find answers to your questions",
                onClick = { /* TODO: Navigate to Help Center */ }
            )
        }
        item {
            SettingsNavigationItem(
                icon = Icons.Default.SupportAgent,
                title = "Contact Us",
                subtitle = "Get in touch with support",
                onClick = { /* TODO: Navigate to Contact Us */ }
            )
        }
        
        item {
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = onLogout,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 16.dp) // Added vertical padding for spacing
                    .height(50.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.errorContainer)
            ) {
                Icon(Icons.Default.Logout, contentDescription = "Logout", tint = MaterialTheme.colorScheme.onErrorContainer)
                Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                Text("Logout", color = MaterialTheme.colorScheme.onErrorContainer, fontSize = 16.sp)
            }
        }
    }
}

@Composable
fun SettingsHeader(user: User, onEditProfileClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 24.dp, bottom = 24.dp, start = 16.dp, end = 16.dp), // Adjusted top padding
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Surface(
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape),
            color = MaterialTheme.colorScheme.primaryContainer
        ) {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                Text(
                    text = user.username.firstOrNull()?.uppercaseChar()?.toString() ?: "R", // Default to R if username is empty
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    fontSize = 36.sp // Made initial larger
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = user.fullName ?: user.username,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = user.email ?: "recruit@example.com", // Placeholder email if not available
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = "Recruiter", // Static as per screenshot
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedButton(
            onClick = onEditProfileClick,
            shape = RoundedCornerShape(8.dp),
            // colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.primary)
        ) {
            Icon(Icons.Filled.Edit, contentDescription = "Edit Profile", modifier = Modifier.size(ButtonDefaults.IconSize))
            Spacer(Modifier.size(ButtonDefaults.IconSpacing))
            Text("Edit Profile")
        }
    }
}

@Composable
fun SettingsSectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium, // Made section title a bit larger
        fontWeight = FontWeight.SemiBold,       // Slightly less bold
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 20.dp, bottom = 10.dp), // Adjusted padding
        color = MaterialTheme.colorScheme.onSurface // Changed to onSurface for better contrast as per screenshot
    )
}

@Composable
fun SettingsNavigationItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Column(modifier = Modifier.clickable(onClick = onClick)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp), // Increased vertical padding
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                modifier = Modifier.size(24.dp),
                tint = MaterialTheme.colorScheme.primary // Icon color matched to screenshot
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(title, style = MaterialTheme.typography.bodyLarge)
                Text(subtitle, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Icon(
                imageVector = Icons.Default.ArrowForwardIos,
                contentDescription = "Navigate",
                modifier = Modifier.size(18.dp), // Slightly larger arrow
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Divider(modifier = Modifier.padding(start = 16.dp + 24.dp + 16.dp, end = 16.dp), color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)) // end padding for divider
    }
}

@Composable
fun SettingsToggleItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Column(modifier = Modifier.clickable { onCheckedChange(!checked) }) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp), // Increased vertical padding
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                modifier = Modifier.size(24.dp),
                tint = MaterialTheme.colorScheme.primary // Icon color matched to screenshot
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(title, style = MaterialTheme.typography.bodyLarge)
                Text(subtitle, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = MaterialTheme.colorScheme.primary,
                    checkedTrackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                    uncheckedThumbColor = MaterialTheme.colorScheme.outline,
                    uncheckedTrackColor = MaterialTheme.colorScheme.surfaceVariant
                )
            )
        }
        Divider(modifier = Modifier.padding(start = 16.dp + 24.dp + 16.dp, end = 16.dp), color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)) // end padding for divider
    }
}
