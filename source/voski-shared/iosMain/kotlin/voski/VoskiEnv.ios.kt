package voski

/**
 * iOS-specific implementation of VoskiEnv.
 * Provides configuration based on build variants and iOS-specific settings.
 */
actual object VoskiEnv {
    actual val config: VoskiConfig by lazy {
        // Use testnet config for debug builds, mainnet for release
        if (isDebug) {
            VoskiConfig.testnet().copy(
                // Override with local development settings if needed
                lspBaseUrl = "https://localhost:8080",
                backupMode = VoskiConfig.BackupMode.DevicePlusMirror
            )
        } else {
            VoskiConfig.mainnet()
        }
    }
    
    actual val isDebug: Boolean = platform.Foundation.NSBundle.mainBundle.objectForInfoDictionaryKey("DEBUG") != null
    
    actual val platform: String = "ios"
}