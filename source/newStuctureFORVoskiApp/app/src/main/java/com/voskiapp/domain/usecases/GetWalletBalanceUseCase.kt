package com.voskiapp.domain.usecases

import com.voskiapp.data.models.Wallet
import com.voskiapp.data.repository.WalletRepository

/**
 * Use case for getting wallet balance
 * This demonstrates the Clean Architecture pattern with use cases
 */
class GetWalletBalanceUseCase(
    private val walletRepository: WalletRepository
) {
    suspend operator fun invoke(userId: String): Result<Double> {
        return try {
            val walletResult = walletRepository.getWallet(userId)
            if (walletResult.isSuccess) {
                Result.success(walletResult.getOrThrow().balance)
            } else {
                Result.failure(walletResult.exceptionOrNull() ?: Exception("Unknown error"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}



