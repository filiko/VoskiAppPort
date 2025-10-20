package com.voskiapp.ui.screens.card

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class CardState(
    val cards: List<CreditCardInfo> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

class CardViewModel : ViewModel() {
    private val _state = MutableStateFlow(CardState())
    val state: StateFlow<CardState> = _state.asStateFlow()
    
    init {
        loadCards()
    }
    
    private fun loadCards() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            try {
                // Simulate API call
                val cards = sampleCards
                
                _state.value = _state.value.copy(
                    cards = cards,
                    isLoading = false
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    error = e.message,
                    isLoading = false
                )
            }
        }
    }
    
    fun addCard(card: CreditCardInfo) {
        viewModelScope.launch {
            val currentCards = _state.value.cards.toMutableList()
            currentCards.add(card)
            _state.value = _state.value.copy(cards = currentCards)
        }
    }
    
    fun removeCard(card: CreditCardInfo) {
        viewModelScope.launch {
            val currentCards = _state.value.cards.toMutableList()
            currentCards.remove(card)
            _state.value = _state.value.copy(cards = currentCards)
        }
    }
}



