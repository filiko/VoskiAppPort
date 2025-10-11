package voski

/**
 * Centralized configuration for all Voski-specific features and toggles.
 * This provides a single point of control for LSP integration, backup modes,
 * and other Voski-specific functionality.
 */
data class VoskiConfig(
    /** Base URL for the Voltage LSP backend service */
    val lspBaseUrl: String,
    
    /** Public key of the Voltage LSP node */
    val lspNodePubkey: String,
    
    /** URL for Rapid Gossip Sync server (null to disable RGS) */
    val rgsUrl: String?,
    
    /** Backup strategy for channel state and wallet data */
    val backupMode: BackupMode = BackupMode.DeviceOnly,
    
    /** Enable voucher redemption functionality */
    val enableVoucher: Boolean = true
) {
    enum class BackupMode {
        /** Store encrypted backups only on device */
        DeviceOnly,
        /** Store on device + mirror encrypted backups to backend */
        DevicePlusMirror
    }
    
    companion object {
        /** Default testnet configuration for development */
        fun testnet() = VoskiConfig(
            lspBaseUrl = "https://testnet-api.voski.com",
            lspNodePubkey = "03864ef025fde8fb587d989186ce6a4a186895ee44a926bfc370e2c366597a3f8f",
            rgsUrl = "https://rgs.testnet.voski.com/snapshot",
            backupMode = BackupMode.DevicePlusMirror,
            enableVoucher = true
        )
        
        /** Default mainnet configuration for production */
        fun mainnet() = VoskiConfig(
            lspBaseUrl = "https://api.voski.com",
            lspNodePubkey = "03864ef025fde8fb587d989186ce6a4a186895ee44a926bfc370e2c366597a3f8f",
            rgsUrl = "https://rgs.voski.com/snapshot",
            backupMode = BackupMode.DeviceOnly,
            enableVoucher = true
        )
    }
}

/**
 * Platform-specific environment and configuration.
 * Actual implementations provided by platform-specific modules.
 */
expect object VoskiEnv {
    /** Current configuration based on build variant and platform */
    val config: VoskiConfig
    
    /** Whether this is a debug/development build */
    val isDebug: Boolean
    
    /** Platform identifier (android/ios) */
    val platform: String
}