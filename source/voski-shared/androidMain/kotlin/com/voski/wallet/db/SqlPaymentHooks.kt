package com.voski.wallet.db

import fr.acinq.lightning.utils.UUID
import com.voski.wallet.db.payments.CloudKitInterface
import com.voski.wallet.db.sqldelight.AppDatabase
import com.voski.wallet.db.sqldelight.PaymentsDatabase

actual fun didSaveWalletPayment(id: UUID, database: PaymentsDatabase) {}
actual fun didDeleteWalletPayment(id: UUID, database: PaymentsDatabase) {}
actual fun didUpdateWalletPaymentMetadata(id: UUID, database: PaymentsDatabase) {}

actual fun didSaveContact(contactId: UUID, database: PaymentsDatabase) {}
actual fun didDeleteContact(contactId: UUID, database: PaymentsDatabase) {}

actual fun makeCloudKitDb(appDb: SqliteAppDb, paymentsDb: SqlitePaymentsDb): CloudKitInterface? {
    return null
}