package com.compose.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.MoneyOff
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.ui.graphics.vector.ImageVector

enum class RallyScreen(
    val icon: ImageVector
) {
    OverView(
        icon = Icons.Filled.PieChart
    ),
    Accounts(
        icon = Icons.Filled.AttachMoney
    ),
    Bills(
        icon = Icons.Filled.MoneyOff
    );

    companion object {
        fun fromRoute(route: String?): RallyScreen =
            when (route?.substringBefore("/")) {
                Accounts.name -> Accounts
                Bills.name -> Bills
                OverView.name -> OverView
                null -> OverView
                else -> throw IllegalArgumentException("Route $route is not recognized.")
            }
    }
}