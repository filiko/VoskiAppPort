package com.voski.wallet.utils

import fr.acinq.lightning.MilliSatoshi
import fr.acinq.lightning.utils.UUID
import com.voski.wallet.data.ContactInfo
import com.voski.wallet.data.LocalChannelInfo
import com.voski.wallet.data.availableForReceive
import com.voski.wallet.data.canRequestLiquidity
import com.voski.wallet.data.inFlightPaymentsCount

/**
 * Workarounds for various shortcomings between Kotlin and iOS.
 */

//fun NodeParamsManager.Companion._liquidityLeaseRate(amount: Satoshi): LiquidityAds_LeaseRate {
//    val result = this.liquidityLeaseRate(amount)
//    return LiquidityAds_LeaseRate(result)
//}

fun LocalChannelInfo.Companion.availableForReceive(
    channels: List<LocalChannelInfo>
): MilliSatoshi? {
    return channels.availableForReceive()
}

fun LocalChannelInfo.Companion.canRequestLiquidity(
    channels: List<LocalChannelInfo>
): Boolean {
    return channels.canRequestLiquidity()
}

fun LocalChannelInfo.Companion.inFlightPaymentsCount(
    channels: List<LocalChannelInfo>
): Int {
    return channels.inFlightPaymentsCount()
}