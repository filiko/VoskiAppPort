package com.voskiapp.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object Wallet : BottomNavItem(
        route = "wallet",
        title = "Wallet",
        icon = Icons.Default.AccountBalanceWallet
    )
    
    object Market : BottomNavItem(
        route = "market",
        title = "Market",
        icon = Icons.Default.CardGiftcard
    )
    
    object Home : BottomNavItem(
        route = "home",
        title = "Home",
        icon = Icons.Default.Home
    )
    
    object Rewards : BottomNavItem(
        route = "rewards",
        title = "Rewards",
        icon = Icons.Default.Receipt
    )
    
    object Settings : BottomNavItem(
        route = "settings",
        title = "Settings",
        icon = Icons.Default.Person
    )

    companion object {
        val items = listOf(Wallet, Market, Home, Rewards, Settings)
    }
}

