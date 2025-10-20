package com.voskiapp.ui.screens.getstarted

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.voskiapp.ui.components.ReusableButton
import com.voskiapp.ui.components.ReusableOutlinedButton

@Composable
fun GetStartedScreen(
    onNavigateToRememberMe: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // App Logo or Image placeholder
        Spacer(modifier = Modifier.weight(1f))
        
        Text(
            text = "Welcome to Voski",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = "Your digital wallet for all your needs",
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.weight(1f))
        
        ReusableButton(
            text = "Get Started",
            onClick = onNavigateToRememberMe,
            modifier = Modifier.padding(bottom = 12.dp)
        )
        
        ReusableOutlinedButton(
            text = "Learn More",
            onClick = { /* Handle learn more */ }
        )
        
        Spacer(modifier = Modifier.height(24.dp))
    }
}



