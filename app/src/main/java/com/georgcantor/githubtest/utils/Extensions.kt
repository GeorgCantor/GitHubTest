package com.georgcantor.githubtest.utils

import android.content.Context
import android.net.ConnectivityManager

fun Context.isNetworkAvailable(): Boolean {
    val connectivityManager =
        this.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager

    connectivityManager?.let {
        val networkInfo = it.activeNetworkInfo
        networkInfo?.let { info ->
            if (info.isConnected) return true
        }
    }

    return false
}