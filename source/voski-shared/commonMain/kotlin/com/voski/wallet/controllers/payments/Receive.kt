package com.voski.wallet.controllers.payments

import fr.acinq.lightning.MilliSatoshi
import com.voski.wallet.controllers.MVI

object Receive {

    sealed class Model : MVI.Model() {
        object Awaiting : Model()
        object Generating: Model()
        data class Generated(val request: String, val paymentHash: String, val amount: MilliSatoshi?, val desc: String?): Model()
    }

    sealed class Intent : MVI.Intent() {
        data class Ask(
            val amount: MilliSatoshi?,
            val desc: String?,
            val expirySeconds: Long = 3600 * 24 * 7 // 7 days
        ) : Intent()
    }

}
