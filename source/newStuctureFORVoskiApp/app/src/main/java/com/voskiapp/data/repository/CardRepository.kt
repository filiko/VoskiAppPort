package com.voskiapp.data.repository

import com.voskiapp.data.models.Card
import kotlinx.coroutines.delay

/**
 * Repository pattern implementation for card data
 */
interface CardRepository {
    suspend fun getCards(userId: String): Result<List<Card>>
    suspend fun addCard(card: Card): Result<Card>
    suspend fun removeCard(cardId: String): Result<Unit>
    suspend fun setDefaultCard(cardId: String): Result<Unit>
}

class CardRepositoryImpl : CardRepository {
    
    private val cards = mutableListOf<Card>()
    
    override suspend fun getCards(userId: String): Result<List<Card>> {
        return try {
            delay(500)
            Result.success(cards.filter { it.userId == userId })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun addCard(card: Card): Result<Card> {
        return try {
            delay(500)
            cards.add(card)
            Result.success(card)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun removeCard(cardId: String): Result<Unit> {
        return try {
            delay(500)
            cards.removeIf { it.id == cardId }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun setDefaultCard(cardId: String): Result<Unit> {
        return try {
            delay(500)
            cards.forEach { card ->
                if (card.id == cardId) {
                    // Update card to be default
                    // In real implementation, this would update the database
                }
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}



