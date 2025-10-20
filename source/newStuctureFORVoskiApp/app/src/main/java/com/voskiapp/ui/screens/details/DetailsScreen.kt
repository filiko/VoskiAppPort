package com.voskiapp.ui.screens.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.voskiapp.ui.components.ReusableButton
import com.voskiapp.ui.components.ReusableCard
import com.voskiapp.ui.components.ReusableInfoCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen(
    onNavigateToOnline: () -> Unit,
    onNavigateBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Transaction Details") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Filled.ArrowBack, "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { /* Share */ }) {
                        Icon(Icons.Default.Share, "Share")
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
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Amount Card
            ReusableCard(
                title = "Payment Received",
                subtitle = "Completed",
                backgroundColor = MaterialTheme.colorScheme.primaryContainer
            ) {
                Text(
                    text = "$500.00",
                    fontSize = 48.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            
            Text(
                text = "Transaction Information",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 8.dp)
            )
            
            ReusableInfoCard(
                label = "Transaction ID",
                value = "TXN123456789",
                icon = {
                    Icon(Icons.Default.Receipt, "Receipt")
                }
            )
            
            ReusableInfoCard(
                label = "Date & Time",
                value = "October 15, 2025 at 2:30 PM",
                icon = {
                    Icon(Icons.Default.CalendarToday, "Calendar")
                }
            )
            
            ReusableInfoCard(
                label = "Category",
                value = "Income",
                icon = {
                    Icon(Icons.Default.Category, "Category")
                }
            )
            
            ReusableInfoCard(
                label = "From",
                value = "Client Payment",
                icon = {
                    Icon(Icons.Default.Description, "Description")
                }
            )
            
            ReusableInfoCard(
                label = "Payment Method",
                value = "Bank Transfer",
                icon = {
                    Icon(Icons.Default.LocationOn, "Location")
                }
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            ReusableButton(
                text = "View Receipt",
                onClick = onNavigateToOnline
            )
        }
    }
}


