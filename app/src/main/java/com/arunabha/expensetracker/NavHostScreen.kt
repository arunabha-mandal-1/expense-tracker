package com.arunabha.expensetracker

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun NavHostScreen(){
    val navController = rememberNavController()
    val context = LocalContext.current
    val dataStore = StoreUserInfo(context)

    // Change start destination according to preferences
    val startDest = dataStore.readRegistrationStatusAndGoToParticularScreen(context)
    NavHost(navController = navController, startDestination = startDest){
        composable(route = "/welcome"){
            WelcomeScreen(navController)
        }
        composable(route = "/home"){
            HomeScreen(navController)
        }
        composable(route = "/add"){
            AddTransaction(navController)
        }
        composable(route = "/allTransactions"){
            AllTransactionsScreen(navController)
        }
    }
}