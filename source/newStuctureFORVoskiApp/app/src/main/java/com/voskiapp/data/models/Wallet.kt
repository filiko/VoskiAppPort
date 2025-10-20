package com.voskiapp.data.models

data class Wallet(
    val id: String,
    val userId: String,
    val balance: Double,
    val currency: String = "USD",
    val lastUpdated: Long = System.currentTimeMillis()
)

data class WalletTransaction(
    val id: String,
    val walletId: String,
    val type: TransactionType,
    val amount: Double,
    val currency: String = "USD",
    val description: String,
    val category: String,
    val status: TransactionStatus,
    val timestamp: Long = System.currentTimeMillis(),
    val metadata: Map<String, String> = emptyMap()
)

enum class TransactionType {
    CREDIT,
    DEBIT,
    TRANSFER,
    REFUND
}

enum class TransactionStatus {
    PENDING,
    COMPLETED,
    FAILED,
    CANCELLED
}



