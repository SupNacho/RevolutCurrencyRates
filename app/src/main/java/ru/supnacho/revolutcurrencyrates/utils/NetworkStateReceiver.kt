package ru.supnacho.revolutcurrencyrates.utils

import android.annotation.TargetApi
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build

class NetworkStateReceiver(private val callback: OnConnectionChanged): BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        val connectivityManager =
            context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val isConnected = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            newConnectionCheck(connectivityManager)
        else
            oldConnectionCheck(connectivityManager)

        if (isConnected)
            callback.onRestoreConnection()
        else
            callback.onLostConnection()
    }

    @TargetApi(Build.VERSION_CODES.M)
    private fun newConnectionCheck(connectivityManager: ConnectivityManager): Boolean {
        val network = connectivityManager.activeNetwork
        val connection = connectivityManager.getNetworkCapabilities(network)

        return connection?.let {
            it.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                    it.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
        } ?: false
    }

    private fun oldConnectionCheck(
        connectivityManager: ConnectivityManager
    ): Boolean {
        val activeNetwork = connectivityManager.activeNetworkInfo
        return activeNetwork?.let {
            activeNetwork.type == ConnectivityManager.TYPE_WIFI ||
                    activeNetwork.type == ConnectivityManager.TYPE_MOBILE
        } ?: false
    }

    interface OnConnectionChanged {
        fun onLostConnection()
        fun onRestoreConnection()
    }
}