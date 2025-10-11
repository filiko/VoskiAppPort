package com.voski.wallet.controllers.config

import com.voski.wallet.controllers.MVI

object Configuration {

    sealed class Model : MVI.Model() {
        object SimpleMode : Model()
        object FullMode : Model()
    }

    sealed class Intent : MVI.Intent()
}
