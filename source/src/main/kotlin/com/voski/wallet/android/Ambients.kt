/*
 * Copyright 2020 ACINQ SAS
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

package com.voski.wallet.android

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.voski.wallet.VoskiBusiness
import com.voski.wallet.android.utils.UserTheme
import com.voski.wallet.android.utils.datastore.InternalDataRepository
import com.voski.wallet.android.utils.datastore.PreferredBitcoinUnits
import com.voski.wallet.android.utils.datastore.UserPrefsRepository
import com.voski.wallet.controllers.ControllerFactory
import com.voski.wallet.data.*
import com.voski.wallet.managers.AppConfigurationManager


typealias CF = ControllerFactory

val LocalTheme = staticCompositionLocalOf { UserTheme.SYSTEM }
val LocalBusiness = staticCompositionLocalOf<VoskiBusiness?> { null }
val LocalControllerFactory = staticCompositionLocalOf<ControllerFactory?> { null }
val LocalNavController = staticCompositionLocalOf<NavController?> { null }
val LocalBitcoinUnits = compositionLocalOf { PreferredBitcoinUnits(primary = BitcoinUnit.Sat) }
val LocalFiatCurrencies = compositionLocalOf { AppConfigurationManager.PreferredFiatCurrencies(primary = FiatCurrency.USD, others = emptyList()) }
val LocalExchangeRatesMap = compositionLocalOf<Map<FiatCurrency, ExchangeRate.BitcoinPriceRate>> { emptyMap() }
val LocalShowInFiat = compositionLocalOf { false }
val isDarkTheme: Boolean
    @Composable
    get() = LocalTheme.current.let { it == UserTheme.DARK || (it == UserTheme.SYSTEM && isSystemInDarkTheme()) }

val navController: NavController
    @Composable
    get() = LocalNavController.current ?: error("navigation controller is not available")

val preferredAmountUnit: CurrencyUnit
    @Composable
    get() = if (LocalShowInFiat.current) LocalFiatCurrencies.current.primary else LocalBitcoinUnits.current.primary

val primaryFiatRate: ExchangeRate.BitcoinPriceRate?
    @Composable
    get() = LocalFiatCurrencies.current.primary.let { prefFiat -> LocalExchangeRatesMap.current[prefFiat] }

val internalData: InternalDataRepository
    @Composable
    get() = application.internalDataRepository

val userPrefs: UserPrefsRepository
    @Composable
    get() = application.userPrefs

val controllerFactory: ControllerFactory
    @Composable
    get() = LocalControllerFactory.current ?: error("No controller factory set. Please use appView or mockView.")

val business: VoskiBusiness
    @Composable
    get() = LocalBusiness.current ?: error("business is not available")

val application: VoskiApplication
    @Composable
    get() = LocalContext.current.applicationContext as? VoskiApplication ?: error("Application is not of type VoskiApplication. Are you using appView in preview?")
