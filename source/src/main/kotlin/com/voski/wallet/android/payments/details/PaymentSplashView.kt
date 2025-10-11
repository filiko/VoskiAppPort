/*
 * Copyright 2025 ACINQ SAS
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

@file:Suppress("DEPRECATION")

package com.voski.wallet.android.payments.details

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import fr.acinq.lightning.db.AutomaticLiquidityPurchasePayment
import fr.acinq.lightning.db.Bolt11IncomingPayment
import fr.acinq.lightning.db.Bolt12IncomingPayment
import fr.acinq.lightning.db.ChannelCloseOutgoingPayment
import fr.acinq.lightning.db.IncomingPayment
import fr.acinq.lightning.db.LegacyPayToOpenIncomingPayment
import fr.acinq.lightning.db.LegacySwapInIncomingPayment
import fr.acinq.lightning.db.LightningOutgoingPayment
import fr.acinq.lightning.db.ManualLiquidityPurchasePayment
import fr.acinq.lightning.db.NewChannelIncomingPayment
import fr.acinq.lightning.db.SpliceCpfpOutgoingPayment
import fr.acinq.lightning.db.SpliceInIncomingPayment
import fr.acinq.lightning.db.SpliceOutgoingPayment
import fr.acinq.lightning.utils.UUID
import com.voski.wallet.R
import com.voski.wallet.android.components.BorderButton
import com.voski.wallet.android.components.DefaultScreenHeader
import com.voski.wallet.android.components.SplashLayout
import com.voski.wallet.android.payments.details.splash.SplashAutoLiquidityPurchase
import com.voski.wallet.android.payments.details.splash.SplashChannelClose
import com.voski.wallet.android.payments.details.splash.SplashIncomingBolt11
import com.voski.wallet.android.payments.details.splash.SplashIncomingBolt12
import com.voski.wallet.android.payments.details.splash.SplashIncomingLegacyPayToOpen
import com.voski.wallet.android.payments.details.splash.SplashIncomingLegacySwapIn
import com.voski.wallet.android.payments.details.splash.SplashIncomingNewChannel
import com.voski.wallet.android.payments.details.splash.SplashIncomingSpliceIn
import com.voski.wallet.android.payments.details.splash.SplashLightningOutgoing
import com.voski.wallet.android.payments.details.splash.SplashManualLiquidityPurchase
import com.voski.wallet.android.payments.details.splash.SplashSpliceOut
import com.voski.wallet.android.payments.details.splash.SplashSpliceOutCpfp
import com.voski.wallet.android.payments.details.splash.SplashStatusAutoLiquidityPurchase
import com.voski.wallet.android.payments.details.splash.SplashStatusChannelClose
import com.voski.wallet.android.payments.details.splash.SplashStatusIncoming
import com.voski.wallet.android.payments.details.splash.SplashStatusLightningOutgoing
import com.voski.wallet.android.payments.details.splash.SplashStatusManualLiquidityPurchase
import com.voski.wallet.android.payments.details.splash.SplashStatusSpliceOut
import com.voski.wallet.android.payments.details.splash.SplashStatusSpliceOutCpfp
import com.voski.wallet.android.utils.borderColor
import com.voski.wallet.data.WalletPaymentInfo

@Composable
fun PaymentDetailsSplashView(
    onBackClick: () -> Unit,
    data: WalletPaymentInfo,
    onDetailsClick: (UUID) -> Unit,
    onMetadataDescriptionUpdate: (UUID, String?) -> Unit,
    fromEvent: Boolean,
) {
    val payment = data.payment
    SplashLayout(
        header = { DefaultScreenHeader(onBackClick = onBackClick) },
        topContent = {
            when (payment) {
                is LightningOutgoingPayment -> SplashStatusLightningOutgoing(payment = payment, fromEvent = fromEvent)
                is ChannelCloseOutgoingPayment -> SplashStatusChannelClose(payment = payment, fromEvent = fromEvent, onCpfpSuccess = onBackClick)
                is SpliceOutgoingPayment -> SplashStatusSpliceOut(payment = payment, fromEvent = fromEvent, onCpfpSuccess = onBackClick)
                is SpliceCpfpOutgoingPayment -> SplashStatusSpliceOutCpfp(payment = payment, fromEvent = fromEvent, onCpfpSuccess = onBackClick)
                is ManualLiquidityPurchasePayment -> SplashStatusManualLiquidityPurchase(payment = payment, fromEvent = fromEvent)
                is AutomaticLiquidityPurchasePayment -> SplashStatusAutoLiquidityPurchase(payment = payment, fromEvent = fromEvent)
                is IncomingPayment -> SplashStatusIncoming(payment = payment, fromEvent = fromEvent)
            }
        }
    ) {
        @Suppress("DEPRECATION")
        when (payment) {
            is Bolt11IncomingPayment -> SplashIncomingBolt11(payment = payment, metadata = data.metadata, onMetadataDescriptionUpdate = onMetadataDescriptionUpdate)
            is Bolt12IncomingPayment -> SplashIncomingBolt12(payment = payment, metadata = data.metadata, onMetadataDescriptionUpdate = onMetadataDescriptionUpdate)
            is SpliceInIncomingPayment -> SplashIncomingSpliceIn(payment = payment, metadata = data.metadata, onMetadataDescriptionUpdate = onMetadataDescriptionUpdate)
            is NewChannelIncomingPayment -> SplashIncomingNewChannel(payment = payment, metadata = data.metadata, onMetadataDescriptionUpdate = onMetadataDescriptionUpdate)
            is LegacySwapInIncomingPayment -> SplashIncomingLegacySwapIn(payment = payment, metadata = data.metadata, onMetadataDescriptionUpdate = onMetadataDescriptionUpdate)
            is LegacyPayToOpenIncomingPayment -> SplashIncomingLegacyPayToOpen(payment = payment, metadata = data.metadata, onMetadataDescriptionUpdate = onMetadataDescriptionUpdate)

            is LightningOutgoingPayment -> SplashLightningOutgoing(payment = payment, metadata = data.metadata, onMetadataDescriptionUpdate = onMetadataDescriptionUpdate)
            is ChannelCloseOutgoingPayment -> SplashChannelClose(payment = payment, metadata = data.metadata, onMetadataDescriptionUpdate = onMetadataDescriptionUpdate)
            is SpliceCpfpOutgoingPayment -> SplashSpliceOutCpfp(payment = payment, metadata = data.metadata, onMetadataDescriptionUpdate = onMetadataDescriptionUpdate)
            is SpliceOutgoingPayment -> SplashSpliceOut(payment = payment, metadata = data.metadata, onMetadataDescriptionUpdate = onMetadataDescriptionUpdate)
            is ManualLiquidityPurchasePayment -> SplashManualLiquidityPurchase(payment)
            is AutomaticLiquidityPurchasePayment -> SplashAutoLiquidityPurchase(payment)
        }

        Spacer(modifier = Modifier.height(48.dp))
        BorderButton(
            text = stringResource(id = R.string.paymentdetails_details_button),
            borderColor = borderColor,
            textStyle = MaterialTheme.typography.caption,
            icon = R.drawable.ic_tool,
            iconTint = MaterialTheme.typography.caption.color,
            onClick = { onDetailsClick(data.id) },
        )
    }
}
