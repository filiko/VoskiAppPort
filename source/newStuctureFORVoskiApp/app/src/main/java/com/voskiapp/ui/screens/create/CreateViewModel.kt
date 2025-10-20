package com.voskiapp.ui.screens.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class CreateCardState(
    val cardNumber: String = "",
    val cardHolder: String = "",
    val expiryDate: String = "",
    val cvv: String = "",
    val isSaving: Boolean = false,
    val saveSuccess: Boolean = false,
    val error: String? = null
)

class CreateViewModel : ViewModel() {
    private val _state = MutableStateFlow(CreateCardState())
    val state: StateFlow<CreateCardState> = _state.asStateFlow()
    
    fun updateCardNumber(value: String) {
        _state.value = _state.value.copy(cardNumber = value)
    }
    
    fun updateCardHolder(value: String) {
        _state.value = _state.value.copy(cardHolder = value)
    }
    
    fun updateExpiryDate(value: String) {
        _state.value = _state.value.copy(expiryDate = value)
    }
    
    fun updateCvv(value: String) {
        _state.value = _state.value.copy(cvv = value)
    }
    
    fun saveCard() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isSaving = true, error = null)
            
            try {
                // Validate card details
                validateCardDetails()
                
                // Simulate API call to save card
                // In real app, this would call a repository
                kotlinx.coroutines.delay(1000)
                
                _state.value = _state.value.copy(
                    isSaving = false,
                    saveSuccess = true
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isSaving = false,
                    error = e.message
                )
            }
        }
    }
    
    private fun validateCardDetails() {
        val state = _state.value
        
        when {
            state.cardNumber.isEmpty() -> throw Exception("Card number is required")
            state.cardNumber.length < 13 -> throw Exception("Invalid card number")
            state.cardHolder.isEmpty() -> throw Exception("Card holder name is required")
            state.expiryDate.isEmpty() -> throw Exception("Expiry date is required")
            !isValidExpiryDate(state.expiryDate) -> throw Exception("Invalid expiry date")
            state.cvv.isEmpty() -> throw Exception("CVV is required")
            state.cvv.length < 3 -> throw Exception("Invalid CVV")
        }
    }
    
    private fun isValidExpiryDate(date: String): Boolean {
        if (!date.matches(Regex("\\d{2}/\\d{2}"))) return false
        
        val parts = date.split("/")
        val month = parts[0].toIntOrNull() ?: return false
        val year = parts[1].toIntOrNull() ?: return false
        
        return month in 1..12 && year >= 0
    }
    
    fun resetState() {
        _state.value = CreateCardState()
    }
}



