package com.voskiapp.ui.screens.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class TransactionDetail(
    val id: String,
    val amount: Double,
    val title: String,
    val status: String,
    val date: String,
    val category: String,
    val from: String,
    val paymentMethod: String,
    val description: String?
)

data class DetailsState(
    val transaction: TransactionDetail? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

class DetailsViewModel : ViewModel() {
    private val _state = MutableStateFlow(DetailsState())
    val state: StateFlow<DetailsState> = _state.asStateFlow()
    
    fun loadTransactionDetails(transactionId: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            try {
                // Simulate API call
                val transaction = TransactionDetail(
                    id = transactionId,
                    amount = 500.0,
                    title = "Payment Received",
                    status = "Completed",
                    date = "October 15, 2025 at 2:30 PM",
                    category = "Income",
                    from = "Client Payment",
                    paymentMethod = "Bank Transfer",
                    description = "Monthly service payment"
                )
                
                _state.value = _state.value.copy(
                    transaction = transaction,
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
}



