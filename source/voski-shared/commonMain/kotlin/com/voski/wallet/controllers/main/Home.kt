package com.voski.wallet.controllers.main

import fr.acinq.lightning.MilliSatoshi
import com.voski.wallet.controllers.MVI

object Home {

    data class Model(
        val balance: MilliSatoshi?,
    ) : MVI.Model()

    val emptyModel = Model(
        balance = null,
    )

    sealed class Intent : MVI.Intent()
}
