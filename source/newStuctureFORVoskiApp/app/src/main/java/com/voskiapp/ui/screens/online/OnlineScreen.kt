package com.voskiapp.ui.screens.online

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.voskiapp.ui.components.ReusableButton
import com.voskiapp.ui.components.ReusableCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnlineScreen(
    onNavigateBack: () -> Unit
) {
    var isOnline by remember { mutableStateOf(true) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Online Services") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Filled.ArrowBack, "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { isOnline = !isOnline }) {
                        Icon(Icons.Default.Refresh, "Refresh")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // Connection Status Card
            ReusableCard(
                title = if (isOnline) "Connected" else "Offline",
                subtitle = if (isOnline) "All systems operational" else "Check your connection",
                backgroundColor = if (isOnline) 
                    MaterialTheme.colorScheme.primaryContainer 
                else 
                    MaterialTheme.colorScheme.errorContainer
            ) {
                Icon(
                    imageVector = if (isOnline) Icons.Default.CloudDone else Icons.Default.CloudOff,
                    contentDescription = "Connection status",
                    tint = if (isOnline) 
                        MaterialTheme.colorScheme.primary 
                    else 
                        MaterialTheme.colorScheme.error
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "Available Services",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(onlineServices) { service ->
                    ReusableCard(
                        title = service.name,
                        subtitle = service.description,
                        onClick = { /* Navigate to service */ }
                    ) {
                        Text(
                            text = if (isOnline) "Available" else "Unavailable",
                            color = if (isOnline) 
                                MaterialTheme.colorScheme.primary 
                            else 
                                MaterialTheme.colorScheme.error,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
                
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    if (!isOnline) {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "You're currently offline",
                                fontSize = 16.sp,
                                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            ReusableButton(
                                text = "Retry Connection",
                                onClick = { isOnline = true }
                            )
                        }
                    }
                }
            }
        }
    }
}

data class OnlineService(
    val name: String,
    val description: String
)

val onlineServices = listOf(
    OnlineService("Money Transfer", "Send money to friends and family"),
    OnlineService("Bill Payment", "Pay your utility bills online"),
    OnlineService("Mobile Recharge", "Recharge your mobile phone"),
    OnlineService("Online Shopping", "Shop from thousands of stores"),
    OnlineService("Investments", "Invest in stocks and mutual funds")
)


