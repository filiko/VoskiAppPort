package com.voskiapp.ui.screens.create

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
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Person
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.voskiapp.ui.components.ReusableButton
import com.voskiapp.ui.components.ReusableTextField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateScreen(
    onNavigateBack: () -> Unit,
    onSaveSuccess: () -> Unit
) {
    var cardNumber by remember { mutableStateOf("") }
    var cardHolder by remember { mutableStateOf("") }
    var expiryDate by remember { mutableStateOf("") }
    var cvv by remember { mutableStateOf("") }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add New Card") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Filled.ArrowBack, "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Card Information",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            
            Text(
                text = "Enter your card details to add a new payment method",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            ReusableTextField(
                value = cardNumber,
                onValueChange = { 
                    if (it.length <= 16) cardNumber = it 
                },
                label = "Card Number",
                placeholder = "1234 5678 9012 3456",
                leadingIcon = {
                    Icon(Icons.Default.CreditCard, contentDescription = "Card")
                },
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            )
            
            ReusableTextField(
                value = cardHolder,
                onValueChange = { cardHolder = it },
                label = "Card Holder Name",
                placeholder = "John Doe",
                leadingIcon = {
                    Icon(Icons.Default.Person, contentDescription = "Person")
                },
                imeAction = ImeAction.Next
            )
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                ReusableTextField(
                    value = expiryDate,
                    onValueChange = { 
                        if (it.length <= 5) expiryDate = it 
                    },
                    label = "Expiry Date",
                    placeholder = "MM/YY",
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next,
                    modifier = Modifier.weight(1f)
                )
                
                ReusableTextField(
                    value = cvv,
                    onValueChange = { 
                        if (it.length <= 4) cvv = it 
                    },
                    label = "CVV",
                    placeholder = "123",
                    keyboardType = KeyboardType.NumberPassword,
                    imeAction = ImeAction.Done,
                    modifier = Modifier.weight(1f)
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            ReusableButton(
                text = "Add Card",
                onClick = {
                    // Validate and save
                    if (cardNumber.isNotEmpty() && cardHolder.isNotEmpty() && 
                        expiryDate.isNotEmpty() && cvv.isNotEmpty()) {
                        onSaveSuccess()
                    }
                },
                enabled = cardNumber.isNotEmpty() && cardHolder.isNotEmpty() && 
                         expiryDate.isNotEmpty() && cvv.isNotEmpty()
            )
        }
    }
}


