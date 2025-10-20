package com.voskiapp.ui.screens.wallet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class WalletState(
    val balance: Double = 0.0,
    val transactions: List<Transaction> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

class WalletViewModel : ViewModel() {
    private val _state = MutableStateFlow(WalletState())
    val state: StateFlow<WalletState> = _state.asStateFlow()
    
    init {
        loadWalletData()
    }
    
    private fun loadWalletData() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            try {
                // Simulate API call
                val mockTransactions = sampleTransactions
                val totalBalance = calculateBalance(mockTransactions)
                
                _state.value = _state.value.copy(
                    balance = totalBalance,
                    transactions = mockTransactions,
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
    
    private fun calculateBalance(transactions: List<Transaction>): Double {
        return transactions.sumOf { 
            if (it.isPositive) it.amount.replace("+$", "").replace(",", "").toDoubleOrNull() ?: 0.0
            else -(it.amount.replace("-$", "").replace(",", "").toDoubleOrNull() ?: 0.0)
        }
    }
    
    fun refreshWallet() {
        loadWalletData()
    }
}



