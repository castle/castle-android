/*
 * Copyright (c) 2020 Castle
 */
package io.castle.android.sample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import io.castle.android.Castle
import io.castle.android.sample.databinding.MainBinding
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: MainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = MainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.identify.setOnClickListener { onIdentifyClick() }
        binding.trackScreen.setOnClickListener { onTrackScreenClick() }
        binding.flush.setOnClickListener { onFlushClick() }
        binding.reset.setOnClickListener { onResetClick() }
    }

    private fun onIdentifyClick() {
        // Set signature for secure mode
        Castle.secure("944d7d6c5187cafac297785bbf6de0136a2e10f31788e92b2822f5cfd407fa52")

        // Identify user with a unique identifier including user traits
        val traits: MutableMap<String, String> = HashMap()
        traits["email"] = "sebastiasimson@castle.io"
        Castle.identify("sebastiansimson", traits)
    }

    private fun onTrackScreenClick() {
        // Track a screen view
        Castle.screen("Menu")
    }

    private fun onFlushClick() {
        Castle.flush()
    }

    private fun onResetClick() {
        Castle.reset()
    }
}