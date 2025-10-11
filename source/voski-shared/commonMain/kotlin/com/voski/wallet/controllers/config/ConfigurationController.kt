package com.voski.wallet.controllers.config

import co.touchlab.kermit.Logger
import fr.acinq.lightning.logging.LoggerFactory
import com.voski.wallet.VoskiBusiness
import com.voski.wallet.managers.WalletManager
import com.voski.wallet.controllers.AppController
import kotlinx.coroutines.launch


class AppConfigurationController(
    loggerFactory: LoggerFactory,
    private val walletManager: WalletManager
) : AppController<Configuration.Model, Configuration.Intent>(
    loggerFactory = loggerFactory,
    firstModel = Configuration.Model.SimpleMode
) {
    constructor(business: VoskiBusiness): this(
        loggerFactory = business.loggerFactory,
        walletManager = business.walletManager
    )

    init {
        launch {
            model(
                if (!walletManager.isLoaded())
                    Configuration.Model.SimpleMode
                else
                    Configuration.Model.FullMode
            )
        }
    }

    override fun process(intent: Configuration.Intent) = error("Nothing to process")
}
