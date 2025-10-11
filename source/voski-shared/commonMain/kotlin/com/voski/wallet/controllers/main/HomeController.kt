package com.voski.wallet.controllers.main

import fr.acinq.lightning.logging.LoggerFactory
import com.voski.wallet.VoskiBusiness
import com.voski.wallet.controllers.AppController
import com.voski.wallet.managers.BalanceManager
import kotlinx.coroutines.launch


class AppHomeController(
    loggerFactory: LoggerFactory,
    private val balanceManager: BalanceManager
) : AppController<Home.Model, Home.Intent>(
    loggerFactory = loggerFactory,
    firstModel = Home.emptyModel
) {
    constructor(business: VoskiBusiness): this(
        loggerFactory = business.loggerFactory,
        balanceManager = business.balanceManager
    )

    init {
        launch {
            balanceManager.balance.collect {
                model { copy(balance = it) }
            }
        }
    }

    override fun process(intent: Home.Intent) {}
}
