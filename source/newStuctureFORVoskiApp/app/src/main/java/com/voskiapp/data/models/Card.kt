package com.voskiapp.data.models

data class Card(
    val id: String,
    val userId: String,
    val cardNumber: String, // Last 4 digits only for security
    val cardHolderName: String,
    val expiryMonth: Int,
    val expiryYear: Int,
    val cardType: CardType,
    val isDefault: Boolean = false,
    val isActive: Boolean = true,
    val createdAt: Long = System.currentTimeMillis()
)

enum class CardType {
    VISA,
    MASTERCARD,
    AMEX,
    DISCOVER,
    UNKNOWN
}

fun getCardTypeFromNumber(number: String): CardType {
    return when {
        number.startsWith("4") -> CardType.VISA
        number.startsWith("5") -> CardType.MASTERCARD
        number.startsWith("34") || number.startsWith("37") -> CardType.AMEX
        number.startsWith("6") -> CardType.DISCOVER
        else -> CardType.UNKNOWN
    }
}



