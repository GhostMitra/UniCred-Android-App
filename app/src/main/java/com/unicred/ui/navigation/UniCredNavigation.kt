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
            when (currentUser.accessType) { // Smart cast is now possible
                com.unicred.data.AccessType.STUDENT -> "student_portal"
                com.unicred.data.AccessType.RECRUITER -> "recruiter_portal"
                com.unicred.data.AccessType.UNIVERSITY -> "university_portal"
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
                    when (user.accessType) {
                        com.unicred.data.AccessType.STUDENT -> navController.navigate("student_portal")
                        com.unicred.data.AccessType.RECRUITER -> navController.navigate("recruiter_portal")
                        com.unicred.data.AccessType.UNIVERSITY -> navController.navigate("university_portal")
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
                    navController.popBackStack()
                },
                onBackToLogin = {
                    navController.popBackStack()
                }
            )
        }

        composable("student_portal") {
            val userForPortal = authState.user
            if (userForPortal != null) {
                StudentPortal(
                    user = userForPortal,
                    onLogout = {
                        authViewModel.logout()
                        navController.navigate("login") {
                            popUpTo("login") { inclusive = true }
                        }
                    }
                )
            }
        }

        composable("recruiter_portal") {
            val userForPortal = authState.user
            if (userForPortal != null) {
                RecruiterPortal(
                    user = userForPortal,
                    onLogout = {
                        authViewModel.logout()
                        navController.navigate("login") {
                            popUpTo("login") { inclusive = true }
                        }
                    }
                )
            }
        }

        composable("university_portal") {
            val userForPortal = authState.user
            if (userForPortal != null) {
                UniversityPortal(
                    user = userForPortal,
                    onLogout = {
                        authViewModel.logout()
                        navController.navigate("login") {
                            popUpTo("login") { inclusive = true }
                        }
                    }
                )
            }
        }
    }
}
