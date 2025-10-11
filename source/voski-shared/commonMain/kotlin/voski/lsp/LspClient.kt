package voski.lsp

/**
 * Route hint information for private channel routing.
 * Used to help payers route payments through the LSP to reach the user's private channel.
 */
data class RouteHint(
    /** Public key of the LSP node */
    val pubkey: String,
    
    /** Short channel ID alias for the private channel */
    val scidAlias: String,
    
    /** Base fee in millisatoshis */
    val feeBaseMsat: Long,
    
    /** Proportional fee in parts per million */
    val feeProportional: Long,
    
    /** HTLC minimum in millisatoshis */
    val htlcMinimumMsat: Long = 1000,
    
    /** HTLC maximum in millisatoshis */
    val htlcMaximumMsat: Long = 4_294_967_295_000 // ~4.3M sats
)

/**
 * Result wrapper for LSP operations.
 * Provides consistent error handling across all LSP client methods.
 */
sealed class LspResult<out T> {
    data class Ok<T>(val value: T) : LspResult<T>()
    data class Err(val code: String, val message: String) : LspResult<Nothing>()
    
    inline fun <R> map(transform: (value: T) -> R): LspResult<R> = when (this) {
        is Ok -> Ok(transform(value))
        is Err -> this
    }
    
    inline fun onSuccess(action: (value: T) -> Unit): LspResult<T> = apply {
        if (this is Ok) action(value)
    }
    
    inline fun onError(action: (code: String, message: String) -> Unit): LspResult<T> = apply {
        if (this is Err) action(code, message)
    }
}

/**
 * Interface for Lightning Service Provider (LSP) client operations.
 * 
 * This provides the abstraction layer between the mobile wallet and the Voltage-backed
 * LSP node. The LSP handles channel management, liquidity provision, and routing assistance
 * while maintaining the user's self-custody of funds.
 */
interface LspClient {
    /**
     * Ensure the user has sufficient inbound liquidity for receiving payments.
     * 
     * This may trigger the LSP to:
     * - Open a new channel with inbound capacity
     * - Splice additional liquidity into an existing channel
     * - Return success if sufficient liquidity already exists
     * 
     * @param minSats Minimum inbound liquidity required in satoshis
     * @return Success if liquidity is available/created, error otherwise
     */
    suspend fun ensureInboundLiquidity(minSats: Long): LspResult<Unit>
    
    /**
     * Get route hints for the user's private channels.
     * 
     * Route hints are included in BOLT11 invoices to help payers route payments
     * through the LSP to reach the user's private channel. This is essential
     * for Phoenix-style wallets using private channels.
     * 
     * @return List of route hints for invoice generation
     */
    suspend fun getRouteHints(): LspResult<List<RouteHint>>
    
    /**
     * Request the LSP to open a new channel or splice existing channel.
     * 
     * This is used when the user needs more inbound capacity than what
     * ensureInboundLiquidity() can provide, or when opening the first channel.
     * 
     * @param requiredInboundSats Required inbound capacity in satoshis
     * @return Success when channel operation is initiated
     */
    suspend fun openOrSplice(requiredInboundSats: Long): LspResult<Unit>
    
    /**
     * Register the user's device for push notifications.
     * 
     * This enables the push-to-wake functionality where the LSP can notify
     * the mobile device when there's an incoming payment that needs to be
     * processed while the app is offline.
     * 
     * @param nodePubkey User's Lightning node public key
     * @param platform Platform identifier (android/ios)
     * @param pushToken FCM/APNS push notification token
     * @return Success when registration is complete
     */
    suspend fun registerDevice(
        nodePubkey: String,
        platform: String,
        pushToken: String
    ): LspResult<Unit>
    
    /**
     * Get the LSP node's public key and connection information.
     * 
     * @return LSP node information for peer connection
     */
    suspend fun getLspInfo(): LspResult<LspInfo>
}

/**
 * Information about the LSP node for establishing peer connections.
 */
data class LspInfo(
    /** LSP node public key */
    val pubkey: String,
    
    /** LSP node network addresses for connection */
    val addresses: List<String>,
    
    /** LSP node alias/name */
    val alias: String? = null
)