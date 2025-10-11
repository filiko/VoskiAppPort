package com.voski.wallet.android.notify

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.launch
import voski.notify.NotificationBridge
import voski.notify.DefaultNotificationBridge

/**
 * Firebase Cloud Messaging service for push-to-wake functionality.
 * 
 * This service handles incoming push notifications from the Voski backend
 * when there are Lightning payments waiting to be processed while the app
 * is offline or in the background.
 */
class VoskiFcmService : FirebaseMessagingService() {
    
    private val notificationBridge: NotificationBridge by lazy {
        // TODO: Inject the actual Lightning service from Phoenix
        DefaultNotificationBridge(
            lightningService = AndroidLightningService()
        )
    }
    
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        
        println("[VoskiFcmService] Received FCM message: ${remoteMessage.data}")
        
        // Check if this is a wake signal for Lightning payment
        val messageType = remoteMessage.data["type"]
        if (messageType == "lightning_wake") {
            handleLightningWake(remoteMessage.data)
        } else {
            println("[VoskiFcmService] Unknown message type: $messageType")
        }
    }
    
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        
        println("[VoskiFcmService] New FCM token received: $token")
        
        // TODO: Register the new token with the Voski backend
        // This should call the LSP client's registerDevice method
    }
    
    private fun handleLightningWake(data: Map<String, String>) {
        try {
            // Launch coroutine to handle wake signal asynchronously
            kotlinx.coroutines.CoroutineScope(kotlinx.coroutines.Dispatchers.IO).launch {
                notificationBridge.onWakeSignal(data)
            }
        } catch (e: Exception) {
            println("[VoskiFcmService] Error handling Lightning wake: ${e.message}")
        }
    }
}

/**
 * Android-specific implementation of LightningService.
 * 
 * This bridges the notification system with Phoenix's Android Lightning service.
 */
private class AndroidLightningService : voski.notify.LightningService {
    
    override fun isRunning(): Boolean {
        // TODO: Check if Phoenix's NodeService is running
        // This should integrate with the existing Phoenix Android service architecture
        return false
    }
    
    override suspend fun start() {
        // TODO: Start Phoenix's Lightning service
        // This should trigger the NodeService to start if not already running
        println("[AndroidLightningService] Starting Lightning service...")
    }
    
    override suspend fun connectToLsp(timeoutSeconds: Int): Boolean {
        // TODO: Connect to the LSP node using Phoenix's peer management
        // This should establish a connection to the Voltage LSP node
        println("[AndroidLightningService] Connecting to LSP...")
        return true // Placeholder
    }
    
    override fun getNodePubkey(): String? {
        // TODO: Get the node pubkey from Phoenix's Lightning service
        return null
    }
}