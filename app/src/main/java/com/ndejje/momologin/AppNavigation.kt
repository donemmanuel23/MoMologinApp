package com.ndejje.momologin

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.ndejje.momologin.view.HomeScreen
import com.ndejje.momologin.view.LoginScreen
import com.ndejje.momologin.view.RegisterScreen
import com.ndejje.momologin.viewmodel.AuthViewModel

object Routes {
  const val LOGIN    = "login"
  const val REGISTER = "register"
  // Use a placeholder {username} so the NavHost knows it's a variable
  const val HOME     = "home/{username}"
}

@Composable
fun AppNavigation(viewModel: AuthViewModel) {
  val navController = rememberNavController()

  NavHost(navController = navController, startDestination = Routes.LOGIN) {

    composable(Routes.LOGIN) {
      LoginScreen(
        viewModel = viewModel,
        onLoginSuccess = { username ->
          // Navigates to e.g., "home/JohnDoe"
          navController.navigate("home/$username") {
            popUpTo(Routes.LOGIN) { inclusive = true }
          }
        },
        onNavigateToRegister = {
          viewModel.resetState()
          navController.navigate(Routes.REGISTER)
        }
      )
    }

    composable(Routes.REGISTER) {
      RegisterScreen(
        viewModel = viewModel,
        onRegisterSuccess = { username ->
          navController.navigate("home/$username") {
            popUpTo(Routes.LOGIN) { inclusive = true }
          }
        },
        onNavigateToLogin = { navController.popBackStack() }
      )
    }

// Inside AppNavigation.kt
    composable(
      route = Routes.HOME, // This is "home/{username}"
      arguments = listOf(navArgument("username") { type = NavType.StringType })
    ) { backStackEntry ->
      // Extract the username safely
      val username = backStackEntry.arguments?.getString("username") ?: "Guest"
      HomeScreen(
        username = username,
        onLogout = {
          navController.navigate(Routes.LOGIN) {
            popUpTo(0) // Clear the whole backstack on logout
          }
        }
      )
    }

  }
  }
