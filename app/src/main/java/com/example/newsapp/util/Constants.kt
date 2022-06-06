package com.example.newsapp.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build

// various constants used in the app
class Constants {
    companion object {
        const val API_KEY = "25013eb3a0ab4bfd96f058f49fd4fa6b"

        const val BASE_URL = "https://newsapi.org/"

        fun hasInternetConnection(context: Context?): Boolean {
            val connectivityManager =
                context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val activeNetwork = connectivityManager.activeNetwork ?: return false
                val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork)
                return when {
                    capabilities?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) == true -> true
                    capabilities?.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) == true -> true
                    capabilities?.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) == true -> true
                    else -> false
                }
            } else {
                connectivityManager.activeNetworkInfo?.run {
                    return when (type) {
                        ConnectivityManager.TYPE_WIFI -> true
                        ConnectivityManager.TYPE_MOBILE -> true
                        ConnectivityManager.TYPE_ETHERNET -> true
                        else -> false
                    }
                }
            }
            return false
        }
    }
}