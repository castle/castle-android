package io.castle.android.api.model;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.content.PermissionChecker;
import android.telephony.TelephonyManager;

import static android.Manifest.permission.ACCESS_NETWORK_STATE;
import static android.content.Context.CONNECTIVITY_SERVICE;
import static android.content.Context.TELEPHONY_SERVICE;
import static android.net.ConnectivityManager.TYPE_BLUETOOTH;
import static android.net.ConnectivityManager.TYPE_MOBILE;
import static android.net.ConnectivityManager.TYPE_WIFI;
import static android.support.v4.content.PermissionChecker.PERMISSION_GRANTED;

/**
 * Copyright (c) 2017 Castle
 */
public class Network {
    Boolean bluetooth = null;
    String carrier = "unknown";
    Boolean cellular = null;
    Boolean wifi = null;

    @SuppressLint("MissingPermission")
    private Network(Context context) {
        if (PermissionChecker.checkCallingOrSelfPermission(context, ACCESS_NETWORK_STATE) == PERMISSION_GRANTED) {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
            if (connectivityManager != null) {
                NetworkInfo wifiInfo = connectivityManager.getNetworkInfo(TYPE_WIFI);
                wifi = wifiInfo != null && wifiInfo.isConnected();

                NetworkInfo bluetoothInfo = connectivityManager.getNetworkInfo(TYPE_BLUETOOTH);
                bluetooth = bluetoothInfo != null && bluetoothInfo.isConnected();

                NetworkInfo cellularInfo = connectivityManager.getNetworkInfo(TYPE_MOBILE);
                cellular = cellularInfo != null && cellularInfo.isConnected();
            }
        }

        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(TELEPHONY_SERVICE);
        if (telephonyManager != null) {
            carrier = telephonyManager.getNetworkOperatorName();
        }
    }

    static Network create(Context context) {
        return new Network(context);
    }
}