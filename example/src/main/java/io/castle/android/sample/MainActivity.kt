/*
 * Copyright (c) 2020 Castle
 */
package io.castle.android.sample

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import io.castle.android.Castle
import io.castle.android.sample.databinding.MainBinding
import java.util.*

class MainActivity : AppCompatActivity() {
    private val LOCATION_PERMISSION_REQUESST_CODE: Int = 1001
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
        binding.location.setOnClickListener { onLocationClick() }
    }

    private fun onLocationClick() {
        requestLocationAccess()
    }

    private fun requestLocationAccess() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUESST_CODE)
        } else {
            Toast.makeText(this,"Permission already granted", Toast.LENGTH_SHORT).show()
        }
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