package com.unicred.ui.screens.student

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.* // Changed to wildcard import for outlined icons
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
// import androidx.compose.ui.graphics.Color // Not directly used
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.unicred.data.User

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentSettingsScreen(
    user: User,
    onLogout: () -> Unit
) {
    var showLogoutDialog by remember { mutableStateOf(false) }
    var pushNotificationsEnabled by remember { mutableStateOf(true) } 
    var emailAlertsEnabled by remember { mutableStateOf(false) }    

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 16.dp) // Add overall vertical padding
    ) {
        item { 
            Text(
                text = "Settings",
                style = MaterialTheme.typography.headlineLarge, // Slightly larger title
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 16.dp) // Adjusted padding
            )
        }

        item {
            StudentSettingsHeader(user = user, onEditProfileClick = { /* TODO: Navigate to Edit Student Profile */ })
        }

        item { SettingsSectionTitleInternal("Account") }
        item {
            SettingsNavigationItemInternal(
                icon = Icons.Default.PersonOutline,
                title = "Edit Profile",
                subtitle = "Update your personal information",
                onClick = { /* TODO: Navigate to Edit Student Profile */ }
            )
        }
        item {
            SettingsNavigationItemInternal(
                icon = Icons.Default.Key,
                title = "Change Password",
                subtitle = "Update your login password",
                onClick = { /* TODO: Navigate to Change Password screen */ }
            )
        }
        item {
            SettingsNavigationItemInternal(
                icon = Icons.Default.PrivacyTip,
                title = "Privacy Settings",
                subtitle = "Manage your privacy preferences",
                onClick = { /* TODO: Navigate to Privacy Settings screen */ }
            )
        }

        item { SettingsSectionTitleInternal("Preferences") }
        item {
            SettingsToggleItemInternal(
                icon = Icons.Outlined.Notifications, // Corrected to Icons.Outlined
                title = "Push Notifications",
                subtitle = "Receive updates and alerts",
                checked = pushNotificationsEnabled,
                onCheckedChange = { pushNotificationsEnabled = it }
            )
        }
        item {
            SettingsToggleItemInternal(
                icon = Icons.Outlined.Email, // Corrected to Icons.Outlined
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
                title = "Contact Us",
                subtitle = "Get in touch with support",
                onClick = { /* TODO: Navigate to Contact Us */ }
            )
        }
        item {
            SettingsNavigationItemInternal(
                icon = Icons.Outlined.Info,
                title = "About",
                subtitle = "App version and information",
                onClick = { /* TODO: Show About Dialog or Screen */ }
            )
        }

        item {
            Spacer(modifier = Modifier.height(32.dp)) // Increased spacer for logout button
            Button(
                onClick = { showLogoutDialog = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp) // Consistent horizontal padding
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp), // Slightly more rounded
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.errorContainer)
            ) {
                Icon(Icons.Default.Logout, contentDescription = "Logout", tint = MaterialTheme.colorScheme.onErrorContainer)
                Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                Text("Logout", color = MaterialTheme.colorScheme.onErrorContainer, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
            }
            Spacer(modifier = Modifier.height(16.dp)) // Padding at the bottom
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
                    },
                    colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)
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
fun StudentSettingsHeader(user: User, onEditProfileClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp, bottom = 24.dp, start = 16.dp, end = 16.dp), // Adjusted padding
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Surface(
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape),
            color = MaterialTheme.colorScheme.primaryContainer,
            tonalElevation = 4.dp
        ) {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                Text(
                    text = user.username.firstOrNull()?.uppercaseChar()?.toString() ?: "S",
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    fontSize = 36.sp
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
            text = user.email ?: "student@example.com", 
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        user.studentId?.let {
            Text(
                text = "Student ID: $it",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
        OutlinedButton(
            onClick = onEditProfileClick,
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(Icons.Filled.Edit, contentDescription = "Edit Profile", modifier = Modifier.size(ButtonDefaults.IconSize))
            Spacer(Modifier.size(ButtonDefaults.IconSpacing))
            Text("Edit Profile")
        }
    }
}

@Composable
fun SettingsSectionTitleInternal(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleSmall, // Refined style for section title
        fontWeight = FontWeight.SemiBold,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 24.dp, bottom = 12.dp),
        color = MaterialTheme.colorScheme.primary // Use primary color for emphasis
    )
}

@Composable
fun SettingsNavigationItemInternal(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .clickable(onClick = onClick)
            .fillMaxWidth()
            .padding(horizontal = 16.dp) // Horizontal padding for the whole item
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp), // Vertical padding for content
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                modifier = Modifier.size(24.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant // Muted icon color
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(title, style = MaterialTheme.typography.bodyLarge)
                Text(subtitle, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos, // RTL support
                contentDescription = "Navigate",
                modifier = Modifier.size(18.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
            )
        }
        Divider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f), thickness = 0.5.dp) // Subtle divider
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
    Column(
        modifier = Modifier
            .clickable { onCheckedChange(!checked) }
            .fillMaxWidth()
            .padding(horizontal = 16.dp) // Horizontal padding for the whole item
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp), // Vertical padding for content
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                modifier = Modifier.size(24.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant // Muted icon color
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
                    checkedTrackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
                    uncheckedThumbColor = MaterialTheme.colorScheme.outline,
                    uncheckedTrackColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                )
            )
        }
        Divider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f), thickness = 0.5.dp) // Subtle divider
    }
}
