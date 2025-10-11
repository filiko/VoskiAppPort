package voski.notify

/**
 * Cross-platform notification bridge for push-to-wake functionality.
 * 
 * This interface allows the shared business logic to handle push notifications
 * in a platform-agnostic way. Platform-specific implementations handle the
 * actual FCM/APNS integration while this provides the common interface.
 */
interface NotificationBridge {
    /**
     * Handle a wake signal from push notification.
     * 
     * This is called when the app receives a push notification indicating
     * that there's an incoming Lightning payment waiting to be processed.
     * The app should connect to the LSP and allow the payment to complete.
     * 
     * @param data Additional data from the push notification
     */
    suspend fun onWakeSignal(data: Map<String, String>)
    
    /**
     * Get the current push notification token for this device.
     * 
     * @return FCM token (Android) or APNS token (iOS), null if not available
     */
    suspend fun getPushToken(): String?
    
    /**
     * Register for push notifications and return the token.
     * 
     * @return Push token if registration successful, null otherwise
     */
    suspend fun registerForNotifications(): String?
}

/**
 * Default implementation of NotificationBridge for shared logic.
 * 
 * This handles the common wake-up flow that's the same across platforms:
 * 1. Parse the notification data
 * 2. Start Lightning services if needed
 * 3. Connect to the LSP
 * 4. Allow held HTLCs to complete
 */
class DefaultNotificationBridge(
    private val lightningService: LightningService
) : NotificationBridge {
    
    override suspend fun onWakeSignal(data: Map<String, String>) {
        try {
            println("[NotificationBridge] Received wake signal: $data")
            
            // Extract payment information from notification data
            val paymentHash = data["payment_hash"]
            val amount = data["amount_msat"]?.toLongOrNull()
            
            if (paymentHash == null) {
                println("[NotificationBridge] Wake signal missing payment_hash")
                return
            }
            
            println("[NotificationBridge] Waking up for payment: $paymentHash (${amount}msat)")
            
            // Start Lightning services if not already running
            if (!lightningService.isRunning()) {
                println("[NotificationBridge] Starting Lightning service...")
                lightningService.start()
            }
            
            // Connect to LSP to allow payment completion
            println("[NotificationBridge] Connecting to LSP...")
            val connected = lightningService.connectToLsp(timeoutSeconds = 10)
            
            if (connected) {
                println("[NotificationBridge] Successfully connected, payment should complete")
                // The LSP will now forward the held HTLC and the payment will settle
            } else {
                println("[NotificationBridge] Failed to connect to LSP")
            }
            
        } catch (e: Exception) {
            println("[NotificationBridge] Error handling wake signal: ${e.message}")
        }
    }
    
    override suspend fun getPushToken(): String? {
        // This will be implemented by platform-specific code
        return null
    }
    
    override suspend fun registerForNotifications(): String? {
        // This will be implemented by platform-specific code
        return null
    }
}

/**
 * Interface for Lightning service operations needed by the notification bridge.
 * 
 * This abstracts the Phoenix Lightning service so the notification bridge
 * can interact with it without tight coupling.
 */
interface LightningService {
    /**
     * Check if the Lightning service is currently running.
     */
    fun isRunning(): Boolean
    
    /**
     * Start the Lightning service.
     */
    suspend fun start()
    
    /**
     * Connect to the configured LSP node.
     * 
     * @param timeoutSeconds Maximum time to wait for connection
     * @return true if connection successful, false otherwise
     */
    suspend fun connectToLsp(timeoutSeconds: Int = 30): Boolean
    
    /**
     * Get the user's Lightning node public key.
     */
    fun getNodePubkey(): String?
}