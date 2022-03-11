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

        binding.userJwt.setOnClickListener { onUserJwtClick() }
        binding.trackScreen.setOnClickListener { onTrackScreenClick() }
        binding.trackCustom.setOnClickListener { onTrackCustomClick() }
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

    private fun onUserJwtClick() {
        // Identify with user encoded as jwt
        Castle.userJwt("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6ImVjMjQ0ZjMwLTM0MzItNGJiYy04OGYxLTFlM2ZjMDFiYzFmZSIsImVtYWlsIjoidGVzdEBleGFtcGxlLmNvbSIsInJlZ2lzdGVyZWRfYXQiOiIyMDIyLTAxLTAxVDA5OjA2OjE0LjgwM1oifQ.eAwehcXZDBBrJClaE0bkO9XAr4U3vqKUpyZ-d3SxnH0")
    }

    private fun onTrackScreenClick() {
        // Track a screen view
        Castle.screen("Menu")
    }

    private fun onTrackCustomClick() {
        Castle.custom("Added to cart", mapOf(
            "product" to "iPhone 13 Pro",
            "price" to 1099.99
        ))
    }

    private fun onFlushClick() {
        Castle.flush()
    }

    private fun onResetClick() {
        Castle.reset()
    }
}