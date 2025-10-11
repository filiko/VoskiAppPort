package com.voski.wallet.db

import com.voski.wallet.db.payments.*
import kotlinx.coroutines.*

class CloudKitDb(
    appDb: SqliteAppDb,
    paymentsDb: SqlitePaymentsDb
): CloudKitInterface, CoroutineScope by MainScope() {

    val contacts = CloudKitContactsDb(paymentsDb)
    val payments = CloudKitPaymentsDb(paymentsDb)
}
