package com.voskiapp.utils

/**
 * Builder Pattern implementation for creating complex objects
 * This demonstrates the Builder design pattern commonly used in Kotlin/Android development
 */

// Example: User Builder
data class User(
    val id: String,
    val name: String,
    val email: String,
    val phone: String?,
    val address: String?,
    val isVerified: Boolean
)

class UserBuilder {
    private var id: String = ""
    private var name: String = ""
    private var email: String = ""
    private var phone: String? = null
    private var address: String? = null
    private var isVerified: Boolean = false

    fun setId(id: String) = apply { this.id = id }
    fun setName(name: String) = apply { this.name = name }
    fun setEmail(email: String) = apply { this.email = email }
    fun setPhone(phone: String?) = apply { this.phone = phone }
    fun setAddress(address: String?) = apply { this.address = address }
    fun setVerified(verified: Boolean) = apply { this.isVerified = verified }

    fun build(): User {
        require(id.isNotEmpty()) { "ID cannot be empty" }
        require(name.isNotEmpty()) { "Name cannot be empty" }
        require(email.isNotEmpty()) { "Email cannot be empty" }
        
        return User(
            id = id,
            name = name,
            email = email,
            phone = phone,
            address = address,
            isVerified = isVerified
        )
    }
}

// Example: Card Builder
data class CardInfo(
    val cardNumber: String,
    val cardHolder: String,
    val expiryDate: String,
    val cvv: String,
    val cardType: CardType
)

enum class CardType {
    VISA, MASTERCARD, AMEX, DISCOVER
}

class CardBuilder {
    private var cardNumber: String = ""
    private var cardHolder: String = ""
    private var expiryDate: String = ""
    private var cvv: String = ""
    private var cardType: CardType = CardType.VISA

    fun setCardNumber(number: String) = apply { this.cardNumber = number }
    fun setCardHolder(holder: String) = apply { this.cardHolder = holder }
    fun setExpiryDate(date: String) = apply { this.expiryDate = date }
    fun setCvv(cvv: String) = apply { this.cvv = cvv }
    fun setCardType(type: CardType) = apply { this.cardType = type }

    fun build(): CardInfo {
        require(cardNumber.isNotEmpty()) { "Card number cannot be empty" }
        require(cardHolder.isNotEmpty()) { "Card holder name cannot be empty" }
        
        return CardInfo(
            cardNumber = cardNumber,
            cardHolder = cardHolder,
            expiryDate = expiryDate,
            cvv = cvv,
            cardType = cardType
        )
    }
}

// Example: Network Request Builder
data class NetworkRequest(
    val url: String,
    val method: String,
    val headers: Map<String, String>,
    val body: String?,
    val timeout: Long
)

class NetworkRequestBuilder {
    private var url: String = ""
    private var method: String = "GET"
    private var headers: MutableMap<String, String> = mutableMapOf()
    private var body: String? = null
    private var timeout: Long = 30000L

    fun setUrl(url: String) = apply { this.url = url }
    fun setMethod(method: String) = apply { this.method = method }
    fun addHeader(key: String, value: String) = apply { this.headers[key] = value }
    fun setBody(body: String?) = apply { this.body = body }
    fun setTimeout(timeout: Long) = apply { this.timeout = timeout }

    fun build(): NetworkRequest {
        require(url.isNotEmpty()) { "URL cannot be empty" }
        
        return NetworkRequest(
            url = url,
            method = method,
            headers = headers.toMap(),
            body = body,
            timeout = timeout
        )
    }
}



