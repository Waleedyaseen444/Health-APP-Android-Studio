package com.project.sfmd_project

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.os.Build
import androidx.annotation.RequiresApi

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
class NetworkCallbackImpl : ConnectivityManager.NetworkCallback() {

    override fun onAvailable(network: Network) {
        // Network is available
        // Handle your logic here, like syncing messages
    }

    override fun onLost(network: Network) {
        // Network is lost
        // Handle your logic here, maybe show a message to the user
    }

    override fun onCapabilitiesChanged(network: Network, networkCapabilities: NetworkCapabilities) {
        // Network capabilities changed
        // This is where you might check for internet connectivity changes
    }
}