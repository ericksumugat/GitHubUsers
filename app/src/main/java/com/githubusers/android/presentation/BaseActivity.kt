package com.githubusers.android.presentation

import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.NetworkCallback
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import com.githubusers.android.presentation.utils.NetworkStatus
import dagger.android.support.DaggerAppCompatActivity


abstract class BaseActivity : DaggerAppCompatActivity() {


    abstract fun onNetworkConnectionChange(networkStatus: NetworkStatus)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) registerDefaultNetworkCallback() else registerNetworkCallback()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun registerDefaultNetworkCallback() {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        connectivityManager.registerDefaultNetworkCallback(object :
            ConnectivityManager.NetworkCallback() {

            override fun onAvailable(network: Network) {
                onNetworkConnectionChange(NetworkStatus.CONNECTED)
            }

            override fun onLost(network: Network) {
                onNetworkConnectionChange(NetworkStatus.NO_CONNECTION)
            }
        })
    }

    private fun registerNetworkCallback() {
        val connectivityManager = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkCallback: NetworkCallback = object : NetworkCallback() {
            override fun onAvailable(network: Network) {
                onNetworkConnectionChange(NetworkStatus.CONNECTED)
            }

            override fun onLost(network: Network) {
                onNetworkConnectionChange(NetworkStatus.NO_CONNECTION)
            }
        }

        val request = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET).build()
        connectivityManager.registerNetworkCallback(request, networkCallback)
    }
}