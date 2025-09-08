package com.unicred.ui.screens.university

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Info // For About item
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
import com.unicred.data.User

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UniversitySettings(
    user: User,
    onLogout: () -> Unit
) {
    var showLogoutDialog by remember { mutableStateOf(false) }
    var pushNotificationsEnabled by remember { mutableStateOf(true) } // Example state
    var emailAlertsEnabled by remember { mutableStateOf(false) }    // Example state

    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        item { // Main "Settings" title
            Text(
                text = "Settings",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 20.dp, bottom = 12.dp)
            )
        }

        item {
            UniversitySettingsHeader(user = user, onEditProfileClick = { /* TODO: Navigate to Edit University Profile */ })
        }

        item { SettingsSectionTitleInternal("Account") }
        item {
            SettingsNavigationItemInternal(
                icon = Icons.Default.Business, 
                title = "Edit University Profile",
                subtitle = "Update information and branding",
                onClick = { /* TODO */ }
            )
        }
        item {
            SettingsNavigationItemInternal(
                icon = Icons.Default.Key,
                title = "Change Password",
                subtitle = "Update your login password",
                onClick = { /* TODO */ }
            )
        }
         item {
            SettingsNavigationItemInternal(
                icon = Icons.Default.VerifiedUser, 
                title = "Verification Settings",
                subtitle = "Manage credential verification parameters",
                onClick = { /* TODO */ }
            )
        }

        item { SettingsSectionTitleInternal("Management") }
        item {
            SettingsNavigationItemInternal(
                icon = Icons.Default.School,
                title = "Academic Programs",
                subtitle = "Manage degree programs and courses",
                onClick = { /* TODO */ }
            )
        }
        item {
            SettingsNavigationItemInternal(
                icon = Icons.Default.Group,
                title = "Faculty & Staff",
                subtitle = "Manage faculty and administrator accounts",
                onClick = { /* TODO */ }
            )
        }
         item {
            SettingsNavigationItemInternal(
                icon = Icons.Default.PeopleOutline,
                title = "Student Records", 
                subtitle = "View and manage student data",
                onClick = { /* TODO */ }
            )
        }
        item {
            SettingsNavigationItemInternal(
                icon = Icons.Default.Policy, 
                title = "Blockchain & Issuance",
                subtitle = "Configure integration and view logs",
                onClick = { /* TODO */ }
            )
        }
        item {
            SettingsNavigationItemInternal(
                icon = Icons.Default.Assessment, 
                title = "Reports & Analytics",
                subtitle = "Access credential statistics and insights",
                onClick = { /* TODO */ }
            )
        }

        item { SettingsSectionTitleInternal("Preferences") }
        item {
            SettingsToggleItemInternal(
                icon = Icons.Default.Notifications,
                title = "Push Notifications",
                subtitle = "Receive system and activity notifications",
                checked = pushNotificationsEnabled,
                onCheckedChange = { pushNotificationsEnabled = it }
            )
        }
        item {
            SettingsToggleItemInternal(
                icon = Icons.Default.Email,
                title = "Email Alerts",
                subtitle = "Receive important email notifications",
                checked = emailAlertsEnabled,
                onCheckedChange = { emailAlertsEnabled = it }
            )
        }
        
        item { SettingsSectionTitleInternal("Support") }
         item {
            SettingsNavigationItemInternal(
                icon = Icons.Default.HelpOutline,
                title = "Help Center",
                subtitle = "Find answers to your questions",
                onClick = { /* TODO: Navigate to Help Center */ }
            )
        }
        item {
            SettingsNavigationItemInternal(
                icon = Icons.Default.SupportAgent,
                title = "Contact Support",
                subtitle = "Get assistance with your account",
                onClick = { /* TODO */ }
            )
        }
        item {
            SettingsNavigationItemInternal(
                icon = Icons.Outlined.Info,
                title = "About UniCred",
                subtitle = "App version and information",
                onClick = { /* TODO: Show About Dialog or Screen */ }
            )
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = { showLogoutDialog = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 16.dp)
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

    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text("Logout", fontWeight = FontWeight.Bold) },
            text = { Text("Are you sure you want to logout?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showLogoutDialog = false
                        onLogout()
                    }
                ) {
                    Text("Logout")
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun UniversitySettingsHeader(user: User, onEditProfileClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 24.dp, bottom = 24.dp, start = 16.dp, end = 16.dp),
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
                    text = user.fullName?.firstOrNull()?.uppercaseChar()?.toString() ?: "U",
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    fontSize = 36.sp
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = user.fullName ?: user.username, // University Name
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = user.email ?: "university@example.edu", // Placeholder email
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = "University Administrator", // Role
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedButton(
            onClick = onEditProfileClick,
            shape = RoundedCornerShape(8.dp)
        ) {
            Icon(Icons.Filled.Edit, contentDescription = "Edit University Profile", modifier = Modifier.size(ButtonDefaults.IconSize))
            Spacer(Modifier.size(ButtonDefaults.IconSpacing))
            Text("Edit Profile")
        }
    }
}

@Composable
fun SettingsSectionTitleInternal(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.SemiBold,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 20.dp, bottom = 10.dp),
        color = MaterialTheme.colorScheme.onSurface
    )
}

@Composable
fun SettingsNavigationItemInternal(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Column(modifier = Modifier.clickable(onClick = onClick)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                modifier = Modifier.size(24.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(title, style = MaterialTheme.typography.bodyLarge)
                Text(subtitle, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Icon(
                imageVector = Icons.Default.ArrowForwardIos, 
                contentDescription = "Navigate",
                modifier = Modifier.size(18.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Divider(modifier = Modifier.padding(start = 16.dp + 24.dp + 16.dp, end = 16.dp), color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))
    }
}

@Composable
fun SettingsToggleItemInternal(
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
                .padding(horizontal = 16.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                modifier = Modifier.size(24.dp),
                tint = MaterialTheme.colorScheme.primary
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
        Divider(modifier = Modifier.padding(start = 16.dp + 24.dp + 16.dp, end = 16.dp), color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))
    }
}
