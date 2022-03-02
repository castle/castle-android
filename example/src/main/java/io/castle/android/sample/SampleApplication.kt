/*
 * Copyright (c) 2020 Castle
 */
package io.castle.android.sample

import android.app.Application
import io.castle.android.Castle
import io.castle.android.CastleConfiguration
import java.util.*

class SampleApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        val baseURLAllowList = listOf("https://m.castle.io/")

        // Create configuration object
        val configuration = CastleConfiguration.Builder()
                .publishableKey("pk_CTsfAeRTqxGgA7HHxqpEESvjfPp4QAKA")
                .screenTrackingEnabled(true)
                .debugLoggingEnabled(true)
                .baseURLAllowList(baseURLAllowList)
                .build()

        // Setup Castle SDK with provided configuration
        Castle.configure(this, configuration)
    }
}