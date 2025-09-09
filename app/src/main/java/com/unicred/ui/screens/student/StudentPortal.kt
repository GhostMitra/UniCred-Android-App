package com.unicred.ui.screens.student

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
// import androidx.compose.ui.graphics.vector.ImageVector // Not directly used, Icons.Default.* are sufficient
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.unicred.R
import com.unicred.data.User

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentPortal(
    user: User, // This user parameter is for StudentSettingsScreen
    onLogout: () -> Unit
) {
    val navController = rememberNavController()
    
    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                
                val navItems = listOf(
                    Triple("dashboard", Icons.Default.Home, stringResource(R.string.dashboard)),
                    Triple("credentials", Icons.Default.Description, stringResource(R.string.credentials)),
                    Triple("settings", Icons.Default.Settings, stringResource(R.string.settings)) // Changed to stringResource
                )

                navItems.forEach { (route, icon, label) ->
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
            startDestination = "dashboard",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("dashboard") {
                StudentDashboard()
            }
            composable("credentials") {
                StudentCredentials()
            }
            composable("settings") { 
                StudentSettingsScreen(user = user, onLogout = onLogout) // Changed to StudentSettingsScreen
            }
        }
    }
}