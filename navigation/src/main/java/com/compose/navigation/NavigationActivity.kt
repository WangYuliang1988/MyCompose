package com.compose.navigation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.compose.navigation.data.UserData
import com.compose.navigation.ui.accounts.AccountsBody
import com.compose.navigation.ui.accounts.SingleAccountBody
import com.compose.navigation.ui.bills.BillsBody
import com.compose.navigation.ui.bills.SingleBillBody
import com.compose.navigation.ui.components.RallyTopAppBar
import com.compose.navigation.ui.overview.OverviewBody
import com.compose.navigation.ui.theme.RallyTheme

private val ROUTE_SINGLE_ACCOUNT = "${RallyScreen.Accounts.name}/{name}"
private val ROUTE_SINGLE_BILL = "${RallyScreen.Bills.name}/{name}"

private val URI_SINGLE_ACCOUNT = "rally://$ROUTE_SINGLE_ACCOUNT"
private val URI_SINGLE_BILL = "rally://$ROUTE_SINGLE_BILL"

class NavigationActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            RallyApp()
        }
    }
}

@Composable
fun RallyApp() {
    RallyTheme {
        val allScreens = RallyScreen.values().toList()
        val navController = rememberNavController()
        val backstackEntry = navController.currentBackStackEntryAsState()
        val currentScreen = RallyScreen.fromRoute(
            backstackEntry.value?.destination?.route
        )
        Scaffold(
            topBar = {
                RallyTopAppBar(
                    allScreens = allScreens,
                    onTabSelected = { screen -> navController.navigate(screen.name) },
                    currentScreen = currentScreen
                )
            }
        ) { innerPadding ->
            RallyNavHost(
                navController = navController,
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}

@Composable
fun RallyNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = RallyScreen.OverView.name,
        modifier = modifier
    ) {
        composable(RallyScreen.OverView.name) {
            OverviewBody(
                onClickSeeAllAccounts = { navController.navigate(RallyScreen.Accounts.name) },
                onClickSeeAllBills = { navController.navigate(RallyScreen.Bills.name) },
                onAccountClick = { name ->
                    NavigateToSingleAccount(navController, name)
                },
                onBillClick = { name ->
                    NavigateToSingleBill(navController, name)
                }
            )
        }
        composable(RallyScreen.Accounts.name) {
            AccountsBody(
                accounts = UserData.accounts,
                onAccountClick = { name ->
                    NavigateToSingleAccount(navController, name)
                }
            )
        }
        composable(RallyScreen.Bills.name) {
            BillsBody(
                bills = UserData.bills,
                onBillClick = { name ->
                    NavigateToSingleBill(navController, name)
                }
            )
        }
        composable(
            route = ROUTE_SINGLE_ACCOUNT,
            arguments = listOf(
                navArgument("name", ) {
                    type = NavType.StringType
                }
            ),
            deepLinks = listOf(
                navDeepLink {
                    uriPattern = URI_SINGLE_ACCOUNT
                }
            )
        ) { entry ->
            val accountName = entry.arguments?.getString("name")
            val account = UserData.getAccount(accountName)
            SingleAccountBody(account)
        }
        composable(
            route = ROUTE_SINGLE_BILL,
            arguments = listOf(
                navArgument("name") {
                    type = NavType.StringType
                }
            ),
            deepLinks = listOf(
                navDeepLink {
                    uriPattern = URI_SINGLE_BILL
                }
            )
        ) { entry ->
            val billName = entry.arguments?.getString("name")
            val bill = UserData.getBill(billName)
            SingleBillBody(bill)
        }
    }
}

private fun NavigateToSingleAccount(
    navController: NavHostController,
    name: String
) {
    navController.navigate("${RallyScreen.Accounts.name}/$name")
}

private fun NavigateToSingleBill(
    navController: NavHostController,
    name: String
) {
    navController.navigate("${RallyScreen.Bills.name}/$name")
}

@Preview
@Composable
private fun RallyAppPreview() {
    RallyApp()
}