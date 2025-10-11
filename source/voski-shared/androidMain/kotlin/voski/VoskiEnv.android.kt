package voski

// BuildConfig will be available at runtime from Android app module

/**
 * Android-specific implementation of VoskiEnv.
 * Provides configuration based on build variants and Android-specific settings.
 */
actual object VoskiEnv {
    actual val config: VoskiConfig by lazy {
        // Use testnet config for debug builds, mainnet for release
        if (isDebug) {
            VoskiConfig.testnet().copy(
                // Override with local development settings if needed
                lspBaseUrl = "http://localhost:3001",
                backupMode = VoskiConfig.BackupMode.DevicePlusMirror
            )
        } else {
            VoskiConfig.mainnet()
        }
    }
    
    actual val isDebug: Boolean = true // TODO: Get from BuildConfig when available
    
    actual val platform: String = "android"
}