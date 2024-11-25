package com.arunabha.expensetracker

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.arunabha.expensetracker.data.model.TransactionEntity
import kotlinx.serialization.json.Json

@Composable
fun NavHostScreen() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val dataStore = StoreUserInfo(context)

    // Change start destination according to preferences
    val startDest = dataStore.readRegistrationStatusAndGoToParticularScreen(context)
    NavHost(navController = navController, startDestination = startDest) {
        composable(route = "/welcome") {
            WelcomeScreen(navController)
        }
        composable(route = "/home") {
            HomeScreen(navController)
        }
        composable(
            route = "/add/{transactionJson}",
            arguments = listOf(navArgument("transactionJson") {
                type = NavType.StringType;
                nullable = true
            })
        ) { backStackEntry ->
            val transactionJson = backStackEntry.arguments?.getString("transactionJson")
            // pass transaction model : checked omk
            val transaction = transactionJson?.let { Json.decodeFromString<TransactionEntity>(it) }
            AddTransaction(navController, transaction)
        }
        composable(route = "/allTransactions") {
            AllTransactionsScreen(navController)
        }
    }
}