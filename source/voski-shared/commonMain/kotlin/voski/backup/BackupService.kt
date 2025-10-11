package voski.backup

import voski.VoskiEnv
import voski.VoskiConfig

/**
 * Encrypted backup service for Lightning channel state and wallet data.
 * 
 * This service implements the device-first backup strategy where:
 * 1. All channel state is encrypted using keys derived from the user's seed
 * 2. Encrypted backups are stored locally on device storage
 * 3. Optionally, encrypted backups are mirrored to the backend (still encrypted)
 * 4. The backend never has access to decryption keys
 * 
 * This maintains self-custody while providing backup redundancy.
 */
object BackupService {
    
    private const val BACKUP_VERSION = 1
    private const val HKDF_INFO = "voski/ln-backup/v1"
    
    /**
     * Create an encrypted backup snapshot of current Lightning state.
     * 
     * This should be called after every significant Lightning state change:
     * - Payment settled (sent or received)
     * - Channel opened/closed
     * - Channel state updated
     * - Voucher redeemed
     * 
     * @param trigger Description of what triggered the backup (for logging)
     */
    suspend fun snapshot(trigger: String) {
        try {
            println("[BackupService] Creating backup snapshot: $trigger")
            
            // Step 1: Serialize current Lightning state
            val channelManagerData = serializeChannelManager()
            val channelMonitorsData = serializeChannelMonitors()
            val walletData = serializeWalletData()
            
            val backupData = BackupData(
                version = BACKUP_VERSION,
                timestamp = getCurrentTimeMillis(),
                channelManager = channelManagerData,
                channelMonitors = channelMonitorsData,
                wallet = walletData,
                trigger = trigger
            )
            
            // Step 2: Encrypt the backup data
            val encryptedBackup = encryptBackup(backupData)
            
            // Step 3: Store locally (always)
            storeBackupLocally(encryptedBackup)
            
            // Step 4: Mirror to backend if enabled
            if (VoskiEnv.config.backupMode == VoskiConfig.BackupMode.DevicePlusMirror) {
                mirrorBackupToServer(encryptedBackup)
            }
            
            println("[BackupService] Backup snapshot completed successfully")
            
        } catch (e: Exception) {
            println("[BackupService] Failed to create backup snapshot: ${e.message}")
            // Don't throw - backup failures shouldn't crash the app
        }
    }
    
    /**
     * Restore Lightning state from the most recent backup.
     * 
     * This is used during wallet recovery when the user enters their seed phrase.
     * The backup data is decrypted using keys derived from the seed.
     * 
     * @param seedBytes The user's BIP39 seed bytes
     * @return True if restore was successful, false otherwise
     */
    suspend fun restore(seedBytes: ByteArray): Boolean {
        return try {
            println("[BackupService] Attempting to restore from backup...")
            
            // Step 1: Find the most recent backup
            val encryptedBackup = findLatestBackup()
            if (encryptedBackup == null) {
                println("[BackupService] No backup found for restore")
                return false
            }
            
            // Step 2: Decrypt using seed-derived keys
            val backupData = decryptBackup(encryptedBackup, seedBytes)
            
            // Step 3: Restore Lightning state
            restoreChannelManager(backupData.channelManager)
            restoreChannelMonitors(backupData.channelMonitors)
            restoreWalletData(backupData.wallet)
            
            println("[BackupService] Successfully restored from backup (${backupData.trigger})")
            true
            
        } catch (e: Exception) {
            println("[BackupService] Failed to restore from backup: ${e.message}")
            false
        }
    }
    
    /**
     * Check if backups are available for recovery.
     * 
     * @return True if encrypted backup files exist
     */
    suspend fun hasBackups(): Boolean {
        return try {
            findLatestBackup() != null
        } catch (e: Exception) {
            false
        }
    }
    
    /**
     * Get information about the latest backup.
     * 
     * @return Backup info or null if no backup exists
     */
    suspend fun getLatestBackupInfo(): BackupInfo? {
        return try {
            val backup = findLatestBackup() ?: return null
            BackupInfo(
                timestamp = backup.timestamp,
                sizeBytes = backup.encryptedData.size.toLong(),
                version = backup.version
            )
        } catch (e: Exception) {
            null
        }
    }
    
    private suspend fun serializeChannelManager(): ByteArray {
        // TODO: Access the Phoenix ChannelManager and serialize it
        // This needs to integrate with the existing Phoenix Lightning state
        // The ChannelManager contains the overall Lightning node state
        return ByteArray(0) // Placeholder
    }
    
    private suspend fun serializeChannelMonitors(): List<ByteArray> {
        // TODO: Access all ChannelMonitors and serialize them
        // Each channel has its own monitor that tracks the latest state
        // for breach detection and recovery
        return emptyList() // Placeholder
    }
    
    private suspend fun serializeWalletData(): ByteArray {
        // TODO: Serialize additional wallet metadata
        // This could include payment history, contacts, settings, etc.
        return ByteArray(0) // Placeholder
    }
    
    private suspend fun encryptBackup(backupData: BackupData): EncryptedBackup {
        // TODO: Implement AEAD encryption using seed-derived key
        // 1. Get user's seed bytes from secure storage
        // 2. Derive encryption key using HKDF(seed, HKDF_INFO)
        // 3. Generate random nonce/IV
        // 4. Encrypt backup data using ChaCha20-Poly1305 or AES-GCM
        // 5. Return encrypted data + nonce + authentication tag
        
        return EncryptedBackup(
            version = backupData.version,
            timestamp = backupData.timestamp,
            nonce = ByteArray(12), // Placeholder
            encryptedData = ByteArray(100), // Placeholder
            authTag = ByteArray(16) // Placeholder
        )
    }
    
    private suspend fun decryptBackup(encryptedBackup: EncryptedBackup, seedBytes: ByteArray): BackupData {
        // TODO: Implement AEAD decryption using seed-derived key
        // 1. Derive same encryption key using HKDF(seedBytes, HKDF_INFO)
        // 2. Decrypt using the stored nonce and verify auth tag
        // 3. Deserialize the BackupData structure
        
        return BackupData(
            version = encryptedBackup.version,
            timestamp = encryptedBackup.timestamp,
            channelManager = ByteArray(0),
            channelMonitors = emptyList(),
            wallet = ByteArray(0),
            trigger = "restored"
        )
    }
    
    private suspend fun storeBackupLocally(encryptedBackup: EncryptedBackup) {
        // TODO: Store encrypted backup to device storage
        // Android: Internal app storage (not accessible to other apps)
        // iOS: App container documents directory
        // Use timestamp-based filename for multiple backup versions
    }
    
    private suspend fun mirrorBackupToServer(encryptedBackup: EncryptedBackup) {
        // TODO: POST encrypted backup to backend
        // Backend stores the encrypted blob but cannot decrypt it
        // Include user's node pubkey as identifier
        // POST ${lspBaseUrl}/backups/mirror
    }
    
    private suspend fun findLatestBackup(): EncryptedBackup? {
        // TODO: Find the most recent backup file
        // Try device storage first, then backend mirror if configured
        return null
    }
    
    private suspend fun restoreChannelManager(data: ByteArray) {
        // TODO: Deserialize and restore ChannelManager state
    }
    
    private suspend fun restoreChannelMonitors(monitors: List<ByteArray>) {
        // TODO: Deserialize and restore all ChannelMonitor states
    }
    
    private suspend fun restoreWalletData(data: ByteArray) {
        // TODO: Restore wallet metadata and settings
    }
    
    private fun getCurrentTimeMillis(): Long = System.currentTimeMillis()
}

/**
 * Raw backup data before encryption.
 */
data class BackupData(
    val version: Int,
    val timestamp: Long,
    val channelManager: ByteArray,
    val channelMonitors: List<ByteArray>,
    val wallet: ByteArray,
    val trigger: String
)

/**
 * Encrypted backup data for storage.
 */
data class EncryptedBackup(
    val version: Int,
    val timestamp: Long,
    val nonce: ByteArray,
    val encryptedData: ByteArray,
    val authTag: ByteArray
)

/**
 * Information about a backup for display purposes.
 */
data class BackupInfo(
    val timestamp: Long,
    val sizeBytes: Long,
    val version: Int
)