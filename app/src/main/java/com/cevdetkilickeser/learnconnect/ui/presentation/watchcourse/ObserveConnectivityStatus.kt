package com.cevdetkilickeser.learnconnect.ui.presentation.watchcourse

import android.content.Context
import android.content.Context.CONNECTIVITY_SERVICE
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

@Composable
fun ObserveConnectivityStatus(context: Context): Boolean {
    val connectivityManager = remember {
        context.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
    }
    val isConnected = remember {
        mutableStateOf(
            connectivityManager.activeNetwork?.let { network ->
                val capabilities = connectivityManager.getNetworkCapabilities(network)
                capabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
            } ?: false
        )
    }

    DisposableEffect(Unit) {
        val networkCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: android.net.Network) {
                isConnected.value = true
            }

            override fun onLost(network: android.net.Network) {
                isConnected.value = false
            }
        }
        connectivityManager.registerDefaultNetworkCallback(networkCallback)
        onDispose {
            connectivityManager.unregisterNetworkCallback(networkCallback)
        }
    }

    return isConnected.value
}