package com.unicred.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.unicred.ui.screens.auth.LoginScreen
import com.unicred.ui.screens.auth.SignupScreen
import com.unicred.ui.screens.recruiter.RecruiterPortal
import com.unicred.ui.screens.student.StudentPortal
import com.unicred.ui.screens.university.UniversityPortal
import com.unicred.ui.viewmodel.AuthViewModel
import com.unicred.data.AccessType // Import AccessType if not already

@Composable
fun UniCredNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val authState by authViewModel.authState.collectAsStateWithLifecycle()
    val currentUser = authState.user // Capture the user in a local variable

    NavHost(
        navController = navController,
        startDestination = if (currentUser != null) { // Use the local variable
            when (currentUser.role) { // Changed from accessType to role
                AccessType.STUDENT -> "student_portal"
                AccessType.RECRUITER -> "recruiter_portal"
                AccessType.UNIVERSITY -> "university_portal"
                AccessType.ADMIN -> "admin_portal" // Assuming you might have an admin portal
                else -> "login" // Fallback for any other unhandled or null roles
            }
        } else {
            "login"
        },
        modifier = modifier
    ) {
        composable("login") {
            LoginScreen(
                onLoginSuccess = { user ->
                    authViewModel.setUser(user)
                    // Navigate based on user.role after login
                    when (user.role) {
                        AccessType.STUDENT -> navController.navigate("student_portal") { popUpTo("login") { inclusive = true } }
                        AccessType.RECRUITER -> navController.navigate("recruiter_portal") { popUpTo("login") { inclusive = true } }
                        AccessType.UNIVERSITY -> navController.navigate("university_portal") { popUpTo("login") { inclusive = true } }
                        AccessType.ADMIN -> navController.navigate("admin_portal") { popUpTo("login") { inclusive = true } }
                        else -> navController.navigate("login") { popUpTo("login") { inclusive = true } } // Fallback
                    }
                },
                onNavigateToSignup = {
                    navController.navigate("signup")
                }
            )
        }

        composable("signup") {
            SignupScreen(
                onSignupSuccess = {
                    // Navigate to login after successful signup
                    navController.navigate("login") {
                        popUpTo("login") { inclusive = true } // Clear back stack to login
                    }
                },
                onBackToLogin = {
                    navController.popBackStack()
                }
            )
        }

        composable("student_portal") {
            val userForPortal = authState.user
            if (userForPortal != null && userForPortal.role == AccessType.STUDENT) {
                StudentPortal(
                    user = userForPortal,
                    onLogout = {
                        authViewModel.logout()
                        navController.navigate("login") {
                            popUpTo("login") { inclusive = true }
                        }
                    }
                )
            } else {
                // If user is null or not a student, redirect to login
                navController.navigate("login") { popUpTo(navController.graph.id) { inclusive = true } }
            }
        }

        composable("recruiter_portal") {
            val userForPortal = authState.user
            if (userForPortal != null && userForPortal.role == AccessType.RECRUITER) {
                RecruiterPortal(
                    user = userForPortal,
                    onLogout = {
                        authViewModel.logout()
                        navController.navigate("login") {
                            popUpTo("login") { inclusive = true }
                        }
                    }
                )
            } else {
                 navController.navigate("login") { popUpTo(navController.graph.id) { inclusive = true } }
            }
        }

        composable("university_portal") {
            val userForPortal = authState.user
            if (userForPortal != null && userForPortal.role == AccessType.UNIVERSITY) {
                UniversityPortal(
                    user = userForPortal,
                    onLogout = {
                        authViewModel.logout()
                        navController.navigate("login") {
                            popUpTo("login") { inclusive = true }
                        }
                    }
                )
            } else {
                navController.navigate("login") { popUpTo(navController.graph.id) { inclusive = true } }
            }
        }
        
        // Example for admin_portal, create the actual Composable if needed
        composable("admin_portal") {
            val userForPortal = authState.user
            if (userForPortal != null && userForPortal.role == AccessType.ADMIN) {
                // AdminPortal(...)
                // For now, redirect to login if AdminPortal Composable doesn't exist
                 navController.navigate("login") { popUpTo(navController.graph.id) { inclusive = true } }
            } else {
                navController.navigate("login") { popUpTo(navController.graph.id) { inclusive = true } }
            }
        }
    }
}
