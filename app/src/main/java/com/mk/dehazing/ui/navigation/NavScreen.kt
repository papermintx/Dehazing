package com.mk.dehazing.ui.navigation

import android.net.Uri

sealed class NavScreen(val route: String) {
    data object Home : NavScreen("home")

    data object Result : NavScreen("result?uri={uri}") {
        fun createRoute(uri: Uri): String {
            return "result?uri=${Uri.encode(uri.toString())}"
        }
    }
}