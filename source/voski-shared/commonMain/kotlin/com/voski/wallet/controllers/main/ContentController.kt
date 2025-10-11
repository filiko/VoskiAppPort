package com.voski.wallet.controllers.main

import co.touchlab.kermit.Logger
import fr.acinq.lightning.logging.LoggerFactory
import com.voski.wallet.VoskiBusiness
import com.voski.wallet.controllers.AppController
import com.voski.wallet.managers.WalletManager
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch


class AppContentController(
    loggerFactory: LoggerFactory,
    private val walletManager: WalletManager
) : AppController<Content.Model, Content.Intent>(
    loggerFactory = loggerFactory,
    firstModel = Content.Model.Waiting
) {
    constructor(business: VoskiBusiness): this(
        loggerFactory = business.loggerFactory,
        walletManager = business.walletManager
    )

    init {
        launch {
            if (walletManager.isLoaded()) {
                model(Content.Model.IsInitialized)
            } else {
                model(Content.Model.NeedInitialization)
                // Suspends until a wallet is created
                walletManager.keyManager.filterNotNull().first()
                model(Content.Model.IsInitialized)
            }
        }
    }

    override fun process(intent: Content.Intent) = error("Nothing to process")
}
