package com.voskiapp.ui.screens.market

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

data class MarketItem(
    val id: Int,
    val title: String,
    val price: String,
    val icon: ImageVector,
    val gradient: List<Color>
)

@Composable
fun MarketScreen() {
    val marketItems = listOf(
        MarketItem(1, "Gift Cards", "$10 - $100", Icons.Default.CardGiftcard, listOf(Color(0xFFFF6B9D), Color(0xFFC239B3))),
        MarketItem(2, "Vouchers", "$5 - $50", Icons.Default.LocalOffer, listOf(Color(0xFF4CAF50), Color(0xFF2E7D32))),
        MarketItem(3, "Deals", "Save up to 50%", Icons.Default.Discount, listOf(Color(0xFFFF9800), Color(0xFFF57C00))),
        MarketItem(4, "Rewards", "Earn Points", Icons.Default.Stars, listOf(Color(0xFFFDD835), Color(0xFFF57F17))),
        MarketItem(5, "Shopping", "Browse Store", Icons.Default.ShoppingCart, listOf(Color(0xFF2196F3), Color(0xFF1565C0))),
        MarketItem(6, "Services", "Premium", Icons.Default.RocketLaunch, listOf(Color(0xFF9C27B0), Color(0xFF6A1B9A))),
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        // Header
        Text(
            text = "Market",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        
        Text(
            text = "Explore deals and offers",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // Search Bar
        OutlinedTextField(
            value = "",
            onValueChange = {},
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            placeholder = { Text("Search for deals...") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFFFDD835),
                unfocusedBorderColor = MaterialTheme.colorScheme.outline
            )
        )

        // Market Items Grid
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(marketItems) { item ->
                MarketItemCard(item)
            }
        }
    }
}

@Composable
fun MarketItemCard(item: MarketItem) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(item.gradient)
                )
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Icon(
                    imageVector = item.icon,
                    contentDescription = item.title,
                    tint = Color.White,
                    modifier = Modifier.size(40.dp)
                )
                
                Column {
                    Text(
                        text = item.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = item.price,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.9f)
                    )
                }
            }
        }
    }
}

