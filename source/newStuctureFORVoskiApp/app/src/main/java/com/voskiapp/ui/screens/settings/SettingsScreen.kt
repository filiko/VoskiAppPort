package com.voskiapp.ui.screens.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

data class SettingsItem(
    val title: String,
    val subtitle: String,
    val icon: ImageVector,
    val hasSwitch: Boolean = false,
    val hasArrow: Boolean = true
)

data class SettingsSection(
    val title: String,
    val items: List<SettingsItem>
)

@Composable
fun SettingsScreen() {
    var notificationsEnabled by remember { mutableStateOf(true) }
    var biometricEnabled by remember { mutableStateOf(false) }
    var darkModeEnabled by remember { mutableStateOf(false) }

    val settingsSections = listOf(
        SettingsSection(
            title = "Account",
            items = listOf(
                SettingsItem("Profile", "Edit your personal information", Icons.Default.Person),
                SettingsItem("Security", "Password and authentication", Icons.Default.Security),
                SettingsItem("Privacy", "Control your privacy settings", Icons.Default.PrivacyTip)
            )
        ),
        SettingsSection(
            title = "Preferences",
            items = listOf(
                SettingsItem("Notifications", "Push notifications", Icons.Default.Notifications, hasSwitch = true),
                SettingsItem("Biometric Login", "Use fingerprint or face", Icons.Default.Fingerprint, hasSwitch = true),
                SettingsItem("Dark Mode", "Switch theme", Icons.Default.DarkMode, hasSwitch = true)
            )
        ),
        SettingsSection(
            title = "Support",
            items = listOf(
                SettingsItem("Help Center", "Get help and support", Icons.Default.Help),
                SettingsItem("Contact Us", "Send us a message", Icons.Default.Email),
                SettingsItem("About", "App version and info", Icons.Default.Info)
            )
        ),
        SettingsSection(
            title = "Legal",
            items = listOf(
                SettingsItem("Terms of Service", "Read our terms", Icons.Default.Description),
                SettingsItem("Privacy Policy", "Read our privacy policy", Icons.Default.Policy)
            )
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Header with Profile
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF6366F1))
                .padding(24.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Profile",
                        tint = Color.White,
                        modifier = Modifier.size(50.dp)
                    )
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Text(
                    text = "John Doe",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                
                Text(
                    text = "john.doe@example.com",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.8f)
                )
            }
        }

        // Settings List
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            settingsSections.forEach { section ->
                item {
                    SettingsSectionContent(
                        section = section,
                        notificationsEnabled = notificationsEnabled,
                        onNotificationsChange = { notificationsEnabled = it },
                        biometricEnabled = biometricEnabled,
                        onBiometricChange = { biometricEnabled = it },
                        darkModeEnabled = darkModeEnabled,
                        onDarkModeChange = { darkModeEnabled = it }
                    )
                }
            }
            
            // Logout Button
            item {
                Button(
                    onClick = { /* Handle logout */ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFF44336)
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Logout,
                        contentDescription = "Logout",
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Logout",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Text(
                    text = "Version 1.0.0",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun SettingsSectionContent(
    section: SettingsSection,
    notificationsEnabled: Boolean,
    onNotificationsChange: (Boolean) -> Unit,
    biometricEnabled: Boolean,
    onBiometricChange: (Boolean) -> Unit,
    darkModeEnabled: Boolean,
    onDarkModeChange: (Boolean) -> Unit
) {
    Column(
        modifier = Modifier.padding(horizontal = 24.dp)
    ) {
        Text(
            text = section.title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(bottom = 12.dp)
        )
        
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column {
                section.items.forEachIndexed { index, item ->
                    SettingsItemRow(
                        item = item,
                        checked = when (item.title) {
                            "Notifications" -> notificationsEnabled
                            "Biometric Login" -> biometricEnabled
                            "Dark Mode" -> darkModeEnabled
                            else -> false
                        },
                        onCheckedChange = { checked ->
                            when (item.title) {
                                "Notifications" -> onNotificationsChange(checked)
                                "Biometric Login" -> onBiometricChange(checked)
                                "Dark Mode" -> onDarkModeChange(checked)
                            }
                        }
                    )
                    
                    if (index < section.items.size - 1) {
                        Divider(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SettingsItemRow(
    item: SettingsItem,
    checked: Boolean = false,
    onCheckedChange: (Boolean) -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { if (!item.hasSwitch) { /* Handle click */ } }
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFF6366F1).copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = item.icon,
                    contentDescription = null,
                    tint = Color(0xFF6366F1),
                    modifier = Modifier.size(24.dp)
                )
            }
            
            Column {
                Text(
                    text = item.title,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = item.subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
        }
        
        when {
            item.hasSwitch -> {
                Switch(
                    checked = checked,
                    onCheckedChange = onCheckedChange,
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = Color(0xFF6366F1)
                    )
                )
            }
            item.hasArrow -> {
                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = "Navigate",
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                )
            }
        }
    }
}

