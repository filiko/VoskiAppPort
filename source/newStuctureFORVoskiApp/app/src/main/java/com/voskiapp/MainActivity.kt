package com.voskiapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.voskiapp.ui.components.BottomNavigationBar
import com.voskiapp.ui.navigation.BottomNavItem
import com.voskiapp.ui.screens.card.CardScreen
import com.voskiapp.ui.screens.casestudy.CaseStudyScreen
import com.voskiapp.ui.screens.create.CreateScreen
import com.voskiapp.ui.screens.details.DetailsScreen
import com.voskiapp.ui.screens.getstarted.GetStartedScreen
import com.voskiapp.ui.screens.home.HomeScreen
import com.voskiapp.ui.screens.market.MarketScreen
import com.voskiapp.ui.screens.online.OnlineScreen
import com.voskiapp.ui.screens.rememberme.RememberMeScreen
import com.voskiapp.ui.screens.rewards.RewardsScreen
import com.voskiapp.ui.screens.settings.SettingsScreen
import com.voskiapp.ui.screens.wallet.WalletScreen
import com.voskiapp.ui.theme.VoskiAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            VoskiAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    VoskiApp()
                }
            }
        }
    }
}

@Composable
fun VoskiApp() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    
    // Define main bottom nav routes
    val bottomNavRoutes = listOf("wallet", "market", "home", "rewards", "settings")
    val showBottomBar = currentRoute in bottomNavRoutes
    
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            // Show bottom navigation for main tabs (test: always show)
            BottomNavigationBar(navController = navController)
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "getstarted",
            modifier = Modifier.padding(innerPadding)
        ) {
            // Onboarding screens (no bottom nav)
            composable("getstarted") {
                GetStartedScreen(
                    onNavigateToRememberMe = { navController.navigate("rememberme") }
                )
            }
            composable("rememberme") {
                RememberMeScreen(
                    onNavigateToWallet = { 
                        navController.navigate("home") {
                            popUpTo("getstarted") { inclusive = true }
                        }
                    },
                    onNavigateBack = { navController.popBackStack() }
                )
            }
            
            // Main bottom navigation tabs
            composable(BottomNavItem.Wallet.route) {
                WalletScreen(
                    onNavigateToCard = { navController.navigate("card") },
                    onNavigateToDetails = { navController.navigate("details") },
                    onNavigateBack = { }
                )
            }
            composable(BottomNavItem.Market.route) {
                MarketScreen()
            }
            composable(BottomNavItem.Home.route) {
                HomeScreen()
            }
            composable(BottomNavItem.Rewards.route) {
                RewardsScreen()
            }
            composable(BottomNavItem.Settings.route) {
                SettingsScreen()
            }
            
            // Secondary screens (accessed from bottom nav tabs)
            composable("card") {
                CardScreen(
                    onNavigateToCreate = { navController.navigate("create") },
                    onNavigateBack = { navController.popBackStack() }
                )
            }
            composable("create") {
                CreateScreen(
                    onNavigateBack = { navController.popBackStack() },
                    onSaveSuccess = { navController.popBackStack() }
                )
            }
            composable("details") {
                DetailsScreen(
                    onNavigateToOnline = { navController.navigate("online") },
                    onNavigateBack = { navController.popBackStack() }
                )
            }
            composable("online") {
                OnlineScreen(
                    onNavigateBack = { navController.popBackStack() }
                )
            }
            composable("casestudy") {
                CaseStudyScreen(
                    onNavigateBack = { navController.popBackStack() }
                )
            }
        }
    }
}



